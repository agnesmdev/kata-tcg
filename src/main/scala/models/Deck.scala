package models

import scala.util.Random

case class Deck(cards: Seq[Card]) {
  lazy val numberOfCards: Int = cards.length

  lazy val isEmpty: Boolean = cards.isEmpty

  def draw(number: Int): (Seq[Card], Deck) = {
    val drawnCards = randomCards(cards, number)
    (drawnCards, Deck(cards diff drawnCards))
  }

  @scala.annotation.tailrec
  private def randomCards(cards: Seq[Card], number: Int, result: Seq[Card] = Nil): Seq[Card] = number match {
    case 0 => result
    case _ =>
      val card = cards(Random.nextInt(cards.length))
      randomCards(cards diff Seq(card), number - 1, result ++ Seq(card))
  }
}

object Deck {
  def apply(): Deck = Deck(initDeck())

  private def initDeck(): Seq[Card] = Seq(0, 0, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8).map(DamageCard)
}
