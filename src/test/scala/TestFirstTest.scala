import org.scalatest.{FunSuite, MustMatchers}
import java.io.{ByteArrayInputStream, InputStream, OutputStreamWriter}

class TestFirstTest extends FunSuite with MustMatchers {
  test("does it run"){
    val l = new FirstTestLambda
    val testdata = """{"key1": "value1"}"""

    l.handleRequest(new ByteArrayInputStream(testdata.toCharArray.map(_.toByte)), null,null)
  }
}
