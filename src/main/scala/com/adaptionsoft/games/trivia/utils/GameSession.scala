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
    val player = players(currentPlayerId)
    println(s"${player.name} is the current player")
    println(s"They have rolled a $roll")

    val isGettingOutOfPenaltyBox = player.inPenaltyBox && (roll % 2 != 0)

    val nextQuestionCategoryIndex = if (!isGettingOutOfPenaltyBox && player.inPenaltyBox) {
      println(s"${player.name} is not getting out of the penalty box")

      player.questionCategoryIndex
    } else {
      val nextQuestionCategory = calculateNextQuestionCategoryIndex(player.questionCategoryIndex, roll)

      if (player.inPenaltyBox) println(s"${player.name} is getting out of the penalty box")
      println(s"${player.name}'s new location is $nextQuestionCategory")
      println(s"The category is ${currentCategory(nextQuestionCategory)}")

      nextQuestionCategory
    }

    val (newGameSessionWithUpdatedQuestions, question) = askQuestion(nextQuestionCategoryIndex)
    println(question)
    
    val updatedPlayer = player.copy(questionCategoryIndex = nextQuestionCategoryIndex, isGettingOutOfPenaltyBox = isGettingOutOfPenaltyBox)
    newGameSessionWithUpdatedQuestions.copy(players = players.updated(currentPlayerId, updatedPlayer))
  }

  def wrongAnswer(): (GameSession, Boolean) = {
    val player = players(currentPlayerId)
    println("Question was incorrectly answered")

    val updatedGameSession = if (!player.inPenaltyBox) {
      println(s"${player.name} was sent to the penalty box")

      updateGameRound(player.copy(inPenaltyBox = true))
    } else {
      this.copy(currentPlayerId = getNextPlayerIndex(players.length, player.id))
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
        (this.copy(currentPlayerId = getNextPlayerIndex(players.length, player.id)), CONTINUE_GAME)
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
    this.copy(players = players.updated(currentPlayerId, currentPlayer),
      currentPlayerId = getNextPlayerIndex(players.length, currentPlayerId))
  }

  def finishGame: Boolean = {
    println(s"${players(currentPlayerId).name} won the game!")

    END_GAME
  }

}