import org.scalatest.{DiagrammedAssertions, FlatSpec}
import javacpp4s.DynamicLibraryMeta

class UseDynamicLibraryMetaSpec extends FlatSpec with DiagrammedAssertions {
  "UseDynamicLibraryMeta" should "return the build.sbt parameters" in {
    val actual = UseDynamicLibraryMeta.get

    assert(actual._1 == DynamicLibraryMeta.Linux)
    assert(actual._2 == "libTest")
  }
}