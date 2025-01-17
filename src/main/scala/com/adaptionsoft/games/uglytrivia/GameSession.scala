package com.adaptionsoft.games.uglytrivia

import com.adaptionsoft.games.trivia.models.{GameQuestions, Player}
import com.adaptionsoft.games.trivia.utils.Constants.*
import com.adaptionsoft.games.trivia.utils.GameHelper
import com.adaptionsoft.games.trivia.utils.QuestionCategories.*

case class GameSession(
                        players: List[Player] = List(),
                        currentPlayerId: Int = 0,
                        questions: GameQuestions = GameQuestions.initializeQuestions()
                       ) extends GameHelper {
  def initializeGameInstance(numberOfQuestions: Int = DEFAULT_NUMBER_OF_QUESTIONS): GameSession = {
    GameSession(questions = GameQuestions.initializeQuestions(numberOfQuestions))
  }

  def isPlayable: Boolean = players.length >= 2

  def addPlayer(playerName: String): GameSession = {
    val newPlayersList = players :+ Player(id = players.length, name = playerName)
    println(s"$playerName was added")
    println(s"Number of players: ${newPlayersList.length}")

    copy(players = newPlayersList)
  }

  def roll(roll: Int): GameSession = {
    val currentPlayer = players(currentPlayerId)

    println()
    println(s"${currentPlayer.name} is the current player")
    println(s"They have rolled a $roll")

    val isGettingOutOfPenaltyBox = currentPlayer.inPenaltyBox && (roll % 2 != 0)

    val newPlayerGameLocation = if (!isGettingOutOfPenaltyBox && currentPlayer.inPenaltyBox) {
      println(s"${currentPlayer.name} is not getting out of the penalty box")

      currentPlayer.gameLocation
    } else {
      if (currentPlayer.inPenaltyBox) println(s"${currentPlayer.name} is getting out of the penalty box")

      val newLocation = newPlayerLocation(currentPlayer.gameLocation, roll)
      println(s"${currentPlayer.name}'s new location is $newLocation")
      println(s"The category is ${currentCategory(newLocation)}")

      newLocation
    }

    val updatedPlayer = currentPlayer
      .copy(gameLocation = newPlayerGameLocation, isGettingOutOfPenaltyBox = isGettingOutOfPenaltyBox)

    val (newGameSessionWithUpdatedQuestions, question) = askQuestion(newPlayerGameLocation)
    println(question)

    newGameSessionWithUpdatedQuestions.copy(players = players.updated(currentPlayerId, updatedPlayer))
  }


  def wrongAnswer(): (GameSession, Boolean) = {
    val player = players(currentPlayerId)
    println("Question was incorrectly answered")
    println(s"${player.name} was sent to the penalty box")

    val updatedPlayer = player.copy(inPenaltyBox = true)

    (updateGameRound(updatedPlayer), GAME_CONTINUES)
  }

  def wasCorrectlyAnswered(): (GameSession, Boolean) = {
    val player = players(currentPlayerId)
    val updatedPlayer = player.copy(goldCoins = player.goldCoins + 1)

    if (player.inPenaltyBox) {
      if (player.isGettingOutOfPenaltyBox) {
        printAnswerWasCorrectMessage(player.name, updatedPlayer.goldCoins)
        checkIfGameEnds(updatedPlayer)
      } else {
        val nextPlayer = prepareNextPlayer(players.length, player.id)

        (this.copy(currentPlayerId = nextPlayer), GAME_CONTINUES)
      }
    } else {
      printAnswerWasCorrectMessage(player.name, updatedPlayer.goldCoins)
      checkIfGameEnds(updatedPlayer)
    }
  }

  def askQuestion(playerPlace: Int): (GameSession, String) = {
    currentCategory(playerPlace) match {
      case Pop => extractQuestionFromList(Pop)
      case Science => extractQuestionFromList(Science)
      case Sports => extractQuestionFromList(Sports)
      case Rock => extractQuestionFromList(Rock)
      case _ => throw new Exception("An error occurred during askQuestion!")
    }
  }

  private def extractQuestionFromList(questionCategory: QuestionCategory): (GameSession, String) = {
    val (question, updatedQuestions) = questionCategory match {
      case Pop => (questions.popQuestions.head, updateQuestions(questionCategory))
      case Science => (questions.scienceQuestions.head, updateQuestions(questionCategory))
      case Sports => (questions.sportsQuestions.head, updateQuestions(questionCategory))
      case Rock => (questions.rockQuestions.head, updateQuestions(questionCategory))
      case _ => throw new Exception("Undefined question category!")
    }

    (copy(questions = updatedQuestions), question)
  }

  private def updateQuestions(questionCategory: QuestionCategory): GameQuestions = {
    questionCategory match {
      case Pop => questions.copy(popQuestions = questions.popQuestions.tail)
      case Science => questions.copy(scienceQuestions = questions.scienceQuestions.tail)
      case Sports => questions.copy(sportsQuestions = questions.sportsQuestions.tail)
      case Rock => questions.copy(rockQuestions = questions.rockQuestions.tail)
      case _ => throw new Exception("Undefined question category!")
    }
  }

  private def checkIfGameEnds(player: Player): (GameSession, Boolean) = {
    if (didPlayerWin(player.goldCoins)) (this.copy(players = players.updated(currentPlayerId, player)), GAME_ENDS_HERE) else (updateGameRound(player), GAME_CONTINUES)
  }

  private def updateGameRound(currentPlayer: Player): GameSession = {
    val nextPlayer = prepareNextPlayer(players.length, currentPlayer.id)

    copy(players = players.updated(currentPlayerId, currentPlayer), currentPlayerId = nextPlayer)
  }

  def finishGame: Boolean = {
    println(s"${players(currentPlayerId).name} won the game!")

    GAME_ENDS_HERE
  }

  def getWinner: Player = players(currentPlayerId)
}