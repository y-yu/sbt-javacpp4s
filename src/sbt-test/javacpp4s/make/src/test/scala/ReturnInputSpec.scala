import org.scalatest.{DiagrammedAssertions, FlatSpec}
import returninput.ReturnInput

class ReturnInputSpec extends FlatSpec with DiagrammedAssertions {
  "ReturnInput" should "return the input number" in {
    val sut = new ReturnInput

    assert(sut.id(5) == 5)
  }
}
