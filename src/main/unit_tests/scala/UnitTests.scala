import com.adaptionsoft.games.trivia.runner.RefactoredGameRunner
import com.adaptionsoft.games.trivia.utils.QuestionCategories.*
import com.adaptionsoft.games.uglytrivia.GameSession
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class UnitTests extends AnyFunSuite with ExpectedResults{
  private final val DEFAULT_GAME_QUESTIONS_NUMBER = 50

  test("UT01 - Game is playable") {
    val someGame = GameSession()
      .addPlayer("PlayerOne")
      .addPlayer("PlayerTwo")
      .addPlayer("PlayerThree")

    assert(someGame.isPlayable, "Game should be playable since it has at least 2 players!")
  }

  test("UT02 - Game is unplayable") {
    val someGame = GameSession()
      .addPlayer("randomPlayer")

    assert(!someGame.isPlayable, "Game should be unplayable since it has 1 playerGoldCoins!")
  }

  test("UT03 - initialize game questions") {
    val someGameQuestions = GameSession().questions

    assert(someGameQuestions.popQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGameQuestions.rockQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGameQuestions.scienceQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGameQuestions.sportsQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
  }

  test("UT04 - test add function") {
    val someGame = GameSession()
      .addPlayer("PlayerOne")
      .addPlayer("PlayerTwo")
      .addPlayer("PlayerThree")

    assert(someGame.isPlayable, "This game should have 3 players!")
    assert(someGame.players.map(_.gameLocation).sum == 0, "The game is initialized, all players should be on 0.")
    assert(someGame.players.map(_.goldCoins).sum == 0 , "The game is initialized, all players should have 0 purses.")
    assert(!someGame.players.exists(_.inPenaltyBox), "The game is initialized, no playerGoldCoins should be in penalty box.")
  }

  test("UT05 - test currentCategory") {
    val someGame = GameSession()

    assert(someGame.questions.popQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGame.questions.rockQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGame.questions.scienceQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGame.questions.sportsQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)


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
    val someGame = GameSession()

    val placeForPopQuestion = 0
    assert(someGame.askQuestion(placeForPopQuestion)._2 == "Pop Question 1")

    val placeForScienceQuestion = 1
    assert(someGame.askQuestion(placeForScienceQuestion)._2 == "Science Question 1")

    val placeForSportsQuestion = 2
    assert(someGame.askQuestion(placeForSportsQuestion)._2 == "Sports Question 1")

    val placeForRockQuestion = 3
    assert(someGame.askQuestion(placeForRockQuestion)._2 == "Rock Question 1")
  }


  test("UT07 - askQuestion: multiple questions from the same category") {
    val someGame = GameSession()
    val placeForPopQuestion = 0

    val (firstNewGameSession, firstQuestion) = someGame.askQuestion(placeForPopQuestion)
    assert(firstQuestion == "Pop Question 1")

    val (secondNewGameSession, secondQuestion) = firstNewGameSession.askQuestion(placeForPopQuestion)
    assert(secondQuestion == "Pop Question 2")

    val (thirdNewGameSession, thirdQuestion) = secondNewGameSession.askQuestion(placeForPopQuestion)
    assert(thirdQuestion == "Pop Question 3")
  }

  test("Integration test - test a full game") {
    val randomSeed = Random
    randomSeed.setSeed(888)

    val randomGame = GameSession()
      .addPlayer("Chet")
      .addPlayer("Pat")
      .addPlayer("Sue")

    val gameSessionFinalImage = RefactoredGameRunner
      .gameInProgress(randomSeed, randomGame, false)
      ._1

    assert(gameSessionFinalImage.players.equals(expectedGameSessionT08.players))
    assert(gameSessionFinalImage.currentPlayerId.equals(expectedGameSessionT08.currentPlayerId))
    assert(gameSessionFinalImage.questions.popQuestions.equals(expectedGameSessionT08.questions.popQuestions))
    assert(gameSessionFinalImage.questions.scienceQuestions.equals(expectedGameSessionT08.questions.scienceQuestions))
    assert(gameSessionFinalImage.questions.sportsQuestions.equals(expectedGameSessionT08.questions.sportsQuestions))
    assert(gameSessionFinalImage.questions.rockQuestions.equals(expectedGameSessionT08.questions.rockQuestions))
  }
}
