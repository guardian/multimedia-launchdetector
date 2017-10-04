import org.apache.commons.codec.digest.DigestUtils
import com.amazonaws.services.kinesis.clientlibrary.types.UserRecord
import com.amazonaws.services.kinesis.model.Record
import com.amazonaws.services.lambda.runtime.events.KinesisEvent
import com.amazonaws.services.lambda.runtime
import com.gu.contentatom.thrift.Atom
import com.gu.crier.model.event.v1._

import scala.collection.JavaConverters._

class LaunchDetectorLambda {
  def handle(event:KinesisEvent): Unit = {
    val rawRecords: List[Record] = event.getRecords.asScala.map(_.getKinesis).toList
    val userRecords = UserRecord.deaggregate(rawRecords.asJava)

    println(s"Processing ${userRecords.size} records ...")
    CrierEventProcessor.process(userRecords.asScala) { event=>
      event.itemType match {
        case ItemType.Atom=>
          event.payload.exists({
            case EventPayload.Atom(atom)=>
              AtomEventProcessor.process(atom)
            case _=>
              false
          })
        case _=>
          println("This event is for something other than an atom, not going to do anything.")
          false
      }
    }
  }
}
