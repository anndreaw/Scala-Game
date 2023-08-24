package ch.makery.address.model.EnemyTypes
import ch.makery.address.model.EmotionTypes._
import ch.makery.address.model.Enemy

class Graduation extends Enemy with Happy{
    val name: String = "Graduation"
    var health = 1000
    var attack = 200
    var defense = 100
}
