import helpers.GameHelper
import models._
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.{BeforeAndAfter, GivenWhenThen, OptionValues}

class GameAppSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter with OptionValues {

  before {
    GameHelper.clear()
  }

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

    Scenario("With Bleeding Out") {
      val player_1 = Player("Jean", first = true)
      val player_2 = Player("Henry", first = false)

      When("A player's deck is empty")
      val emptyDeckPlayer = player_1.copy(deck = Deck(Seq(3, 4, 5).map(DamageCard)))
      Game(emptyDeckPlayer, player_2).play()

      Then("They take one damage for each card drawn")
      (1 to GameHelper.getTotalTurns by 2).foreach { turn =>
        val actionsAtTurn = GameHelper.getTurnActions(turn)
        assert(actionsAtTurn.head === ReceiveMana(turn))
        assert(actionsAtTurn(1) === RefillMana(turn))
        assert(actionsAtTurn(2) === BleedingOut(turn))
      }
    }

    Scenario("With Overload") {
      val player_1 = Player("Marie", first = true)
      val player_2 = Player("Isabelle", first = false)

      When("A player's hand is full")
      val fullHandPlayer = player_1.copy(hand = Hand(Seq(3, 4, 5, 5, 6).map(DamageCard)))
      Game(fullHandPlayer, player_2).play()

      Then("They discard the drawn card")
      val actionsAtTurn1 = GameHelper.getTurnActions(1)
      assert(actionsAtTurn1.head === ReceiveMana(1))
      assert(actionsAtTurn1(1) === RefillMana(1))
      assert(actionsAtTurn1(2) === DrawCards(1, 1))
      assert(actionsAtTurn1(3) === Overload(1))
    }

    Scenario("With Dud Card") {
      val player_1 = Player("Etienne", first = true)
      val player_2 = Player("Paula", first = false)

      When("A player's hand only contains 0 mana cards")
      val fullHandPlayer = player_1.copy(hand = Hand(Seq(0, 0, 0).map(DamageCard)))
      Game(fullHandPlayer, player_2).play()

      Then("They play the 0 card")
      val actionsAtTurn1 = GameHelper.getTurnActions(1)
      assert(actionsAtTurn1.head === ReceiveMana(1))
      assert(actionsAtTurn1(1) === RefillMana(1))
      assert(actionsAtTurn1(2) === DrawCards(1, 1))

      val playZeroCardActions = actionsAtTurn1.filter(_ === PlayCard(DamageCard(0), 1))
      assert(playZeroCardActions.length === 3)

      Then("They do 0 damage")
      val zeroDamageActions = actionsAtTurn1.filter(_ === TakeDamage(0, 1))
      assert(zeroDamageActions.length === 3)
    }
  }
}
