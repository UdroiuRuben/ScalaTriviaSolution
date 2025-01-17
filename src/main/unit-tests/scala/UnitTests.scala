import com.adaptionsoft.games.uglytrivia.{Game, RefactoredGame}
import org.scalatest.funsuite.AnyFunSuite

class UnitTests extends AnyFunSuite {
  private final val DEFAULT_GAME_QUESTIONS_NUMBER = 50

  test("UT01 - Game is playable") {
    val someGame = new RefactoredGame()
    someGame.add("PlayerOne")
    someGame.add("PlayerTwo")
    someGame.add("PlayerThree")

    assert(someGame.isPlayable, "Game should be playable since it has at least 2 players!")
  }

  test("UT02 - Game is unplayable") {
    val someGame = new RefactoredGame()
    someGame.add("randomPlayer")

    assert(!someGame.isPlayable, "Game should be unplayable since it has 1 player!")
  }

  test("UT03 - initialize game questions") {
    val someGame = new RefactoredGame()

    assert(someGame.popQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGame.rockQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGame.scienceQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGame.sportsQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
  }

  test("UT04 - test add function") {
    val someGame = new RefactoredGame()
    someGame.add("PlayerOne")
    someGame.add("PlayerTwo")
    someGame.add("PlayerThree")

    assert(someGame.howManyPlayers == 3, "This game should have 3 players!")
    assert(someGame.places.sum == 0, "The game is initialized, all players should be on 0.")
    assert(someGame.purses.sum == 0 , "The game is initialized, all players should have 0 purses.")
    assert(!someGame.inPenaltyBox.contains(true), "The game is initialized, no player should be in penalty box.")
  }


}
