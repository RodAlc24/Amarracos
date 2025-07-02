package com.rodalc.amarracos.data.generico

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.abs

class GenericoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GenericoUiState())
    val uiState = _uiState.asStateFlow()

    fun startGame(jugadores: List<JugadorGenericoUiState>) {
        val updatedJugadores = jugadores.map { jugador ->
            jugador.copy(nombre = jugador.nombre.ifBlank { "Jugador ${jugador.id}" })
        }
        _uiState.value = GenericoUiState(jugadores = updatedJugadores)
    }

    enum class PointType {
        TOTAL,
        APUESTA,
        VICTORIA,
        INCREMENTO
    }

    enum class STATE {
        APUESTAS,
        VICTORIAS
    }

    fun updatePoints(jugadorId: Int, newPoints: Int, pointType: PointType) {
        val newJugador = _uiState.value.jugadores.find { it.id == jugadorId }
        if (newJugador != null) {
            val updatedJugador = newJugador.copy(
                apuesta = newJugador.apuesta + if (pointType == PointType.APUESTA) newPoints else 0,
                victoria = newJugador.victoria + if (pointType == PointType.VICTORIA) newPoints else 0,
                incremento = newJugador.incremento + if (pointType == PointType.INCREMENTO) newPoints else 0,
                puntos = newJugador.puntos + if (pointType == PointType.TOTAL) newPoints else 0
            )

            val updatedJugadores = _uiState.value.jugadores.map { jugador ->
                if (jugador.id == jugadorId) {
                    updatedJugador
                } else {
                    jugador
                }
            }
            _uiState.update { currentState ->
                currentState.copy(jugadores = updatedJugadores)
            }
        }
    }

    fun changeRound(isPocha: Boolean) {
        val rondaApuestas = !_uiState.value.rondaApuestas
        var updatedJugadores = _uiState.value.jugadores
        var duplica = _uiState.value.duplica

        if (!isPocha || !_uiState.value.rondaApuestas) {
            updatedJugadores = _uiState.value.jugadores.map { jugador ->
                applyPoints(jugador = jugador, duplica = duplica, isPocha = isPocha)
            }
            duplica = false
        }

        _uiState.update { currentState ->
            currentState.copy(
                rondaApuestas = rondaApuestas,
                jugadores = updatedJugadores,
                duplica = duplica
            )
        }
    }

    private fun applyPoints(
        jugador: JugadorGenericoUiState,
        duplica: Boolean,
        isPocha: Boolean
    ): JugadorGenericoUiState {
        var increment =
            if (!isPocha)
                jugador.incremento
            else
                if (jugador.apuesta == jugador.victoria)
                    (10 + 5 * jugador.apuesta)
                else
                    (-5 * abs(jugador.apuesta - jugador.victoria))

        if (duplica && isPocha)
            increment *= 2

        return jugador.copy(
            apuesta = 0,
            victoria = 0,
            incremento = 0,
            puntos = jugador.puntos + increment
        )

    }

    fun setDuplica(duplica: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(duplica = duplica)
        }
    }

    fun apuestasEqualVictorias(): Boolean {
        var value = 0

        uiState.value.jugadores.forEach {
            value += it.apuesta - it.victoria
        }

        return value == 0
    }
}
