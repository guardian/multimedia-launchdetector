import java.util.Base64

import com.gu.contentatom.thrift.{Atom, AtomData, AtomType, ContentChangeDetails}
import com.gu.contentatom.thrift.atom.media._
import com.gu.crier.model.event.v1._
import org.apache.thrift.protocol.TCompactProtocol
import org.apache.thrift.transport.{TMemoryBuffer, TTransportFactory}
import org.scalatest.{FunSuite, MustMatchers}
import com.amazonaws.services.lambda.runtime.events.SNSEvent.{SNS, SNSRecord}

class TestCrierEventProcessor extends FunSuite with MustMatchers {
  val successful = 'success

  test("eventFromRecord should decode an Event from binary"){
    def genBinaryEvent:Array[Byte] = {
      val asset = Asset(AssetType.Audio, version = 1, id = "SomeAssetId", platform = Platform.Url)
      val mediaAtom = MediaAtom(Seq(asset), title = "some title", category = Category.News)

      val atom = Atom(id = "someId", atomType = AtomType.Media, defaultHtml = "<p></p>", data = AtomData.Media(mediaAtom), contentChangeDetails = ContentChangeDetails(revision = 1))
      val payload = EventPayload.Atom(atom.asInstanceOf[EventPayloadAliases.AtomAlias])

      val event = Event(payloadId = "fakeIdString",
        eventType = EventType.Update,
        itemType = ItemType.Atom,
        dateTime = 1507221293,
        payload = Some(payload)
      )

      val protocolFactory = new TCompactProtocol.Factory()
      val transport = new TMemoryBuffer(64)

      Event.encode(event, protocolFactory.getProtocol(transport))
      transport.getArray
    }

    val sns = new SNS
    sns.setMessage(Base64.getEncoder.encodeToString(genBinaryEvent))

    println(sns.getMessage)
    val snsRecord = new SNSRecord
    snsRecord.setSns(sns)

    val result = CrierEventProcessor.eventFromRecord(snsRecord)
    result must be(successful)

    result.get.payloadId must be("fakeIdString")
    result.get.eventType must be(EventType.Update)
    result.get.itemType must be (ItemType.Atom)
    result.get.dateTime must be (1507221293)
    result.get.payload must be(defined)

  }
}
