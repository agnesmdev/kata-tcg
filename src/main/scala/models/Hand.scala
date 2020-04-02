package models

case class Hand(cards: Seq[Card] = Nil) {
  lazy val numberOfCards: Int = cards.length

  lazy val overview: String = cards.map(_.cost).mkString(" / ")

  def sortByCost: Seq[Card] = cards.sortBy(_.cost).reverse

  def draw(newCards: Seq[Card]): Hand = Hand(cards ++ newCards)

  def useCard(card: Card): Hand = Hand(cards diff Seq(card))
}
