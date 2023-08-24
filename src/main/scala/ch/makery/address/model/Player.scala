package ch.makery.address.model

import ch.makery.address.model.AttackTypes._

import scala.collection.mutable.ListBuffer

class Player(val name: String) extends Entity {
  var health: Double = 1000
  var attack: Double = 200
  var defense: Double = 100
  override val critRate: Double = 0.5 // 50% crit rate
  override val critDamage: Double = 1.2 // 120% crit damage
  var emotionMeter: Double = 0
  var isExplode: Boolean = false // True means emotion meter is full

  def dealDamage(attackType : Attack, target: Enemy): (Double, Boolean) = {
    val multiplier = attackType.effectiveMultiplier(target)
    var crit: Boolean = false

    // For every effective hit the player emotionMeter increases and increase emotion meter
    if (multiplier == 2.0){
      emotionMeter += 1
    }

    // Calculate raw damage base on target's defense
    // in courtesy of https://rpg.fandom.com/wiki/Damage_Formula
    val rawDamage = attack * (100 / (100 + target.defense))
    var finalDamage = rawDamage * multiplier

    // Calculate Crit Hit
    val critChance = (Math.random())
    // Convert to 2 decimal points
    // in courtesy of https://stackoverflow.com/questions/11106886/scala-doubles-and-precision
    val critChance2dp = BigDecimal(critChance).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    if (critChance2dp < critRate) {
      finalDamage = finalDamage * (1 + critDamage)
      crit = true
    }
    (finalDamage, crit)
  }

  def dealExplodeDamage(enemies: ListBuffer[Enemy]): Double = {
    var damage: Double = 0.0
    enemies.foreach { es =>
      if (es.isDead != true) {
        val explodeAtk = attack * 4.0 // Explode does 4x the damage of a normal attack
        damage = explodeAtk * (100 / (100 + es.defense)) // AOE Damage
        es.health -= damage
      }
    }
    // Reset
    emotionMeter = 0
    isExplode = false
    damage
  }

  // Function to check if emotion meter is full (maximum is 3 points)
  def checkEmotionMeter(): Boolean = {
    if (emotionMeter >= 3) {
      isExplode = true
      emotionMeter = 3
    }
    isExplode
  }

  def applyBuff(attackType: Attack): Unit = {
    attackType match {
      case Laughter => {
        defense *= 1.10 // Increases 10% of defense
        emotionType = "Happy"
      }
      case Cry => {
        health += 200 // Heals 200 Hp
        emotionType = "Sad"
      }
      case Smash => {
        attack *= 1.10 // Increases 10% of attack
        emotionType = "Angry"
      }
      case _ =>
    }
  }




}
