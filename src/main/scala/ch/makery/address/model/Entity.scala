package ch.makery.address.model

trait Entity extends Emotion {
  var isDead: Boolean = false
  var health: Double
  var attack: Double
  var defense: Double
  val critRate: Double = 0.1
  val critDamage: Double = 0.5
}
