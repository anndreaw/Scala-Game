package ch.makery.address.model.EnemyTypes

import ch.makery.address.model.EmotionTypes._
import ch.makery.address.model.Enemy

class ErrorPopUp extends Enemy with Sad{
  val name: String = "Error Pop Up"
  var health = 1000
  var attack = 200
  var defense = 100
}
