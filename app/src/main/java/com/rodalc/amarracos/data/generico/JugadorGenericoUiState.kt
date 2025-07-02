package com.rodalc.amarracos.data.generico

import kotlinx.serialization.Serializable

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
