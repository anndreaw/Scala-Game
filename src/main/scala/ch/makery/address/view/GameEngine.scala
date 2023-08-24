package ch.makery.address.view

import ch.makery.address.MainApp
import ch.makery.address.model.EnemyTypes._
import ch.makery.address.model.Enemy

import scala.collection.mutable.ListBuffer

object GameEngine {
  var playerName: String = "???"
  var winner: String = ""
  val enemyTypes = List(new Cat, new Graduation, new ErrorPopUp, new NoWifi, new RetroComputer, new IceCreamContainer)

  def spawnEnemies(entities: List[Enemy]): ListBuffer[Enemy] = {
    val rand = new scala.util.Random
    var indexList = List[Int]()
    for (x <- 1 to 2){ // Randomly pick two enemies
      val index = rand.nextInt(entities.length)
      indexList = indexList :+ index
    }
    val chosenEntity = ListBuffer[Enemy]()
    for(xs <- indexList){
      val enemy: Enemy = xs match {  // Create instances of enemies
        case 0 => new Cat
        case 1 => new Graduation
        case 2 => new ErrorPopUp
        case 3 => new NoWifi
        case 4 => new RetroComputer
        case 5 => new IceCreamContainer
        case _ => throw new IllegalArgumentException("No valid enemy")
      }
      chosenEntity += enemy
    }
    chosenEntity
  }

  def gameOver(): Unit = {
    MainApp.showGameEnd()
  }

}
