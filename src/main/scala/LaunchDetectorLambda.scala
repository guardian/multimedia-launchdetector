import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord
import com.gu.crier.model.event.v1._
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

import scala.collection.JavaConverters._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import org.apache.logging.log4j.scala.Logging

class LaunchDetectorLambda extends RequestHandler[SNSEvent, Unit] with Logging {
  override def handleRequest(incomingEvent:SNSEvent, context: Context): Unit = {
    val rawRecords: List[SNSRecord] = incomingEvent.getRecords.asScala.toList

    logger.info(s"Processing ${rawRecords.size} records ...")
    val processingResults = SNSEventProcessor.process(rawRecords) { event=>
      event.itemType match {
        case ItemType.Atom=>
          PlutoSender.sendToVidispine(event)
        case _=>
          println("This event is for something other than an atom, not going to do anything.")
          Future(Success(false))
      }
    }

    val results = Await.result(processingResults, (context.getRemainingTimeInMillis-100).millis)
    //Success(true) means that we processed it
    val success = results.filter({
      case Success(result)=> result
      case _=>false
    })
    //Success(false) means that we ignored it, but there was no error
    val ignored = results.filter({
      case Success(result)=> !result
      case _=>false
    })

    //Failure(err) means that there was a processing error
    val errors = results
      .filter(_.isFailure)
      .map({
        case Failure(err)=>err
        case _=>new Exception //this should never be matched, as we filtered on isFailure, but is included to prevent a compiler warning
      })

    println(s"Processed ${success.length} entries successfully and ignored ${ignored.length}")
    if(errors.nonEmpty){
      println(s"Got the following ${errors.length} errors while processing")
      val msgs = errors.map(_.getMessage).mkString("\n")
      println(msgs)
      throw errors.head //shows as an error to Lambda
    }
  }
}
