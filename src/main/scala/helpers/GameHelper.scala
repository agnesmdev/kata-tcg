package helpers

import helpers.ColorHelper._
import helpers.LoggerHelper.logger
import models.{Action, Player}

import scala.collection.mutable

object GameHelper {

  private var actions = mutable.ArrayBuilder.make[Action]()
  private var states = mutable.ArrayBuilder.make[(Player, Player)]()

  def manageActions(player: Player, newActions: Action*): Player = {
    actions ++= newActions
    Player.takeActions(newActions, player)
  }

  def manageEndOfTurn(player_1: Player, player_2: Player): (Player, Player) = {
    val (firstPlayer, secondPlayer) = if (player_1.first) (player_1, player_2) else (player_2, player_1)
    states += ((firstPlayer, secondPlayer))

    logger.debug(s"${firstPlayer.coloredName}: ${firstPlayer.health.toString.info} HP - ${secondPlayer.coloredName}: ${secondPlayer.health.toString.info} HP")
    (player_1, player_2)
  }

  def resolveWinner(player_1: Player, player_2: Player): Option[Player] = {
    if (player_1.health <= 0) {
      Some(player_2)
    } else if (player_2.health <= 0) {
      Some(player_1)
    } else {
      None
    }
  }

  def getTurnActions(turn: Int): Seq[Action] = {
    actions.result().filter(_.turn == turn)
  }

  def getStateAtTurn(turn: Int): (Player, Player) = {
    states.result()(turn + 1)
  }

  def getFinalState: (Player, Player) = {
    states.result().last
  }
}
