package com.rodalc.amarracos.pocha


import java.util.concurrent.atomic.AtomicInteger

data class Jugador(
    var nombre: String = "Jugador ${id.incrementAndGet()}",
    var punots: Int = 0,
    var apuesta: Int = 0,
    var victoria: Int = 0
) {
    companion object {
        private val id = AtomicInteger(0)
    }
}

enum class Ronda {
    NOMBRES,
    APUESTAS,
    CONTEO
}