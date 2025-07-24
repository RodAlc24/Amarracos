package com.rodalc.amarracos.data.generico

import kotlinx.serialization.Serializable

/**
 * Represents the UI state for the Generico game mode.
 *
 * @property jugadores A list of [JugadorGenericoUiState] objects, representing the players in the game. Defaults to a list with two players (id 1 and 2).
 * @property duplica A boolean indicating whether the "duplica" rule is active. Defaults to false.
 * @property rondaApuestas A boolean indicating whether it is currently the betting round. Defaults to true.
 * @property isPocha A boolean indicating whether the current game is a "Pocha" variant. Defaults to false.
 * @property playOfTheGame The biggest increment of points in the game. Defaults to 0.
 * @property potgPlayer The ID of the player with the biggest increment. Defaults to 0.
 */
@Serializable
data class GenericoUiState(
    val jugadores: List<JugadorGenericoUiState> = listOf(
        JugadorGenericoUiState(id = 1),
        JugadorGenericoUiState(id = 2)
    ),
    val duplica: Boolean = false,
    val rondaApuestas: Boolean = true,
    val isPocha: Boolean = false,
    val playOfTheGame: Int = 0,
    val potgPlayer: String = ""
)