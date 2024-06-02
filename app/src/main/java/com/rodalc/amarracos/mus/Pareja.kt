package com.rodalc.amarracos.mus


data class Pareja(
    var nombre: String,
    var puntos: Int = 0,
    var victorias: Int = 0
)

data class Envites(
    var grande: Int = 0,
    var chica: Int = 0,
    var pares: Int = 0,
    var juego: Int = 0,
) {
    fun getEnvite(ronda: RondasMus): Int {
        return when (ronda) {
            RondasMus.GRANDE -> this.grande
            RondasMus.CHICA -> this.chica
            RondasMus.PARES -> this.pares
            RondasMus.JUEGO -> this.juego
        }
    }

    fun setEnvite(ronda: RondasMus, envite: Int) {
        when (ronda) {
            RondasMus.GRANDE -> this.grande = envite
            RondasMus.CHICA -> this.chica = envite
            RondasMus.PARES -> this.pares = envite
            RondasMus.JUEGO -> this.juego = envite
        }
    }
}

enum class RondasMus {
    GRANDE,
    CHICA,
    PARES,
    JUEGO
}
