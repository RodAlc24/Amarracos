package com.rodalc.amarracos.comun

import kotlinx.serialization.Serializable
import kotlin.math.abs


/**
 * Representa un jugador, con información relevante como ID, nombre, puntos e incremento.
 *
 * @property id Identificador único del jugador.
 * @property nombre Nombre del jugador.
 * @property puntos Puntos acumulados por el jugador.
 * @property incremento Puntos ganados por el jugador en la ronda actual.
 * @property apuesta Apuesta realizada por el jugador en la ronda actual.
 * @property victoria Número de manos ganadas en la ronda actual.
 * @property historicoPuntos Mapa que contiene el historial de puntos del jugador.
 *
 * @see Ronda
 */
@Serializable
data class Jugador(
    val id: Int,
    var nombre: String = "",
    var puntos: Int = 0,
    var apuesta: Int = 0,
    var victoria: Int = 0,
    var incremento: Int = 0,
    var historicoPuntos: Map<Int, Int> = mapOf(0 to 0)
) {
    /**
     * Actualiza la puntuación del jugador.
     *
     * Se el suma a puntos el incremento.
     *
     */
    fun actualizarPuntuacion(pocha: Boolean = false, duplica: Boolean = false) {
        if (pocha) {
            this.incremento = if (this.apuesta == this.victoria) 10 + 5 * this.apuesta else -5 * abs(this.apuesta - this.victoria)
            if (duplica) this.incremento *= 2
        }
        this.puntos += this.incremento
        this.incremento = 0
        this.apuesta = 0
        this.victoria = 0
        this.historicoPuntos = this.historicoPuntos + mapOf(this.historicoPuntos.size to this.puntos)
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
