/* based on https://github.com/guardian/fastly-cache-purger/blob/master/src/main/scala/com/gu/fastly/CrierEventProcessor.scala */
import java.nio.ByteBuffer

import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord
import com.gu.crier.model.event.v1.Event
import java.util.Base64
import java.nio.charset.StandardCharsets

import scala.util.Try

object CrierEventProcessor {
  def process(records:Seq[SNSRecord])(func: Event=>Boolean):Unit = {
    val processingResults:Iterable[Boolean] = records.flatMap { record=>
      val event = eventFromRecord(record)
      event.map { e=>
        func(e)
      }.recover {
        case error=>
          println("Unable to deserialise event from stream, skipping")
          false
      }.toOption
    }
    val handledCount: Int = processingResults.count(_ == true)
    println(s"Successfully handled $handledCount pieces of content")
  }

  def eventFromRecord(record: SNSRecord):Try[Event] = {
    val buffer = ByteBuffer.wrap(Base64.getDecoder.decode(record.getSNS.getMessage))

    Try(ThriftDeserializer.fromByteBuffer(buffer)(Event.decode))
  }
}
