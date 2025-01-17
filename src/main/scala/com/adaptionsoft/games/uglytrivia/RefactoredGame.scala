package com.adaptionsoft.games.uglytrivia

import com.adaptionsoft.games.trivia.models.Player
import com.adaptionsoft.games.trivia.utils.Constants.DEFAULT_NUMBER_OF_QUESTIONS
import com.adaptionsoft.games.trivia.utils.QuestionCategories
import com.adaptionsoft.games.trivia.utils.QuestionCategories.*

import java.util
import java.util.{ArrayList, LinkedList}


object RefactoredGame {
  val players: ArrayList[Player] = new ArrayList[Player]
  var currentPlayer: Int = 0
  var isGettingOutOfPenaltyBox: Boolean = false

//  var places: Array[Int] = new Array[Int](6)
//  var purses: Array[Int] = new Array[Int](6)
//  var inPenaltyBox: Array[Boolean] = new Array[Boolean](6)

  val popQuestions: List[String] = generateQuestions(Pop, DEFAULT_NUMBER_OF_QUESTIONS)
  val scienceQuestions: List[String] = generateQuestions(Science, DEFAULT_NUMBER_OF_QUESTIONS)
  val sportsQuestions: List[String] = generateQuestions(Sports, DEFAULT_NUMBER_OF_QUESTIONS)
  val rockQuestions: List[String] = generateQuestions(Rock, DEFAULT_NUMBER_OF_QUESTIONS)

  private def generateQuestions(questionTopic: String, numberOfQuestions: Int): List[String] = {
    List.tabulate(numberOfQuestions)(questionNumber => s"$questionTopic Question ${questionNumber + 1}")
  }
  
  def isPlayable: Boolean = players.size >= 2

  def add(playerName: String): Unit = {
    val player = Player(playerName = playerName)
    players.add(player)

    println(s"${player.playerName} was added")
    println(s"Number of participants ${players.size}")
  }

  def roll(roll: Int): Unit = {
    val currentPlayerDude = players.get(currentPlayer)
    val currentPlayerName = currentPlayerDude.playerName
    val currentPlayerPlace = currentPlayerDude.place
    
    println(s"$currentPlayerName is the current player")
    println("They have rolled a " + roll)
    
    val newPositionInLeaderboardForCurrentPlayer = updatePlayerPlace(currentPlayerPlace, roll)
    println(s"$currentPlayerName's new location is $newPositionInLeaderboardForCurrentPlayer")
    println(s"The category is ${currentCategory(newPositionInLeaderboardForCurrentPlayer)}")

    val gettingOut = if (currentPlayerDude.inPenaltyBox && roll % 2 != 0) true else false
    
    
    val updatedPlayer = currentPlayerDude
      .copy(place = newPositionInLeaderboardForCurrentPlayer, isGettingOutOfPenaltyBox = gettingOut)
    
    askQuestion(newPositionInLeaderboardForCurrentPlayer)
  }
  
  private def updatePlayerPlace(currentPlace: Int, roll: Int): Int = {
    if (currentPlace + roll > 11) currentPlace + roll - 12 else currentPlace + roll
  }
  
  def askQuestion(playerPlace: Int): String = {
    currentCategory(playerPlace) match {
      case Pop => popQuestions.head
      case Science => scienceQuestions.head
      case Sports => sportsQuestions.head
      case Rock => rockQuestions.head
      case _ => throw new Exception("An error occurred during askQuestion!")
    }
  }

  def currentCategory(playerPlace: Int): QuestionCategory = {
    playerPlace % QuestionCategories.values.size match {
      case 0 => Pop
      case 1 => Science
      case 2 => Sports
      case 3 => Rock
      case _ => throw new Exception("Unidentified category!")
    }
  }

  def wasCorrectlyAnswered: Boolean =
    if (inPenaltyBox(currentPlayer)) {
      if (isGettingOutOfPenaltyBox) {
        println("Answer was correct!!!!")
        purses(currentPlayer) += 1
        println(players.get(currentPlayer) + " now has " + purses(currentPlayer) + " Gold Coins.")
        var winner: Boolean = didPlayerWin
        currentPlayer += 1
        if (currentPlayer == players.size) currentPlayer = 0
        winner
      }
      else {
        currentPlayer += 1
        if (currentPlayer == players.size) currentPlayer = 0
        true
      }
    }
    else {
      println("Answer was corrent!!!!")
      purses(currentPlayer) += 1
      println(players.get(currentPlayer) + " now has " + purses(currentPlayer) + " Gold Coins.")
      var winner: Boolean = didPlayerWin
      currentPlayer += 1
      if (currentPlayer == players.size) currentPlayer = 0
      winner
    }

  def wrongAnswer: Boolean =
    println("Question was incorrectly answered")
    println(players.get(currentPlayer) + " was sent to the penalty box")
    inPenaltyBox(currentPlayer) = true
    currentPlayer += 1
    if (currentPlayer == players.size) currentPlayer = 0
    true

  private def didPlayerWin: Boolean = !(purses(currentPlayer) == 6)

}
