package ch.makery.address.model.EnemyTypes

import ch.makery.address.model.EmotionTypes._
import ch.makery.address.model.Enemy

class RetroComputer extends Enemy with Angry {
  val name: String = "Retro Computer"
  var health = 1000
  var attack = 200
  var defense = 100
}
