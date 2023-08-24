package ch.makery.address
import ch.makery.address.view.GameEndController
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.scene.image.Image
import scalafx.scene.layout.BorderPane
import scalafx.stage.{Modality, Stage}

object MainApp extends JFXApp{
  // In courtesy of AddressApp Tutorial by Dr Chin PRG2104 OOP
  val rootResource = getClass.getResource("view/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load()

  val roots = loader.getRoot[jfxs.layout.BorderPane]

  // Initialise stage
  stage = new PrimaryStage{
    title = "Emotion"
    scene = new Scene {
      root = roots: BorderPane
    }
    icons += new Image(getClass.getResourceAsStream("/ch/makery/address/images/Emotion_Logo.png"))
  }

  def showStartMenu()={
    val resource = getClass.getResource("view/StartMenu.fxml")
    val loader = new FXMLLoader(resource,NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }
  def showBattleScene(): Unit ={
    val resource = getClass.getResource("view/BattleScene.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }
  def showInstructions(): Unit = {
    val resource = getClass.getResource("view/Instructions.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }
  def showGameEnd(): Unit = {
    val resource = getClass.getResource("view/GameEnd.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }
  showStartMenu()
}

