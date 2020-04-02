package models

import helpers.ColorHelper._
import helpers.GameHelper._
import helpers.LoggerHelper.logger

case class Turn(turn: Int, activePlayer: Player, passivePlayer: Player) {

  def play: (Player, Player) = {
    logger.debug(s"Turn $turn: Player ${activePlayer.coloredName} plays")
    val readyPlayer = begin

    logger.debug(s"${activePlayer.coloredName} has ${activePlayer.mana.number.toString.info} mana and ${activePlayer.hand.numberOfCards.toString.info} cards (${activePlayer.hand.overview})")
    val (player_1, player_2) = playCards(readyPlayer, passivePlayer, activePlayer.hand.sortByCost)
    manageEndOfTurn(player_1, player_2)
  }

  def begin: Player = {
    logger.debug(s"${activePlayer.coloredName} receives 1 mana and draw 1 card")

    if (activePlayer.deck.isEmpty) {
      logger.debug(s"... but ${activePlayer.coloredName}'s deck ${"is empty".info}")
      manageActions(activePlayer, ReceiveMana(turn), RefillMana(turn))
    } else {
      manageActions(activePlayer, ReceiveMana(turn), RefillMana(turn), DrawCards(1, turn))
    }
  }

  @scala.annotation.tailrec
  private def playCards(player: Player, opponent: Player, cards: Seq[Card]): (Player, Player) = {
    cards match {
      case Nil =>
        logger.debug(s"${player.coloredName} can play no more card this turn")
        (manageActions(player, NoPlayCard(turn)), opponent)
      case card :: otherCards if !player.canPlay(card) => playCards(player, opponent, otherCards)
      case card :: otherCards =>
        logger.debug(s"${player.coloredName} uses ${card.cost} mana to damage player ${opponent.coloredName}")
        logger.debug(s"${opponent.coloredName} has now ${(opponent.health - card.cost).toString.damage} HP")

        val (player_1, player_2) = (manageActions(player, PlayCard(card, turn)), manageActions(opponent, TakeDamage(card.cost, turn)))
        resolveWinner(player_1, player_2) match {
          case None => playCards(player_1, player_2, otherCards)
          case Some(_) => (player_1, player_2)
        }
    }
  }
}
