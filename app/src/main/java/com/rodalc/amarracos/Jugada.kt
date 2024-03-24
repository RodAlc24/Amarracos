package com.rodalc.amarracos

/**
 * Almacena quien ha ganado una ronda o si aún no se sabe
 */
enum class Ganador { BUENOS, MALOS, POR_VER }

/**
 * Almacena los distintos tipos de rondas
 */
enum class Ronda { MUS, GRANDE, CHICA, PARES, JUEGO, PUNTO }

/**
 * Almacena de que tipo son los pares
 */
enum class Pares { PAR, MEDIAS, DUPLES, NADA, POR_VER }

/**
 * Almacena de que tipo es el juego (o punto)
 */
enum class Juego { LA_UNA, JUEGO, PUNTO, NADA, POR_VER }

/**
 * Guarda la infrmación relacionada con una ronda de grande o chica
 */
data class ResultadoRonda(
    var ronda: Ronda,
    var envite: Int = 0,
    var ganador: Ganador = Ganador.POR_VER,
)

/**
 * Guarda la información de una ronda de pares
 */
data class ResultadoRondaPares(
    val ronda: Ronda = Ronda.PARES,
    var envite: Int = 0,
    var ganador: Ganador = Ganador.POR_VER,
    var juegoA: Pares = Pares.POR_VER,
    var juegoB: Pares = Pares.POR_VER
)

/**
 * Guarda la información de una ronda de juego (o punto)
 */
data class ResultadoRondaJuego(
    val ronda: Ronda = Ronda.JUEGO,
    var envite: Int = 0,
    var ganador: Ganador = Ganador.POR_VER,
    var juegoA: Juego = Juego.POR_VER,
    var juegoB: Juego = Juego.POR_VER
)

/**
 * Guarda la información de una jugada entera (grande, chica, pares y juego)
 */
class Jugada {
    var grande: ResultadoRonda = ResultadoRonda(Ronda.GRANDE)
    var chica: ResultadoRonda = ResultadoRonda(Ronda.CHICA)
    var pares: ResultadoRondaPares = ResultadoRondaPares()
    var juego: ResultadoRondaJuego = ResultadoRondaJuego()

    fun reiniciar() {
        grande = ResultadoRonda(Ronda.GRANDE)
        chica = ResultadoRonda(Ronda.CHICA)
        pares = ResultadoRondaPares()
        juego = ResultadoRondaJuego()
    }
}

data class Partida(
    var nombrePareja1: String = "Buenos",
    var nombrePareja2: String = "Malos",

    var puntosPareja1: Int = 0,
    var puntosPareja2: Int = 0,

    var juegosPareja1: Int = 0,
    var juegosPareja2: Int = 0,

    var rondaActual: Jugada = Jugada()
) {
    fun reiniciar() {
        rondaActual.reiniciar()
        puntosPareja1 = 0
        puntosPareja2 = 0
    }
}
