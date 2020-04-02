package models

case class Mana(number: Int = 0, used: Int = 0) {
  lazy val available: Int = number - used

  def useCard(card: Card): Mana = Mana(number, used + card.cost)

  def receiveMana: Mana = Mana(Math.min(number + 1, 10), used)

  def refillMana: Mana = Mana(number)
}
