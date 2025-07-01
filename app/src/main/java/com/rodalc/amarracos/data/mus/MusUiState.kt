package com.rodalc.amarracos.data.mus

/**
 * Data class representing the state of a Mus game.
 *
 * @property nombreBuenos The name of the "Buenos" team.
 * @property nombreMalos The name of the "Malos" team.
 * @property puntosBuenos The current points of the "Buenos" team.
 * @property puntosMalos The current points of the "Malos" team.
 * @property juegosBuenos The current games won by the "Buenos" team.
 * @property juegosMalos The current games won by the "Malos" team.
 * @property enviteGrande The current bet for the "Grande" phase.
 * @property enviteChica The current bet for the "Chica" phase.
 * @property envitePares The current bet for the "Pares" phase.
 * @property enviteJuego The current bet for the "Juego" phase.
 * @property puntosParaGanar The number of points required to win a game.
 */
data class MusUiState(
    val nombreBuenos: String = "",
    val nombreMalos: String = "",
    val puntosBuenos: Int = 0,
    val puntosMalos: Int = 0,
    val juegosBuenos: Int = 0,
    val juegosMalos: Int = 0,
    val enviteGrande: Int = 0,
    val enviteChica: Int = 0,
    val envitePares: Int = 0,
    val enviteJuego: Int = 0,
    val puntosParaGanar: Int = 30
)
