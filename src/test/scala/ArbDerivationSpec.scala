import org.scalacheck.Gen
import org.scalacheck.Gen.Parameters
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class ArbDerivationSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks with ArbDerivation {

  override implicit def parameters: Gen.Parameters = Parameters.default

  case class Foo(thing: String)
  case class Person(age: Int, name: String, foo: Foo){
    def incrementAge = age + 1
    def greet = s"Hi, $name!"
  }

  sealed trait Test

  case object Bar extends Test
  case object Baz extends Test


  it should "generate a sealed trait" in {
    forAll{ (test: Test) =>
      Seq(Bar, Baz) should contain (test)
    }
  }

  it should "generate a person" in {
    forAll { (person: Person) =>
      person.incrementAge shouldBe person.age +1
      person.greet shouldBe s"Hi, ${person.name}!"
    }
  }

}

