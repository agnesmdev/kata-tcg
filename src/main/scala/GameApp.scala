import models.Player

object GameApp {

  private val transition = "------------------------------\n"

  private var player_1: Player = _
  private var player_2: Player = _

  def main(args: Array[String]): Unit = {
    val playerName_1 = args(0)
    player_1 = Player(playerName_1)

    val playerName_2 = args(1)
    player_2 = Player(playerName_2, !player_1.first)

    print(transition)

    val players = Seq(player_1, player_2).sortBy(_.first).reverse
    println(s"First player is ${players.head.coloredName}")
    println(s"Second player is ${players.last.coloredName}")

    print(transition)
    val readyPlayers = players.map(initDeck)

    print(transition)
    playTurns(readyPlayers)
  }

  def getFirstPlayer: Player = if (player_1.first) player_1 else player_2

  def getSecondPlayer: Player = if (!player_1.first) player_1 else player_2

  private def initDeck(player: Player): Player = {
    val number = if (player.first) 3 else 4
    println(s"Player ${player.coloredName} draws $number cards")

    player.draw(number)
  }

  private def playTurns(players: Seq[Player], number: Int = 0): Option[Player] = {
    println("Soon :)")
    None
  }
}
