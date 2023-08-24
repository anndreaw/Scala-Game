package ch.makery.address.model.EnemyTypes

import ch.makery.address.model.EmotionTypes._
import ch.makery.address.model.Enemy

class NoWifi extends Enemy with Sad{
  val name: String = "No Wifi Dinosaur"
  var health = 1000
  var attack = 200
  var defense = 100
}
