package ch.makery.address.model.EnemyTypes

import ch.makery.address.model.EmotionTypes._
import ch.makery.address.model.Enemy

class IceCreamContainer extends Enemy with Happy{
  val name: String = "Totally Real Ice Cream"
  var health = 1000
  var attack = 200
  var defense = 100
}
