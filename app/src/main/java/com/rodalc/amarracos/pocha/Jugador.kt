package com.rodalc.amarracos.pocha

import com.rodalc.amarracos.pocha.Ronda.APUESTAS
import com.rodalc.amarracos.pocha.Ronda.CONTEO
import com.rodalc.amarracos.pocha.Ronda.NOMBRES
import kotlinx.serialization.Serializable
import kotlin.math.abs


/**
 * Representa un jugador de la pocha, con información relevante como ID, nombre, puntos, apuesta y victorias.
 *
 * @property id Identificador único del jugador.
 * @property nombre Nombre del jugador.
 * @property puntos Puntos acumulados por el jugador.
 * @property apuesta Apuesta realizada por el jugador en la ronda actual.
 * @property victoria Número de manos ganadas en la ronda actual.
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
) {
    fun actualizarPuntuacion(duplica: Boolean) {
        val incremento = if (this.apuesta == this.victoria) 10 + 5 * this.apuesta else -5 * abs(this.apuesta - this.victoria)
        puntos += (if (duplica) 2 else 1) * incremento
        this.apuesta = 0
        this.victoria = 0
    }
    override fun toString(): String {
        return if (nombre == "") "Jugador ${this.id}" else this.nombre
    }
}

/**
 * Define las rondas del juego.
 *
 * @property NOMBRES Ronda donde los jugadores ingresan sus nombres.
 * @property APUESTAS Ronda donde los jugadores realizan sus apuestas.
 * @property CONTEO Ronda final donde se cuenta el resultado.
 */
enum class Ronda {
    NOMBRES,
    APUESTAS,
    CONTEO
}