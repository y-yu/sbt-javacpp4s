package javacpp

import javacpp.sbt.HelloWorld

object HelloWorld {
  def main(args: Array[String]): Unit = {
    val instance = new HelloWorld()
    instance.printN(5)
  }
}
