package com.rodalc.amarracos.mus

/**
 * Almacena quien ha ganado una ronda o si aún no se sabe.
 */
enum class Ganador { BUENOS, MALOS, POR_VER }

/**
 * Almacena los distintos tipos de rondas.
 */
enum class Ronda { GRANDE, CHICA, PARES, JUEGO, CONTEO }

/**
 * Almacena de que tipo son los pares.
 */
enum class Pares { PAR, MEDIAS, DUPLES, NADA, POR_VER }

/**
 * Almacena de que tipo es el juego (o punto).
 */
enum class Juego { LA_UNA, JUEGO, NADA, POR_VER }

/**
 * Guarda la infrmación relacionada con una ronda de grande o chica.
 */
data class ResultadoRonda(
    var ronda: Ronda,
    var envite: Int = 0,
    var ganador: Ganador = Ganador.POR_VER,
)

/**
 * Guarda la información de una ronda de pares.
 */
data class ResultadoRondaPares(
    val ronda: Ronda = Ronda.PARES,
    var envite: Int = 0,
    var ganador: Ganador = Ganador.POR_VER,
    var juegoA: Pares = Pares.POR_VER,
    var juegoB: Pares = Pares.POR_VER
)

/**
 * Guarda la información de una ronda de juego (o punto).
 */
data class ResultadoRondaJuego(
    val ronda: Ronda = Ronda.JUEGO,
    var envite: Int = 0,
    var ganador: Ganador = Ganador.POR_VER,
    var juegoA: Juego = Juego.POR_VER,
    var juegoB: Juego = Juego.POR_VER
)

/**
 * Guarda la información de una jugada entera (grande, chica, pares y juego).
 */
class Jugada {
    var grande: ResultadoRonda = ResultadoRonda(Ronda.GRANDE)
    var chica: ResultadoRonda = ResultadoRonda(Ronda.CHICA)
    var pares: ResultadoRondaPares = ResultadoRondaPares()
    var juego: ResultadoRondaJuego = ResultadoRondaJuego()

    /**
     * Reinicia todos los datos excepto los nombres y los juegos (ronda nueva).
     */
    fun reiniciar() {
        grande = ResultadoRonda(Ronda.GRANDE)
        chica = ResultadoRonda(Ronda.CHICA)
        pares = ResultadoRondaPares()
        juego = ResultadoRondaJuego()
    }

    /**
     * Devuelve el envite de la ronda solicitada.
     *
     * @param ronda La ronda de la que se quiere obtener el envite
     * @return El valor del envite en esa ronda
     */
    fun getEnvite(ronda: Ronda): Int {
        return when (ronda) {
            Ronda.GRANDE -> grande.envite
            Ronda.CHICA -> chica.envite
            Ronda.PARES -> pares.envite
            else -> juego.envite
        }
    }

    /**
     * Cambia el envite de una ronda.
     *
     * @param ronda La ronda de la que se va a poner el envite
     * @param envite El valor del envite
     */
    fun setEnvite(ronda: Ronda, envite: Int) {
        when (ronda) {
            Ronda.GRANDE -> grande.envite = envite
            Ronda.CHICA -> chica.envite = envite
            Ronda.PARES -> pares.envite = envite
            else -> juego.envite = envite
        }
    }

    /**
     * Asigna un ganador a una ronda.
     *
     * @param ronda La ronda seleccionada
     * @param ganador El ganador de esa ronda
     */
    fun setGanador(ronda: Ronda, ganador: Ganador) {
        when (ronda) {
            Ronda.GRANDE -> grande.ganador = ganador
            Ronda.CHICA -> chica.ganador = ganador
            Ronda.PARES -> pares.ganador = ganador
            else -> juego.ganador = ganador
        }
    }

    /**
     * Devuelve el ganador de una ronda solicitada.
     *
     * @param ronda La ronda de la que se quiere el ganador
     * @return El ganador de esa ronda
     */
    fun getGanador(ronda: Ronda): Ganador {
        val ganador = when (ronda) {
            Ronda.GRANDE -> grande.ganador
            Ronda.CHICA -> chica.ganador
            Ronda.PARES -> pares.ganador
            else -> juego.ganador
        }
        return ganador
    }

}

/**
 * Clase para almacenar toda la información de una ronda.
 */
data class Partida(
    var nombrePareja1: String = "Buenos",
    var nombrePareja2: String = "Malos",

    var puntosPareja1: Int = 0,
    var puntosPareja2: Int = 0,

    var juegosPareja1: Int = 0,
    var juegosPareja2: Int = 0,

    var rondaActual: Jugada = Jugada(),
    var aux: Int = 0
) {
    fun reiniciar() {
        rondaActual.reiniciar()
        puntosPareja1 = 0
        puntosPareja2 = 0
    }
}
