import helpers.GameHelper
import models.{DrawCards, NoPlayCard, ReceiveMana, RefillMana}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.{BeforeAndAfter, GivenWhenThen, OptionValues}

class GameAppSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter with OptionValues {

  Feature("TCG Game") {
    Scenario("With basic rules") {
      When("The game starts")
      GameApp.main(Array("Charles", "Henry"))

      Then("The two players should have 30 health and 0 mana")
      val (firstPlayerBeforeStart, secondPlayerBeforeStart) = GameHelper.getStateAtTurn(-1)
      assert(firstPlayerBeforeStart.health === 30)
      assert(firstPlayerBeforeStart.mana.number === 0)

      assert(secondPlayerBeforeStart.health === 30)
      assert(secondPlayerBeforeStart.mana.number === 0)

      Then("All players has the starting set of cards")
      val cards = Seq(0, 0, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8)
      assert(firstPlayerBeforeStart.deck.cards.map(_.cost) === cards)
      assert(secondPlayerBeforeStart.deck.cards.map(_.cost) === cards)

      val actionsAtTurn0 = GameHelper.getTurnActions(0)

      Then("First player has drawn 3 cards")
      assert(actionsAtTurn0.head === DrawCards(3, 0))

      Then("Second player has drawn 4 cards")
      assert(actionsAtTurn0.last === DrawCards(4, 0))

      When("The first player begins its turn")
      val actionsAtTurn1 = GameHelper.getTurnActions(1)

      Then("He receives 1 mana and refill missing mana")
      assert(actionsAtTurn1.head === ReceiveMana(1))
      assert(actionsAtTurn1(1) === RefillMana(1))

      Then("He draws a card")
      assert(actionsAtTurn1(2) === DrawCards(1, 1))

      Then("A turn ends when the player can play no more card")
      assert(actionsAtTurn1.last === NoPlayCard(1))

      Then("The player that drops below 0 HP loses")
      val (firstPlayerAtEnd, secondPlayerAtEnd) = GameHelper.getFinalState
      val winner = GameApp.getWinner
      val computerWinner = if (firstPlayerAtEnd.health <= 0) secondPlayerAtEnd else firstPlayerAtEnd

      assert(winner === computerWinner)
    }
  }
}
