package com.rodalc.amarracos.pocha

import kotlinx.serialization.Serializable


@Serializable
data class Jugador(
    val id: Int,
    var nombre: String = "",
    var punots: Int = 0,
    var apuesta: Int = 0,
    var victoria: Int = 0,
) {
}

enum class Ronda {
    NOMBRES,
    APUESTAS,
    CONTEO
}