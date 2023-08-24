package ch.makery.address.model.AttackTypes

import ch.makery.address.model.{Attack, Entity}

case object Cry extends Attack{
  val name: String = "Cry"
  def effectiveMultiplier(entity: Entity): Double =
    entity.emotionType match{
      case "Happy" => 2.0 // Effective against
      case "Angry" => 0.5 // Weak against
      case _ => 1.0 // Normal
    }
}