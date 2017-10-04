/* based on https://github.com/guardian/fastly-cache-purger/blob/master/src/main/scala/com/gu/fastly/CrierEventProcessor.scala */
import com.amazonaws.services.kinesis.model.Record
import com.gu.crier.model.event.v1.Event

import scala.util.Try

object CrierEventProcessor {
  def process(records:Seq[Record])(func: Event=>Boolean):Unit = {
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

  def eventFromRecord(record: Record):Try[Event] = {
    val buffer = record.getData
    Try(ThriftDeserializer.fromByteBuffer(buffer)(Event.decode))
  }
}
