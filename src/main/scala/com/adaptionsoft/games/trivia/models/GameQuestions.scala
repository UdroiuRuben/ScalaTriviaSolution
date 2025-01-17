package com.adaptionsoft.games.trivia.models

import com.adaptionsoft.games.trivia.utils.Constants.DEFAULT_NUMBER_OF_QUESTIONS
import com.adaptionsoft.games.trivia.utils.QuestionCategories._

case class GameQuestions(
                          popQuestions: List[String],
                          scienceQuestions: List[String],
                          sportsQuestions: List[String],
                          rockQuestions: List[String]
                        )

object GameQuestions {
  def initializeQuestions(numberOfQuestions: Int = DEFAULT_NUMBER_OF_QUESTIONS): GameQuestions = {
    GameQuestions(
      popQuestions = generateQuestions(Pop, numberOfQuestions),
      scienceQuestions = generateQuestions(Science, numberOfQuestions),
      sportsQuestions = generateQuestions(Sports, numberOfQuestions),
      rockQuestions = generateQuestions(Rock, numberOfQuestions)
    )
  }

  private def generateQuestions(questionTopic: String, numberOfQuestions: Int): List[String] = {
    List.tabulate(numberOfQuestions)(questionNumber => s"$questionTopic Question ${questionNumber + 1}")
  }
  
}
