package ch.makery.address.view

import ch.makery.address.MainApp
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class GameEndController (
     private val bgImage: ImageView,
     private val noteLabel: Label,
     private val tryAgainButton: Button
                        ){

  if (GameEngine.winner == "Enemy"){
    showGameOver()
    noteLabel.text = "Not so lucky..."
  } else{
    showVictory()
    noteLabel.text = s"${GameEngine.playerName} has won!"
  }

  def handleTryAgain(action: ActionEvent): Unit = {
    MainApp.showStartMenu()
  }

  def showVictory(): Unit = {
    bgImage.image = new Image(getClass.getResourceAsStream("/ch/makery/address/images/Game_Win.png"))
  }
  def showGameOver(): Unit = {
    bgImage.image = new Image(getClass.getResourceAsStream("/ch/makery/address/images/Game_Over.png"))
  }

}
