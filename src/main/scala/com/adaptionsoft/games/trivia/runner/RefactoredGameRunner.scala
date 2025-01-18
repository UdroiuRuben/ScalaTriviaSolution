package com.adaptionsoft.games.trivia.runner

import com.adaptionsoft.games.trivia.utils.Constants.{CONTINUE_GAME, DEFAULT_NUMBER_OF_QUESTIONS, END_GAME}
import com.adaptionsoft.games.trivia.utils.GameSession

import scala.annotation.tailrec
import scala.util.Random

object RefactoredGameRunner {
  def main(args: Array[String]): Unit = {
    val randomNumber = Random
    randomNumber.setSeed(888)
    
    startPredefinedGame(randomNumber)
  }

  private def startPredefinedGame(gameSeed: Random): Unit = {
    val gameSession = GameSession()
      .initializeGameInstance(DEFAULT_NUMBER_OF_QUESTIONS)
      .addPlayer("Chet")
      .addPlayer("Pat")
      .addPlayer("Sue")

    if (gameSession.isPlayable) {
      gameInProgress(gameSeed, gameSession, false)
    } else {
      throw new Exception("Game is not playable! It requires at least 2 players.")
    }
    
  }

  @tailrec
  def gameInProgress(gameSeed: Random, currentGameSession: GameSession, gameFinished: Boolean): (GameSession, Boolean) = {
    gameFinished match {
      case END_GAME => (currentGameSession, currentGameSession.finishGame)
      case CONTINUE_GAME =>
        val rollGameSession = currentGameSession.rollDices(gameSeed.nextInt(5) + 1)
        val (afterAnswerGameSession, currentGameStatus) = if (gameSeed.nextInt(9) == 7) rollGameSession.wrongAnswer() else rollGameSession.correctAnswer()

        gameInProgress(gameSeed, afterAnswerGameSession, currentGameStatus)
    }
  }

}
