package models

case class Bin(cards: Seq[Card] = Nil) {
  def useCard(card: Card): Bin = Bin(Seq(card) ++ cards)
}