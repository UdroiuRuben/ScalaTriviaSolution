package com.adaptionsoft.games.trivia.utils

import scala.language.implicitConversions

object QuestionCategories extends Enumeration {
  type QuestionCategory = Value

  val Pop: QuestionCategory = Value("Pop")
  val Science: QuestionCategory = Value("Science")
  val Sports: QuestionCategory = Value("Sports")
  val Rock: QuestionCategory = Value("Rock")

  implicit def valueToString(value: QuestionCategory): String = value.toString
}
