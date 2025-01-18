import com.adaptionsoft.games.trivia.models.{GameQuestions, Player}
import com.adaptionsoft.games.trivia.utils.Constants.DEFAULT_NUMBER_OF_QUESTIONS
import com.adaptionsoft.games.trivia.utils.GameSession

trait ExpectedResults {
  val expectedGameSessionT08: GameSession = {
    val playerChet = Player(0, "Chet", 2, 5, true, true)
    val playerPat = Player(1, "Pat", 2, 6)
    val playerSue = Player(2, "Sue", 6, 5)
    val players = List(playerChet, playerPat, playerSue)
    val currentPlayer = 1
    val questionsBaseline = GameQuestions.initializeQuestions()
    val questions = questionsBaseline.copy(
      popQuestions = questionsBaseline.popQuestions.takeRight(DEFAULT_NUMBER_OF_QUESTIONS - 2),
      scienceQuestions = questionsBaseline.scienceQuestions.takeRight(DEFAULT_NUMBER_OF_QUESTIONS - 6),
      sportsQuestions = questionsBaseline.sportsQuestions.takeRight(DEFAULT_NUMBER_OF_QUESTIONS - 6),
      rockQuestions = questionsBaseline.rockQuestions.takeRight(DEFAULT_NUMBER_OF_QUESTIONS - 3)
    )

    GameSession(players, currentPlayer, questions)
  }
}
