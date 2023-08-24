package ch.makery.address.model.EnemyTypes

import ch.makery.address.model.EmotionTypes._
import ch.makery.address.model.Enemy

class Cat extends Enemy with Angry{
  val name: String = "Cat"
  var health = 1000
  var attack = 200
  var defense = 100
}
