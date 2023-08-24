package ch.makery.address.model

abstract class Attack{
  val name: String
  def effectiveMultiplier(entity: Entity): Double
}







