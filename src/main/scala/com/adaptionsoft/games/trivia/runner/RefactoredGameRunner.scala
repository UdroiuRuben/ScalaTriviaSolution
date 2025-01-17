package com.adaptionsoft.games.trivia.runner

import com.adaptionsoft.games.trivia.utils.Constants.{GAME_CONTINUES, GAME_ENDS_HERE}
import com.adaptionsoft.games.uglytrivia.GameSession

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
      .addPlayer("Chet")
      .addPlayer("Pat")
      .addPlayer("Sue")

    if (gameSession.isPlayable) {
      gameInProgress(gameSeed, gameSession, false)
    } else {
      throw new Exception("Game is not playable!")
    }
    
  }

  @tailrec
  def gameInProgress(gameSeed: Random, currentGameSession: GameSession, isGameFinished: Boolean): (GameSession, Boolean) = {
    isGameFinished match {
      case GAME_ENDS_HERE => (currentGameSession, currentGameSession.finishGame)
      case GAME_CONTINUES =>
        val rollGameSession = currentGameSession.roll(gameSeed.nextInt(5) + 1)
        val (afterAnswerGameSession, currentGameStatus) = if (gameSeed.nextInt(9) == 7) rollGameSession.wrongAnswer() else rollGameSession.wasCorrectlyAnswered()

        gameInProgress(gameSeed, afterAnswerGameSession, currentGameStatus)
    }
  }

}
