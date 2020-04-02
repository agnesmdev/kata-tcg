package models

import helpers.GameHelper._
import helpers.LoggerHelper.logger

case class Game(player_1: Player, player_2: Player) {

  def play(): Player = {
    initGame(player_1, player_2)

    val (firstPlayer, secondPlayer) = if (player_1.first) (player_1, player_2) else (player_2, player_1)
    val (firstPlayerInit, secondPlayerInit) = (initHand(firstPlayer), initHand(secondPlayer))

    logger.debug(s"First player is ${firstPlayerInit.coloredName}")
    logger.debug(s"Second player is ${secondPlayerInit.coloredName}")
    playTurns(firstPlayerInit, secondPlayerInit)
  }

  @scala.annotation.tailrec
  private def playTurns(activePlayer: Player, passivePlayer: Player, number: Int = 1): Player = {
    val turn = Turn(number, activePlayer, passivePlayer)
    val (player_1, player_2) = turn.play

    resolveWinner(player_1, player_2) match {
      case None => playTurns(player_2, player_1, number + 1)
      case Some(winner) => winner
    }
  }

  private def initGame(firstPlayer: Player, secondPlayer: Player): (Player, Player) = {
    manageEndOfTurn(firstPlayer, secondPlayer)
  }

  private def initHand(player: Player): Player = {
    val number = if (player.first) 3 else 4
    manageActions(player, DrawCards(number, 0))
  }
}
