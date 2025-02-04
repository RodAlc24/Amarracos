package com.rodalc.amarracos.generico

import kotlinx.serialization.Serializable


/**
 * Representa un jugador, con información relevante como ID, nombre, puntos e incremento.
 *
 * @property id Identificador único del jugador.
 * @property nombre Nombre del jugador.
 * @property puntos Puntos acumulados por el jugador.
 * @property incremento Puntos ganados por el jugador en la ronda actual.
 *
 * @see Ronda
 */
@Serializable
data class Jugador(
    val id: Int,
    var nombre: String = "",
    var puntos: Int = 0,
    var incremento: Int = 0,
) {
    /**
     * Actualiza la puntuación del jugador.
     *
     * Se el suma a puntos el incremento.
     *
     */
    fun actualizarPuntuacion() {
        this.puntos += this.incremento
        this.incremento = 0
    }

    /**
     * Devuelve el nombre del jugador o "jugador id" si no tiene nombre.
     *
     * @return El nombre del jugador o "jugador id" si no tiene nombre
     */
    override fun toString(): String {
        return if (nombre == "") "Jugador ${this.id}" else this.nombre
    }
}

/**
 * Define las rondas del juego.
 *
 * @property NOMBRES los jugadores ingresan sus nombres.
 * @property JUEGO se muestran las puntuaciones de los jugadores.
 * @property CONTEO se guardan los puntos ganados en la ronda.
 */
enum class Ronda {
    NOMBRES,
    JUEGO,
    CONTEO
}