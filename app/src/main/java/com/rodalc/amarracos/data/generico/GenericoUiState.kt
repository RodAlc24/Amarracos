package com.rodalc.amarracos.data.generico

import kotlinx.serialization.Serializable

@Serializable
data class GenericoUiState(
    val jugadores: List<JugadorGenericoUiState> = listOf(JugadorGenericoUiState(id = 1), JugadorGenericoUiState(id = 2)),
    val duplica: Boolean = false,
    val rondaApuestas: Boolean = true,
)