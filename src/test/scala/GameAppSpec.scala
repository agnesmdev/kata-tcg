import java.io.{ByteArrayOutputStream, PrintStream}

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.{BeforeAndAfter, GivenWhenThen, OptionValues}

class GameAppSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter with OptionValues {

  private val consoleContent = new ByteArrayOutputStream
  private val originalConsoleContent = System.out

  before {
    System.setOut(new PrintStream(consoleContent))
  }

  after {
    System.setOut(originalConsoleContent)
  }

  Feature("TCG Game") {
    Scenario("With basic rules") {
      When("The game starts")
      GameApp.main(Array("Charles", "Henry"))

      val firstPlayer = GameApp.getFirstPlayer
      val secondPlayer = GameApp.getSecondPlayer

      Then("The two players should have 30 health and 0 mana")
      assert(firstPlayer.health === 30)
      assert(firstPlayer.mana === 0)

      assert(secondPlayer.health === 30)
      assert(secondPlayer.mana === 0)

      Then("All players has the starting set of cards")
      val cards = Seq(0, 0, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 7, 8)
      assert((firstPlayer.deck.cards ++ firstPlayer.hand).map(_.cost).sorted === cards)
      assert((secondPlayer.deck.cards ++ secondPlayer.hand).map(_.cost).sorted === cards)

      Then("First player has drawn 3 cards")
      assert(firstPlayer.hand.length === 3)
      assert(firstPlayer.deck.cards.length === 27)
      assert(consoleContent.toString().contains(s"Player ${firstPlayer.coloredName} has drawn 3 cards"))

      Then("Second player has drawn 4 cards")
      assert(secondPlayer.hand.length === 4)
      assert(secondPlayer.deck.cards.length === 26)
      assert(consoleContent.toString().contains(s"Player ${secondPlayer.coloredName} has drawn 4 cards"))
    }
  }
}
