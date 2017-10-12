import java.util.Base64
import com.gu.crier.model.event.v1._
import org.scalatest.{FunSuite, MustMatchers}
import com.amazonaws.services.lambda.runtime.events.SNSEvent.{SNS, SNSRecord}

class TestCrierEventProcessor extends FunSuite with MustMatchers with TestHelpers {
  val successful = 'success

  test("eventFromRecord should decode an Event from binary"){


    val result = CrierEventProcessor.eventFromRecord(wrapSns(genBinaryEvent))
    result must be(successful)

    result.get.payloadId must be("fakeIdString")
    result.get.eventType must be(EventType.Update)
    result.get.itemType must be (ItemType.Atom)
    result.get.dateTime must be (1507221293)
    result.get.payload must be(defined)

  }
}
