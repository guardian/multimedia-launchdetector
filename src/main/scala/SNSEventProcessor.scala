import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global
import io.circe.parser._
import com.gu.crier.model.event.v1._
//intellij thinks that this is unused. But, if it is not imported, then Scala can't find a decoder for Option[EventPayload]
import cats.syntax.either._
import com.gu.contentapi.client.model.v1._
import com.gu.contentapi.json.CirceDecoders._
import com.gu.crier.model.event.v1.EventPayload.Atom
import com.gu.fezziwig.CirceScroogeMacros._
import io.circe.Decoder._
import io.circe._

object SNSEventProcessor {
  def process(records:Seq[SNSRecord])(func: Event=>Future[Try[Boolean]]):Future[Seq[Try[Boolean]]] = {
    val processFutureList:Seq[Future[Try[Boolean]]] = records.map { record=>
      eventFromRecord(record) match {
        case Right(event)=>func(event)
        case Left(error)=>Future(Failure(error))
      }
    }
    Future.sequence(processFutureList)
  }

  def eventFromRecord(record: SNSRecord):Either[io.circe.Error, Event] = eventFromString(record.getSNS.getMessage)

  def eventFromString(string: String):Either[io.circe.Error, Event] = decode[Event](string)

}
