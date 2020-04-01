package models

import scala.util.Random
import helpers.ColorHelper._

case class Player(name: String,
                  health: Int,
                  mana: Int,
                  deck: Deck,
                  hand: Seq[Card],
                  first: Boolean) {
  lazy val coloredName: String = if (first) name.firstPlayer else name.secondPlayer

  def draw(number: Int): Player = {
    val (cards, newDeck) = deck.draw(number)
    Player(name, health, mana, newDeck, hand ++ cards, first)
  }
}

case class Deck(cards: Seq[Card]) {
  def draw(number: Int): (Seq[Card], Deck) = (cards.take(number), Deck(cards.drop(number)))
}

object Player {
  def apply(name: String, first: Boolean = Random.nextInt % 2 == 0): Player = Player(name, 30, 0, Deck(), Nil, first)
}

object Deck {
  def apply(): Deck = Deck(initDeck())

  private def initDeck(): Seq[Card] = Random.shuffle(Seq(0, 0, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8).map(DamageCard))
}
