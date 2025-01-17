package com.adaptionsoft.games.uglytrivia

import com.adaptionsoft.games.trivia.utils.Constants.DEFAULT_NUMBER_OF_QUESTIONS
import com.adaptionsoft.games.trivia.utils.QuestionCategories
import com.adaptionsoft.games.trivia.utils.QuestionCategories.*

import java.util
import java.util.{ArrayList, LinkedList}

class RefactoredGame {
  var players: ArrayList[String] = new ArrayList[String]
  var places: Array[Int] = new Array[Int](6)
  var purses: Array[Int] = new Array[Int](6)
  var inPenaltyBox: Array[Boolean] = new Array[Boolean](6)

  val popQuestions: List[String] = generateQuestions(Pop, DEFAULT_NUMBER_OF_QUESTIONS)
  val scienceQuestions: List[String] = generateQuestions(Science, DEFAULT_NUMBER_OF_QUESTIONS)
  val sportsQuestions: List[String] = generateQuestions(Sports, DEFAULT_NUMBER_OF_QUESTIONS)
  val rockQuestions: List[String] = generateQuestions(Rock, DEFAULT_NUMBER_OF_QUESTIONS)

  var currentPlayer: Int = 0
  var isGettingOutOfPenaltyBox: Boolean = false

  private def generateQuestions(questionTopic: String, numberOfQuestions: Int): List[String] = {
    List.tabulate(numberOfQuestions)(questionNumber => s"$questionTopic Question ${questionNumber + 1}")
  }


  def isPlayable: Boolean = (howManyPlayers >= 2)

  def add(playerName: String): Boolean =
    players.add(playerName)
    places(howManyPlayers) = 0
    purses(howManyPlayers) = 0
    inPenaltyBox(howManyPlayers) = false
    println(playerName + " was added")
    println("They are player number " + players.size)
    true

  def howManyPlayers: Int = players.size

  def roll(roll: Int): Unit =
    println(players.get(currentPlayer) + " is the current player")
    println("They have rolled a " + roll)
    if (inPenaltyBox(currentPlayer)) {
      if (roll % 2 != 0) {
        isGettingOutOfPenaltyBox = true
        println(players.get(currentPlayer) + " is getting out of the penalty box")
        places(currentPlayer) = places(currentPlayer) + roll
        if (places(currentPlayer) > 11) places(currentPlayer) = places(currentPlayer) - 12
        println(players.get(currentPlayer) + "'s new location is " + places(currentPlayer))
        println("The category is " + currentCategory)
        askQuestion(places(currentPlayer))
      }
      else {
        println(players.get(currentPlayer) + " is not getting out of the penalty box")
        isGettingOutOfPenaltyBox = false
      }
    }
    else {
      places(currentPlayer) = places(currentPlayer) + roll
      if (places(currentPlayer) > 11) places(currentPlayer) = places(currentPlayer) - 12
      println(players.get(currentPlayer) + "'s new location is " + places(currentPlayer))
      println("The category is " + currentCategory)
      askQuestion(places(currentPlayer))
    }

  private def askQuestion(playerPlace: Int): Unit =
    currentCategory(playerPlace) match {
      case Pop => println(popQuestions.head)
      case Science => println(scienceQuestions.head)
      case Sports => println(sportsQuestions.head)
      case Rock => println(rockQuestions.head)
      case _ => throw new Exception("An error occurred during askQuestion!")
    }

  private def currentCategory(playerPlace: Int): QuestionCategory = {
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
