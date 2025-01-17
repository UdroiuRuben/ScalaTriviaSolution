package com.adaptionsoft.games.trivia.models

case class Player(
                   id: Int,
                   name: String,
                   gameLocation: Int = 0,
                   goldCoins: Int = 0,
                   inPenaltyBox: Boolean = false,
                   isGettingOutOfPenaltyBox: Boolean = false
                 )
