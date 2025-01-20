package com.adaptionsoft.games.trivia.utils

import com.adaptionsoft.games.trivia.utils.QuestionCategories.*

trait GameHelper {
  def prepareNextPlayer(numberOfPlayers: Int, currentPlayerId: Int): Int = (currentPlayerId + 1) % numberOfPlayers

  def printAnswerWasCorrectMessage(playerName: String, playerGold: Int): Unit = {
    println("Answer was correct!!!!")
    println(s"$playerName now has $playerGold gold coins.")
  }

  def currentCategory(questionCategoryIndex: Int): QuestionCategory = {
    questionCategoryIndex % QuestionCategories.values.size match {
      case 0 => Pop
      case 1 => Science
      case 2 => Sports
      case 3 => Rock
      case _ => throw new Exception("Unidentified category!")
    }
  }

  def calculateNewPlayerGameLocation(currentPlace: Int, roll: Int): Int = (currentPlace + roll) % 12

  def didPlayerWin(playerGold: Int): Boolean = playerGold == 6
}
