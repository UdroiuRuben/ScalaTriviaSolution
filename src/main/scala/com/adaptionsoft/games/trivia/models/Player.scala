package com.adaptionsoft.games.trivia.models

// problema asta se preta mai usor pe obiecte mutabile
case class Player(
                   id: Int, // ? chiar e necesar
                   name: String,
                   questionCategoryIndex: Int = 0, // location/place pt ca translatia locatiei la intrebare nu e raspunderea playerukui,.
                   //"questions" nu apartine universului playerului
                   goldCoins: Int = 0,
                   inPenaltyBox: Boolean = false,
                   isGettingOutOfPenaltyBox: Boolean = false // nu e necesar
                 )
