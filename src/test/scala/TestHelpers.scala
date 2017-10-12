import scala.io.Source

/* helper functions for tests that can be mixed in */
trait TestHelpers {
  def slurpFile(filename:String):String = {
    val bufferedSource = Source.fromFile(filename)
    val rtn = bufferedSource.getLines().mkString("\n")
    bufferedSource.close()
    rtn
  }
}
