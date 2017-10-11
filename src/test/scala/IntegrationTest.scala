import java.util

import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.lambda.runtime.events.SNSEvent.{SNS, SNSRecord}
import com.gu.crier.model.event.v1._
import org.apache.logging.log4j.scala.Logging
import org.scalatest.{FunSuite, MustMatchers}

import scala.collection.JavaConverters._
import com.amazonaws.services.lambda.runtime._

class FakeLogger extends LambdaLogger {
    override def log(string: String): Unit = {}
}

class FakeClient extends Client {
    override def getAppPackageName: String = ""

    override def getInstallationId: String = ""

    override def getAppTitle: String = ""

    override def getAppVersionCode: String = ""

    override def getAppVersionName: String = ""
}

class FakeClientContext extends ClientContext {
  override def getCustom: util.Map[String, String] = Map("test"->"test").asJava

  override def getEnvironment: util.Map[String, String] = Map("test"->"test").asJava

  override def getClient: Client = new FakeClient
}

class FakeContext extends Context {
  override def getFunctionName: String = "testFunction"

  override def getRemainingTimeInMillis: Int = 1000

  override def getLogger: LambdaLogger = new FakeLogger

  override def getFunctionVersion: String = "1.0"

  override def getMemoryLimitInMB: Int = 512

  override def getClientContext: ClientContext = new FakeClientContext

  override def getLogStreamName: String = "logStream"

  override def getInvokedFunctionArn: String = "arn:aws:lambda:eu-west-1:name"

  override def getIdentity: CognitoIdentity = null

  override def getLogGroupName: String = "test logs"

  override def getAwsRequestId: String = "74B9CB83-FE41-4039-9142-BD5C1D27FC64"
}

class IntegrationTest extends FunSuite with MustMatchers with TestHelpers{
  test("integration test"){
    val data = slurpFile("src/test/scala/data/testevent.json")
    val sns = new SNS()
    sns.setMessage(data)

    val record = new SNSRecord()
    record.setSns(sns)

    val event = new SNSEvent()
    event.setRecords(List(record).asJava)

    val context = new FakeContext
    val l = new LaunchDetectorLambda

    assertThrows[RuntimeException] {
      //currently throws a "not implemented" exception
      l.handleRequest(event, context)
    }
  }
}
