package models

import scala.util.Random

case class Deck(cards: Seq[Card]) {
  lazy val numberOfCards: Int = cards.length

  lazy val isEmpty: Boolean = cards.isEmpty

  def draw(number: Int): (Seq[Card], Deck) = {
    val randomCards = (1 to number).map(_ => Random.nextInt(cards.length))
    val newCards = randomCards.map(cards(_))
    (newCards, Deck(cards diff newCards))
  }
}

object Deck {
  def apply(): Deck = Deck(initDeck())

  private def initDeck(): Seq[Card] = Seq(0, 0, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8).map(DamageCard)
}
