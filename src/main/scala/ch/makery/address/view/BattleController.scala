package ch.makery.address.view

import ch.makery.address.MainApp
import ch.makery.address.model.AttackTypes._
import ch.makery.address.model.{Attack, Enemy, Entity, Player}
import scalafx.Includes.at
import scalafx.animation.{PauseTransition, SequentialTransition, TranslateTransition}
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label, ProgressBar, Tooltip}
import scalafx.scene.effect.DropShadow
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color
import scalafx.util.Duration
import scalafxml.core.macros.sfxml
import scala.collection.mutable.ListBuffer

@sfxml
class BattleController(
  private val playerNameLabel: Label,
  private val playerHealthBar: ProgressBar,
  private val playerImage: ImageView,
  private val playerDmgTakenLabel: Label,
  private val enemy1NameLabel: Label,
  private val enemy1HealthBar: ProgressBar,
  private val enemy1MaxHpLabel: Label,
  private val enemy1Image: ImageView,
  private val enemy1DmgTakenLabel: Label,
  private val enemy2NameLabel: Label,
  private val enemy2HealthBar: ProgressBar,
  private val enemy2MaxHpLabel: Label,
  private val enemy2Image: ImageView,
  private val enemy2DmgTakenLabel: Label,
  private val consoleLabel: Label,
  private val attack1Button: Button,
  private val attack2Button: Button,
  private val attack3Button: Button,
  private val quitButton: Button,
  private val emotionMeter: ProgressBar,
  private val explodeButton: Button,
  private val select1: ImageView,
  private val select2: ImageView
                            ) {

  // Initialisation
  var enemySelected: Boolean = true // True for enemy 1, False for enemy 2
  var playerTurn: Boolean = true // True for player, False for enemies

  // Spawn entities and initialise
  val player = new Player(GameEngine.playerName)
  var enemyList = GameEngine.spawnEnemies(GameEngine.enemyTypes)
  val enemy1 = enemyList(0)
  val enemy2 = enemyList(1)
  val playerMaxHp = player.health
  val enemy1MaxHp = enemy1.health
  val enemy2MaxHp = enemy2.health

  // Initalise game GUI
  enemy1MaxHpLabel.text = (enemy1MaxHp.toInt).toString
  enemy2MaxHpLabel.text = (enemy2MaxHp.toInt).toString
  enemy1NameLabel.text = enemy1.name
  enemy2NameLabel.text = enemy2.name
  playerNameLabel.text = GameEngine.playerName
  playerDmgTakenLabel.setVisible(false)
  enemy1DmgTakenLabel.setVisible(false)
  enemy2DmgTakenLabel.setVisible(false) // Hide it
  select2.setVisible(false)
  explodeButton.setDisable(true) // Disable button
  enemy1Image.image = new Image(getClass.getResourceAsStream("/ch/makery/address/images/Enemy/"+ enemy1.name +".png"))
  enemy2Image.image = new Image(getClass.getResourceAsStream("/ch/makery/address/images/Enemy/"+ enemy2.name +".png"))
  consoleLabel.setText(s"${GameEngine.playerName} has joined the battle.")
  val entityList = List(player, enemy1, enemy2)
  entityList.foreach {
    e =>
      updateHealthBar(e)
      applyEmotion(e)
  }
  attack1Button.setStyle("-fx-background-color: #FF5733;")

  // Applies colour highlight around entity based on emotion
  def applyEmotion(entity: Entity): Unit = {
    entity.emotionType match {
      case "Angry" => applyEmotionEffect(entity, Color.rgb(255, 0, 0))
      case "Sad" => applyEmotionEffect(entity, Color.rgb(0, 162, 232))
      case "Happy" => applyEmotionEffect(entity, Color.rgb(255, 201, 14))
      case "None" =>
      case _ => throw new IllegalArgumentException("Invalid")
    }
  }

  def applyEmotionEffect(entity: Entity, color: Color): Unit = {
    val dropShadow = new DropShadow()
    dropShadow.setSpread(0.75)
      entity match {
        case a if a == enemy1 => {
          dropShadow.setColor(color)
          enemy1Image.setEffect(dropShadow)
        }
        case a if a == enemy2 => {
          dropShadow.setColor(color)
          enemy2Image.setEffect(dropShadow)
        }
        case a if a == player => {
          dropShadow.setColor(color)
          playerImage.setEffect(dropShadow)
        }
        case _ => throw new IllegalArgumentException("No valid entity")
      }
  }

  def quit(action: ActionEvent): Unit = { // quit from battle
    MainApp.showStartMenu()
  }

  def selectEnemy1(): Unit = {
    select1.setVisible(true)
    if (enemySelected == false){
      enemySelected = true
      select2.setVisible(false)
    }
  }

  def selectEnemy2(): Unit = {
    select2.setVisible(true)
    if (enemySelected == true) {
      enemySelected = false
      select1.setVisible(false)
    }
  }

  def attackMove1(action: ActionEvent): Unit = {
    performPlayerTurn(Laughter)
  }

  def attackMove2(actionEvent: ActionEvent): Unit = {
    performPlayerTurn(Cry)
  }

  def attackMove3(actionEvent: ActionEvent): Unit = {
    performPlayerTurn(Smash)
  }

  def explodeMove(actionEvent: ActionEvent): Unit = {
    val damage = player.dealExplodeDamage(enemyList)
    for (e <- enemyList) {
      if (e.isDead != true){
        updateHealthBar(e)
        isEnemyDead(e, damage, false)
        playerTurn = false // Player has used their turn
        playAnimations()
        playEnemyTurn()
      }
    }
    updateConsole("\nPlayer exploded from emotions.", true)
    updateEmotionMeter()
    explodeButton.setDisable(true)
  }

  // Calculations and controls when player's turn and attacks
  def performPlayerTurn(attackType: Attack): Unit = {
    if (enemySelected == true) { // enemy1 selected
      var (damage, crit) = player.dealDamage(attackType, enemy1)
      enemy1.health -= damage
      isEnemyDead(enemy1, damage, crit)
    } else if (enemySelected == false) { //enemy2 selected
      var (damage, crit) = player.dealDamage(attackType, enemy2)
      enemy2.health -= damage
      isEnemyDead(enemy2, damage, crit)
    } else {
      updateConsole("No enemy selected!", false)
    }

    if (player.checkEmotionMeter() == true){ // Only when emotion meter is full
      explodeButton.setDisable(false)
    }
    player.applyBuff(attackType)
    // GUI Updates
    applyEmotion(player) // Change player emotion visually
    updateEmotionMeter() // Add to emotion bar
    playerTurn = false // Player has used their turn
    playAnimations()
    // Play enemy's turns
    playEnemyTurn()
  }

  def performEnemyTurn(): Unit = {
    if (playerTurn == false) { // False = enemy's turn
      for (es <- enemyList) { // Loop through each enemy
        if (es.isDead == false) {
          var (damage, crit) = es.dealDamage(player)
          player.health -= damage
          isPlayerDead(player, es, damage, crit)
        } else {
          enemyList -= es // Remove dead enemy from list
        }
      }
    }
    // Checks if all enemy is dead, if yes exits game
    if (enemyList.isEmpty) {
      GameEngine.winner = GameEngine.playerName
      GameEngine.gameOver()
    }
    playerTurn = true // Player's turn
  }

  def updateConsole(prompt: String, add: Boolean): Unit = {
    if (add == true){
      consoleLabel.setText(consoleLabel.getText() + prompt)
    } else{
      consoleLabel.setText(prompt)
    }
  }

  def updateEmotionMeter(): Unit = {
    emotionMeter.setProgress(player.emotionMeter / 3.0)
  }

  // Update the health bar
  // progress bar basics learnt from https://youtu.be/nEKQjAP0lrQ
  def updateHealthBar(entity: Entity): Unit = {
    var healthPercent: Double = 0.0
    var color: String = "-fx-accent: green;"
    entity match {
      case a if a == enemy1 => {
        healthPercent = enemy1.health/enemy1MaxHp
        enemy1HealthBar.setProgress(healthPercent)
        color = updateHealthBarColours(healthPercent)
        enemy1HealthBar.setStyle(color)
      }
      case a if a == enemy2 => {
        healthPercent = enemy2.health/enemy2MaxHp
        enemy2HealthBar.setProgress(healthPercent)
        color = updateHealthBarColours(healthPercent)
        enemy2HealthBar.setStyle(color)
      }
      case a if a == player => {
        healthPercent = player.health/playerMaxHp
        playerHealthBar.setProgress(healthPercent)
        color = updateHealthBarColours(healthPercent)
        playerHealthBar.setStyle(color)
      }
      case _ => throw new IllegalArgumentException("No valid entity")
    }
  }

  def updateHealthBarColours(healthPercent: Double): String  = {
    val color = healthPercent match {
      case a if a > 0.5 => "-fx-accent: green;"
      case a if a > 0.3 => "-fx-accent: orange;"
      case a if a >= 0.0 => "-fx-accent: red;"
      case _ => "-fx-accent: red;"
    }
    color
  }

  def updateDmgTakenLabel(entity: Entity, dmg: Double, crit: Boolean): Unit = {
    entity match{
      case a if a == enemy1 => {
        enemy1DmgTakenLabel.setVisible(true)
        enemy1DmgTakenLabel.text = (dmg.toInt).toString
        if (crit == true) {
          enemy1DmgTakenLabel.setText(enemy1DmgTakenLabel.getText() + " CRIT!!")
        }
      }
      case a if a == enemy2 => {
        enemy2DmgTakenLabel.setVisible(true)
        enemy2DmgTakenLabel.text = (dmg.toInt).toString
        if (crit == true) {
          enemy2DmgTakenLabel.setText(enemy2DmgTakenLabel.getText() + " CRIT!!")
        }
      }
      case a if a == player => {
        playerDmgTakenLabel.setVisible(true)
        playerDmgTakenLabel.text = (dmg.toInt).toString
        if (crit == true) {
          playerDmgTakenLabel.setText(playerDmgTakenLabel.getText() + " CRIT!!")
        }
      }
      case _ => throw new IllegalArgumentException("No valid entity")
    }
  }

  def isEnemyDead(enemy: Enemy, damage: Double, crit: Boolean): Unit = {
    if (enemy.health <= 0) { // Check if enemy is dead
      enemy.isDead = true
      enemy match{
        case e if e == enemy1 =>
          updateConsole(s"\n${enemy.name} is dead.", true)
          enemy1Image.setVisible(false)
          enemy1HealthBar.setProgress(0)
          enemy1DmgTakenLabel.setVisible(false)
          enemySelected = false
          select1.setVisible(false)
          select2.setVisible(true)

        case e if e == enemy2 =>
          updateConsole(s"\n${enemy.name} is dead", true)
          enemy2Image.setVisible(false)
          enemy2HealthBar.setProgress(0)
          enemy2DmgTakenLabel.setVisible(false)
          enemySelected = true
          select1.setVisible(true)
          select2.setVisible(false)
      }
    }
    // If alive update GUI
    else if (enemy.health > 0){
      updateConsole(s"Player did ${damage.toInt} to ${enemy.name}! ",false)
      updateHealthBar(enemy)
      updateDmgTakenLabel(enemy, damage, crit)
    }
  }

  def isPlayerDead(player: Player, enemy: Enemy, damage: Double, crit: Boolean): Unit = {
    if (player.health <= 0) { // Check if player is dead
      player.isDead = true
      GameEngine.winner = "Enemy"
      GameEngine.gameOver()
    }
    // If alive update GUI
    else if (player.health > 0){
      updateHealthBar(player)
      updateDmgTakenLabel(player, damage, crit)
      updateConsole(s"\n${enemy.name} dealt ${damage.toInt} to player! ", true)
    }
  }

  // Animations and delays for player and enemy
  val playerAtkAnimation = new TranslateTransition(Duration(150), playerImage) {
    byX = 150 //until where
    cycleCount = 2
    autoReverse = true
  }

  val enemy1AtkAnimation = new TranslateTransition(Duration(150), enemy1Image) {
    byX = -150 //until where
    byY = 20
    cycleCount = 2
    autoReverse = true
  }

  val enemy2AtkAnimation = new TranslateTransition(Duration(150), enemy2Image) {
    byX = -150 //until where
    byY = 20
    cycleCount = 2
    autoReverse = true
  }

  def playAnimations(): Unit = {
    playerImage.image = new Image(getClass.getResourceAsStream("/ch/makery/address/images/Player_Attack.png"))
    val seqTransition = new SequentialTransition {
      children = Seq(
        playerAtkAnimation,
        new PauseTransition(Duration.apply(500)),
        enemy1AtkAnimation,
        new PauseTransition(Duration.apply(500)),
        enemy2AtkAnimation
      )
    }
    seqTransition.play()
  }

  // Takes care of player not able to attack when its enemy turn
  def playEnemyTurn(): Unit = {
    attack1Button.setDisable(true)
    attack2Button.setDisable(true)
    attack3Button.setDisable(true)
    // Delay action
    // in courtesy of https://stackoverflow.com/questions/61012205/use-of-thread-sleep-in-javafx-application
    val pause: PauseTransition = new PauseTransition(Duration.apply(2000))
    pause.onFinished = _ => {
      performEnemyTurn()
      attack1Button.setDisable(false)
      attack2Button.setDisable(false)
      attack3Button.setDisable(false)
      playerImage.image = new Image(getClass.getResourceAsStream("/ch/makery/address/images/Player.png"))
    }
    pause.play()
  }





}
