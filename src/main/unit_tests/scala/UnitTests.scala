import com.adaptionsoft.games.trivia.runner.RefactoredGameRunner
import com.adaptionsoft.games.trivia.utils.GameSession
import com.adaptionsoft.games.trivia.utils.QuestionCategories.*
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
    val randomQuestionFromList = 3
    val questionIndex = 2

    assert(someGameQuestions.popQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGameQuestions.popQuestions(questionIndex) == s"Pop question $randomQuestionFromList")

    assert(someGameQuestions.rockQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGameQuestions.rockQuestions(questionIndex) == s"Rock question $randomQuestionFromList")

    assert(someGameQuestions.scienceQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGameQuestions.scienceQuestions(questionIndex) == s"Science question $randomQuestionFromList")

    assert(someGameQuestions.sportsQuestions.size == DEFAULT_GAME_QUESTIONS_NUMBER)
    assert(someGameQuestions.sportsQuestions(questionIndex) == s"Sports question $randomQuestionFromList")
  }

  test("UT04 - test add function") {
    val someGame = GameSession()
      .addPlayer("PlayerOne")
      .addPlayer("PlayerTwo")
      .addPlayer("PlayerThree")

    assert(someGame.isPlayable, "This game should have 3 players!")
    assert(someGame.players.map(_.questionCategoryIndex).sum == 0, "The game is initialized, all players should be on 0.")
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

  test("UT06 - askQuestion: 1 question from each category") {
    val someGame = GameSession()

    val popQuestionCategoryIndex = 0
    assert(someGame.askQuestion(popQuestionCategoryIndex).questions.popQuestions.head == "Pop question 2")

    val scienceQuestionCategoryIndex = 1
    assert(someGame.askQuestion(scienceQuestionCategoryIndex).questions.scienceQuestions.head == "Science question 2")

    val sportsQuestionCategoryIndex = 2
    assert(someGame.askQuestion(sportsQuestionCategoryIndex).questions.sportsQuestions.head == "Sports question 2")

    val rockQuestionCategoryIndex = 3
    assert(someGame.askQuestion(rockQuestionCategoryIndex).questions.rockQuestions.head == "Rock question 2")
  }

  test("UT07 - askQuestion: multiple questions from the same category") {
    val someGame = GameSession()
    val placeForPopQuestion = 0

    val firstNewGameSession = someGame.askQuestion(placeForPopQuestion)
    assert(firstNewGameSession.questions.popQuestions.head == "Pop question 2", "After the first question was asked, the list should start with the second question")

    val secondNewGameSession = firstNewGameSession.askQuestion(placeForPopQuestion)
    assert(secondNewGameSession.questions.popQuestions.head == "Pop question 3", "The list of questions should start with the 3rd question")

    val thirdNewGameSession = secondNewGameSession.askQuestion(placeForPopQuestion)
    assert(thirdNewGameSession.questions.popQuestions.head == "Pop question 4", "The list of questions should start with the 4th question")
  }

  /** Comparison with old version (using the same seed, 888)
   * 1. Number of players 3: Chet / Pat / Sue (done)
   * 2. Science questions asked: 6 (done)
   * 3. Pop questions asked: 2 (done)
   * 4. Rock questions asked: 3 (done)
   * 5. Sports questions asked: 6 (done)
   * 6. Gold coins:
   * -> Chet: 5g (done)
   * -> Pat: 6g (done)
   * -> Sue: 5g (done)
   * 7. Winner: Pat (done)
   * 8. Number of output lines: 129 (done)
   * 9. Chet's name count: 23 (done)
   * 10. Pat's name count: 19 (done)
   * 11. Sue's name count: 16 (done)
   * 12. Penalty count: 5 (done)
   * 13. Correct answers: 16 (done)
   * 14. Incorrect answers: 1 (done)
   */
  test("Integration test - test a full game") {
    val randomSeed = Random
    randomSeed.setSeed(888)

    val randomGame = GameSession()
      .addPlayer("Chet")
      .addPlayer("Pat")
      .addPlayer("Sue")

    val finalGameSessionImage = RefactoredGameRunner
      .gameInProgress(randomSeed, randomGame, false)
      ._1

    assert(finalGameSessionImage.players.equals(expectedGameSessionT08.players))
    assert(finalGameSessionImage.currentPlayerId.equals(expectedGameSessionT08.currentPlayerId))
    assert(finalGameSessionImage.questions.popQuestions.equals(expectedGameSessionT08.questions.popQuestions))
    assert(finalGameSessionImage.questions.scienceQuestions.equals(expectedGameSessionT08.questions.scienceQuestions))
    assert(finalGameSessionImage.questions.sportsQuestions.equals(expectedGameSessionT08.questions.sportsQuestions))
    assert(finalGameSessionImage.questions.rockQuestions.equals(expectedGameSessionT08.questions.rockQuestions))
  }
}
