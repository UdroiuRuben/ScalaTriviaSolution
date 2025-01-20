package com.adaptionsoft.games.trivia.utils

import com.adaptionsoft.games.trivia.models.{GameQuestions, Player}
import com.adaptionsoft.games.trivia.utils.Constants.*
import com.adaptionsoft.games.trivia.utils.QuestionCategories.*

case class GameSession(
                        players: List[Player] = List(),
                        currentPlayerId: Int = 0,
                        questions: GameQuestions = GameQuestions.initializeQuestions()
                       ) extends GameHelper {
  def initializeGameInstance(numberOfQuestions: Int = DEFAULT_NUMBER_OF_QUESTIONS): GameSession = {
    GameSession(questions = GameQuestions.initializeQuestions(numberOfQuestions))
  }

  def addPlayer(playerName: String): GameSession = {
    val newPlayersList = players :+ Player(id = players.length, name = playerName)
    println(s"$playerName was added")
    println(s"Number of players: ${newPlayersList.length}")

    this.copy(players = newPlayersList)
  }

  def isPlayable: Boolean = players.length >= 2

  def rollDices(roll: Int): GameSession = {
    val currentPlayer = players(currentPlayerId)
    println(s"${currentPlayer.name} is the current player")
    println(s"They have rolled a $roll")

    val isGettingOutOfPenaltyBox = currentPlayer.inPenaltyBox && (roll % 2 != 0)

    val nextQuestionCategoryIndex = if (!isGettingOutOfPenaltyBox && currentPlayer.inPenaltyBox) {
      println(s"${currentPlayer.name} is not getting out of the penalty box")

      currentPlayer.questionCategoryIndex
    } else {
      if (currentPlayer.inPenaltyBox) println(s"${currentPlayer.name} is getting out of the penalty box")

      val newLocation = calculateNewPlayerGameLocation(currentPlayer.questionCategoryIndex, roll)
      println(s"${currentPlayer.name}'s new location is $newLocation")
      println(s"The category is ${currentCategory(newLocation)}")

      newLocation
    }

    val (newGameSessionWithUpdatedQuestions, question) = askQuestion(nextQuestionCategoryIndex)

    println(question)
    val updatedPlayer = currentPlayer.copy(questionCategoryIndex = nextQuestionCategoryIndex, isGettingOutOfPenaltyBox = isGettingOutOfPenaltyBox)
    newGameSessionWithUpdatedQuestions.copy(players = players.updated(currentPlayerId, updatedPlayer))
  }

  def wrongAnswer(): (GameSession, Boolean) = {
    val player = players(currentPlayerId)
    println("Question was incorrectly answered")

    val updatedGameSession = if (!player.inPenaltyBox) {
      println(s"${player.name} was sent to the penalty box")
      player.copy(inPenaltyBox = true)

      updateGameRound(player.copy(inPenaltyBox = true))
    } else {
      val nextPlayer = prepareNextPlayer(players.length, player.id)
      this.copy(currentPlayerId = nextPlayer)
    }

    (updatedGameSession, CONTINUE_GAME)
  }

  def correctAnswer(): (GameSession, Boolean) = {
    val player = players(currentPlayerId)
    val updatedPlayer = player.copy(goldCoins = player.goldCoins + 1)

    if (player.inPenaltyBox) {
      if (player.isGettingOutOfPenaltyBox) {
        printAnswerWasCorrectMessage(player.name, updatedPlayer.goldCoins)
        checkIfGameEnds(updatedPlayer)
      } else {
        val nextPlayer = prepareNextPlayer(players.length, player.id)

        (this.copy(currentPlayerId = nextPlayer), CONTINUE_GAME)
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

    (this.copy(questions = updatedQuestions), question)
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

  private def checkIfGameEnds(updatedPlayer: Player): (GameSession, Boolean) = {
    if (didPlayerWin(updatedPlayer.goldCoins)) {
      (this.copy(players = players.updated(currentPlayerId, updatedPlayer)), END_GAME)
    } else {
      (updateGameRound(updatedPlayer), CONTINUE_GAME)
    }
  }

  private def updateGameRound(currentPlayer: Player): GameSession = {
    val nextPlayer = prepareNextPlayer(players.length, currentPlayerId)

    this.copy(players = players.updated(currentPlayerId, currentPlayer), currentPlayerId = nextPlayer)
  }

  def finishGame: Boolean = {
    println(s"${players(currentPlayerId).name} won the game!")

    END_GAME
  }

}