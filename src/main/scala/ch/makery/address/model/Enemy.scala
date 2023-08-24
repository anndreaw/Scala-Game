package ch.makery.address.model

abstract class Enemy extends Entity{
  val name: String

  def dealDamage(target: Player): (Double, Boolean) = {
    var crit: Boolean = false

    // Calculate raw damage base on target's defense
    // in courtesy of https://rpg.fandom.com/wiki/Damage_Formula
    val rawDamage = attack * (100 / (100 + target.defense))
    var finalDamage = rawDamage

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
}
