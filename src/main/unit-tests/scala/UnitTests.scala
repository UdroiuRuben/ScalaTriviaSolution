import com.adaptionsoft.games.trivia.utils.QuestionCategories.*
import com.adaptionsoft.games.uglytrivia.{Game, RefactoredGame}
import org.scalatest.funsuite.AnyFunSuite

import java.io.{ByteArrayOutputStream, PrintStream}

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

  test("UT05 - test currentCategory") {
    val someGame = new RefactoredGame()

    assert(someGame.popQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGame.rockQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGame.scienceQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGame.sportsQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)


    def validateQuestionCategories(playerPositionsForCategory: List[Int], questionCategory: QuestionCategory): Unit = {
      playerPositionsForCategory
        .foreach(playerPlace => assert(someGame.currentCategory(playerPlace) == questionCategory, s"The question should be from $questionCategory category!"))
    }

    val playerPlacesInOrderToGetPopQuestions = List(0, 4, 8, 12, 16)
    validateQuestionCategories(playerPlacesInOrderToGetPopQuestions, Pop)

    val playerPlacesInOrderToGetScienceQuestions = List(1, 5, 9, 13, 17)
    validateQuestionCategories(playerPlacesInOrderToGetScienceQuestions, Science)

    val playerPlacesInOrderToGetSportsQuestions = List(2, 6, 10, 14, 18)
    validateQuestionCategories(playerPlacesInOrderToGetSportsQuestions, Sports)

    val playerPlacesInOrderToGetRockQuestions = List(3, 7, 11, 15, 19)
    validateQuestionCategories(playerPlacesInOrderToGetRockQuestions, Rock)
  }

  test("UTC06 - askQuestion: 1 question from each category") {
    val someGame = new RefactoredGame()

    val placeForPopQuestion = 0
    assert(someGame.askQuestion(placeForPopQuestion) == "Pop Question 1")

    val placeForScienceQuestion = 1
    assert(someGame.askQuestion(placeForScienceQuestion) == "Science Question 1")

    val placeForSportsQuestion = 2
    assert(someGame.askQuestion(placeForSportsQuestion) == "Sports Question 1")

    val placeForRockQuestion = 3
    assert(someGame.askQuestion(placeForRockQuestion) == "Rock Question 1")
  }


  test("UTC07 - askQuestion: multiple questions from the same category") {
    val someGame = new RefactoredGame()

    val placeForPopQuestion = 0
    assert(someGame.askQuestion(placeForPopQuestion) == "Pop Question 1")
    assert(someGame.askQuestion(placeForPopQuestion) == "Pop Question 2")
    assert(someGame.askQuestion(placeForPopQuestion) == "Pop Question 3")
  }
}
