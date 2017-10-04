import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
//import com.gu.contentapi.json.CirceDecoders
//import com.gu.crier.model.event.v1.Event

class FirstTestLambda extends RequestStreamHandler {
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val inputData = scala.io.Source.fromInputStream(input).mkString
    println(inputData)
    val decoded_event = decode[PingEvent](inputData)
    println(decoded_event)
//
//    val another_event = decode[Event](inputData)
//    println(another_event)
  }
}
