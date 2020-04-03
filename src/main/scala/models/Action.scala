package models

sealed trait Action {
  def turn: Int
}

case class DrawCards(number: Int, turn: Int) extends Action

case class DeckEmpty(turn: Int) extends Action

case class ReceiveMana(turn: Int) extends Action

case class RefillMana(turn: Int) extends Action

case class PlayCard(card: Card, turn: Int) extends Action

case class NoPlayCard(turn: Int) extends Action

case class TakeDamage(damage: Int, turn: Int) extends Action

case class Win(turn: Int) extends Action

case class BleedingOut(turn: Int) extends Action

case class Overload(turn: Int) extends Action