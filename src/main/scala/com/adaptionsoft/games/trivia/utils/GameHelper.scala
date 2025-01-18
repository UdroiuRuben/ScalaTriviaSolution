package com.adaptionsoft.games.trivia.utils

import com.adaptionsoft.games.trivia.utils.Constants.FIRST_PLAYER_ID
import com.adaptionsoft.games.trivia.utils.QuestionCategories.*

trait GameHelper {
  def prepareNextPlayer(numberOfPlayers: Int, currentPlayerId: Int): Int = if (numberOfPlayers == currentPlayerId + 1) FIRST_PLAYER_ID else currentPlayerId + 1

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

  def calculateNewPlayerGameLocation(currentPlace: Int, roll: Int): Int = if (currentPlace + roll > 11) currentPlace + roll - 12 else currentPlace + roll

  def didPlayerWin(playerGold: Int): Boolean = playerGold == 6
}
