package com.rodalc.amarracos.data.tabs

import com.rodalc.amarracos.data.generico.JugadorGenericoUiState

/**
 * Represents the UI state for the tabs.
 *
 * @property nombreBuenos The name of the "Good" team.
 * @property nombreMalos The name of the "Bad" team.
 * @property puntos30 Indicates whether the game is played to 30 points (true) or another value (false).
 * @property jugadoresPocha A list of player states for the Pocha game.
 * @property jugadoresGenerico A list of player states for the generic game.
 *
 */
data class TabUiState(
    val nombreBuenos: String = "",
    val nombreMalos: String = "",
    val labelBuenos: String = "Buenos",
    val labelMalos: String = "Malos",
    val puntos30: Boolean = true,
    val jugadoresPocha: List<JugadorGenericoUiState> = listOf(
        JugadorGenericoUiState(id = 1),
        JugadorGenericoUiState(id = 2)
    ),
    val jugadoresGenerico: List<JugadorGenericoUiState> = listOf(
        JugadorGenericoUiState(id = 1),
        JugadorGenericoUiState(id = 2)
    )
)