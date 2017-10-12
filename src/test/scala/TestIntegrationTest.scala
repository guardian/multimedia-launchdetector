import java.util.Base64
import com.gu.crier.model.event.v1._
import org.scalatest.{FunSuite, MustMatchers}
import com.amazonaws.services.lambda.runtime._
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.lambda.runtime.events.SNSEvent.{SNS, SNSRecord}
import scala.collection.JavaConverters._
import java.util

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

  override def getRemainingTimeInMillis: Int = 10000

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


class TestIntegrationTest extends FunSuite with MustMatchers with TestHelpers {
  test("integration test"){
    val snsRecord = wrapSns(genBinaryEvent)
    val snsEvent = new SNSEvent
    snsEvent.setRecords(List(snsRecord).asJava)

    val l = new LaunchDetectorLambda
    val c = new FakeContext
    l.handleRequest(snsEvent, c)
  }
}
