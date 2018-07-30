import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Gen.Parameters
import org.scalacheck.rng.Seed
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

  it should "generate distinct elements for a sealed trait" in {
    val testArb = implicitly[Arbitrary[Test]]
    val tests = (1 to 10).map{ _ =>
      testArb.arbitrary.pureApply(Gen.Parameters.default, Seed.random)
    }

    tests.distinct.length shouldBe 2
  }

  it should "generate a person" in {
    forAll { (person: Person) =>
      person.incrementAge shouldBe person.age +1
      person.greet shouldBe s"Hi, ${person.name}!"
    }
  }

  it should "generate distinct elements for a case class" in {
    val personArb = implicitly[Arbitrary[Person]]
    val people = (1 to 10).map{ _ =>
      personArb.arbitrary.pureApply(Gen.Parameters.default, Seed.random)
    }

    people.distinct.length shouldBe 10
  }

}

