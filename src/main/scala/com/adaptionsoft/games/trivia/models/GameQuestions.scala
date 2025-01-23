package com.adaptionsoft.games.trivia.models

import com.adaptionsoft.games.trivia.models.GameQuestions.generateQuestions
import com.adaptionsoft.games.trivia.utils.Constants.DEFAULT_NUMBER_OF_QUESTIONS
import com.adaptionsoft.games.trivia.utils.QuestionCategories.*

// de ce musai "case class"
// si nu o clasa tipica imutabila care sa incapsuleze creerea intrebarilor
// opt B: incapsulare in loc de clasa case care expune tot ce are.
case class GameQuestions(
                          popQuestions: List[String] = generateQuestions(Pop, DEFAULT_NUMBER_OF_QUESTIONS),
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

  private def generateQuestions(questionTopic: String, numberOfQuestions: Int): List[String] =
    List.tabulate(numberOfQuestions)(questionNumber => s"$questionTopic question ${questionNumber + 1}")
  //metoda exotica....
  
}
