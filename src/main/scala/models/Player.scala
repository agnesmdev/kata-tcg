package models

import helpers.ColorHelper._

case class Player(name: String,
                  health: Int = 30,
                  mana: Mana = Mana(),
                  deck: Deck = Deck(),
                  hand: Hand = Hand(),
                  bin: Bin = Bin(),
                  first: Boolean) {
  lazy val coloredName: String = if (first) name.firstPlayer else name.secondPlayer

  def canPlay(card: Card): Boolean = mana.available >= card.cost

  protected def draw(number: Int): Player = {
    val (cards, newDeck) = deck.draw(number)
    Player(name, health, mana, newDeck, hand.draw(cards), bin, first)
  }

  protected def receiveMana: Player = {
    Player(name, health, mana.receiveMana, deck, hand, bin, first)
  }

  protected def refillMana: Player = {
    Player(name, health, mana.refillMana, deck, hand, bin, first)
  }

  protected def playCard(card: Card): Player = {
    Player(name, health, mana.useCard(card), deck, hand.useCard(card), bin.useCard(card), first)
  }

  protected def takeDamage(damage: Int): Player = {
    Player(name, health - damage, mana, deck, hand, bin, first)
  }
}

object Player {
  @scala.annotation.tailrec
  def takeActions(actions: Seq[Action], player: Player): Player = actions.headOption match {
    case None => player
    case Some(DrawCards(number, _)) => takeActions(actions.tail, player.draw(number))
    case Some(ReceiveMana(_)) => takeActions(actions.tail, player.receiveMana)
    case Some(RefillMana(_)) => takeActions(actions.tail, player.refillMana)
    case Some(PlayCard(card, _)) => takeActions(actions.tail, player.playCard(card))
    case Some(TakeDamage(damage, _)) => takeActions(actions.tail, player.takeDamage(damage))
    case Some(_) => takeActions(actions.tail, player)
  }
}
