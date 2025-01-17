package com.adaptionsoft.games.trivia.models

case class Player(
                  playerName: String,
                  place: Int = 0,
                  goldCoins: Int = 0,
                  inPenaltyBox: Boolean = false,
                  isGettingOutOfPenaltyBox: Boolean = false
                 )
