import magnolia.{CaseClass, Magnolia, Param, SealedTrait}
import org.scalacheck.Gen.Parameters
import org.scalacheck.rng.Seed
import org.scalacheck.{Arbitrary, Gen}
import scala.language.experimental.macros

trait ArbDerivation {
  /** Parameters to configure scalacheck generators */
  implicit def parameters: Parameters

  /** binds the Magnolia macro to this derivation object */
  implicit def gen[T]: Arbitrary[T] = macro Magnolia.gen[T]
  /** type constructor for new instances of the typeclass */
  type Typeclass[T] = Arbitrary[T]

  /** defines how new Arbitrary typeclasses for nested case classes should be constructed */
  def combine[T](ctx: CaseClass[Arbitrary, T]): Arbitrary[T] = {
     val t: T = ctx.construct { param: Param[Typeclass, T] =>
      param
        .typeclass
        .arbitrary
        .pureApply(parameters, Seed.random())
    }
    Arbitrary(Gen.delay(t))
  }

  /** Given a sealed trait, picks a random Arbitrary instance for one of the elements*/
  def dispatch[T](ctx: SealedTrait[Arbitrary, T]): Arbitrary[T] = {
    import scala.util.Random
    /** Pick a random instance of the sealed trait to summon an arbitrary for */
    val elemToUse = Random.shuffle(ctx.subtypes).head
    val arb: Typeclass[elemToUse.SType] = elemToUse.typeclass
    Arbitrary(arb.arbitrary)
  }
}
