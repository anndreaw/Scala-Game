package ch.makery.address.model.AttackTypes

import ch.makery.address.model.{Attack, Entity}

case object Smash extends Attack{
  val name: String = "Smash"
  def effectiveMultiplier(entity: Entity): Double =
    entity.emotionType match{
      case "Sad" => 2.0 // Effective against
      case "Happy" => 0.5 // Weak against
      case _ => 1.0 // Normal
    }
}
