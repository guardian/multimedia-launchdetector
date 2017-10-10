import com.gu.crier.model.event.v1.Event

import scala.concurrent.Future
import scala.util.{Failure, Try}
import scala.concurrent.ExecutionContext.Implicits.global

object PlutoSender {
  def sendToVidispine(record: Event):Future[Try[Boolean]] = {
    Future(Failure(new RuntimeException("Not yet implemented")))
  }
}
