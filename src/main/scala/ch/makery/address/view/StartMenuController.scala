package ch.makery.address.view

import ch.makery.address.MainApp
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Button, TextField}
import scalafxml.core.macros.sfxml



@sfxml
class StartMenuController(
   private val startButton: Button,
   private val playerName: TextField
){
  // obtain from AddressApp
  def nullChecking(x: String) = x == null || x.length == 0

  def validateInput(): Boolean = {
    var errorMessage = ""

    if (nullChecking(playerName.text.value))
      errorMessage += "Not a valid name"

    if (errorMessage.length() == 0) {
      true
    } else {
      val alert = new Alert(Alert.AlertType.Error) { // Show error message
        initOwner(MainApp.stage)
        title = "Invalid Field"
        headerText = "Please type a valid name!"
        contentText = errorMessage
      }.showAndWait()
      false
    }
  }

  def startGame(action: ActionEvent): Unit = {
    if (validateInput() == true) {
      GameEngine.playerName = playerName.text.value
      MainApp.showBattleScene()
    }
  }

  def handleInstructions(): Unit ={
    MainApp.showInstructions()
  }


}




