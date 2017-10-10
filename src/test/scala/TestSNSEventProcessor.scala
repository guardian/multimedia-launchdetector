import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord
import com.gu.crier.model.event.v1._
import org.apache.logging.log4j.scala.Logging
import org.scalatest.{FunSuite, MustMatchers}

import scala.io.Source
import com.gu.contentapi.json.CirceDecoders._
import com.gu.fezziwig.CirceScroogeMacros._
import io.circe.Decoder.Result
import io.circe._
import io.circe.parser._
import io.circe.syntax._

class TestSNSEventProcessor extends FunSuite with MustMatchers with Logging with TestHelpers {
  test("eventFromString should parse a json string"){
    val event = SNSEventProcessor.eventFromString(slurpFile("src/test/scala/data/testevent.json"))
    event.isRight must be(true)
  }

  test("eventFromRecord should decode an SNS record"){
    val sns = new SNSEvent.SNS
    sns.setMessage(slurpFile("src/test/scala/data/testevent.json"))
    sns.setMessageId("fakeMessageId")

    val rec = new SNSRecord
    rec.setEventSource("fakeSource")
    rec.setSns(sns)

    val event = SNSEventProcessor.eventFromRecord(rec)
    event.isRight must be(true)
  }
}
