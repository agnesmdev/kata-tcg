import helpers.LoggerHelper.logger
import models.{Game, Player}

import scala.util.Random

object GameApp {

  private var winner: Player = _

  def main(args: Array[String]): Unit = {
    val playerName_1 = args(0)
    val player_1 = Player(playerName_1, first = Random.nextInt % 2 == 0)

    val playerName_2 = args(1)
    val player_2 = Player(playerName_2, first = !player_1.first)

    val game = Game(player_1, player_2)
    winner = game.play()

    logger.info(s"${winner.coloredName} has won!")
  }

  def getWinner: Player = winner
}
