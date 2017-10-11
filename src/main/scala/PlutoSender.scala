import com.gu.crier.model.event.v1.Event
import com.gu.crier.model.event.v1.EventPayload.Atom
import org.apache.logging.log4j.scala.Logging

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

object PlutoSender extends Logging {

  def sendToVidispine(record: Event):Future[Try[Boolean]] = Future {
    Failure(new RuntimeException("Not yet implemented"))
  }
}
