package com.rodalc.amarracos.data.generico

import kotlinx.serialization.Serializable

/**
 * Represents the UI state for a generic player.
 *
 * @property id The unique identifier of the player.
 * @property nombre The name of the player.
 * @property puntos The current points of the player.
 * @property apuesta The current bet of the player.
 * @property victoria The current victory of the player.
 * @property incremento The point increment in the current round.
 * @property historicoPuntos A map storing the player's point history, where the key is the round number and the value is the points in that round.
 */
@Serializable
data class JugadorGenericoUiState(
    val id: Int = 1,
    val nombre: String = "",
    val puntos: Int = 0,
    val apuesta: Int = 0,
    val victoria: Int = 0,
    val incremento: Int = 0,
    val historicoPuntos: Map<Int, Int> = mapOf(0 to 0)
)
