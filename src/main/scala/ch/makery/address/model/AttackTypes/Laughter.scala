package ch.makery.address.model.AttackTypes

import ch.makery.address.model.{Attack, Entity}

case object Laughter extends Attack{
  val name: String = "Laughter"
  def effectiveMultiplier(entity: Entity): Double =
    entity.emotionType match{
      case "Angry" => 2.0 // Effective against
      case "Sad" => 0.5 // Weak against
      case _ => 1.0 // Normal
    }
}