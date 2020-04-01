package models

trait Card {
  def cost: Int
}

case class DamageCard(cost: Int) extends Card
