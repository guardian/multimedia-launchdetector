import scala.io.Source

trait TestHelpers {
  def slurpFile(filename:String):String = {
    val bufferedSource = Source.fromFile(filename)
    val rtn = bufferedSource.getLines().mkString("\n")
    bufferedSource.close()
    rtn
  }
}
