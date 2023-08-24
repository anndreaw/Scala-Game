package ch.makery.address.view

import ch.makery.address.MainApp
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.collections.{FXCollections, ListChangeListener, ObservableList}
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label, ListView, TableColumn, TableView}
import scalafx.scene.image
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
//import javafx.scene.image

import scala.collection.mutable

@sfxml
class InstructionsController(
    private val returnButton: Button,
    private val tutorialDescription: Label,
    private val tutorialImage: ImageView,
    private val title1: Label,
    private val title2: Label,
    private val title3: Label,
    private val title4: Label,
                            ) {

  title1.setText("Welcome")
  title2.setText("Emotion")
  title3.setText("Attack types")
  title4.setText("Special attack")
  def handleReturn(action: ActionEvent): Unit = {
    MainApp.showStartMenu()
  }

  def selectTitle1(): Unit = {
    tutorialImage.setImage(new image.Image(getClass.getResourceAsStream("/ch/makery/address/images/Tutorial1.png")))
    tutorialDescription.setText("This is a simple 1v2 turn base game made for fun. The aim of the game is to not die! " +
                                "Player can select either one enemies to attack and both enemies will attack player.")
  }

  def selectTitle2(): Unit = {
    tutorialImage.setImage(new image.Image(getClass.getResourceAsStream("/ch/makery/address/images/Tutorial2.png")))
    tutorialDescription.setText("The game has three types of emotions, Happy, Sad and Angry. " +
                                "\nFind the appropriate attack for each enemy as each are effective and weak against one another.")
  }

  def selectTitle3(): Unit = {
    tutorialImage.setImage(new image.Image(getClass.getResourceAsStream("/ch/makery/address/images/Tutorial3.png")))
    tutorialDescription.setText("Player can perform three attacks." +
                                "\n1. Laughter : Happy Attack, increases Defense of player" +
                                "\n2. Cry : Sad Attack, increase Health of player" +
                                "\n3. Smash : Angry Attack, increases Attack of player"
                                  )
  }

  def selectTitle4(): Unit = {
    tutorialImage.setImage(new image.Image(getClass.getResourceAsStream("/ch/makery/address/images/Tutorial4.png")))
    tutorialDescription.setText("Every attack that is effective will increase the emotion meter." +
                                "Once emotion meter reaches full, player is allowed to do an Explode attack." +
                                "Explode attack is an AOE Attack that deals 4x damage to both enemies")
  }

}
