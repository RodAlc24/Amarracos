package com.rodalc.amarracos.data.generico

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rodalc.amarracos.storage.StateSaverManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import kotlin.math.abs

/**
 * ViewModel for managing the state of a generic game.
 *
 * This ViewModel handles the logic for starting a game, updating player points,
 * changing rounds, applying points based on game rules, and managing "duplica" state.
 * It exposes the game state through a [StateFlow] of [GenericoUiState].
 */
class GenericoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GenericoUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Starts the game with the given list of players.
     *
     * This function initializes the game state with the provided list of players.
     * If a player's name is blank, it assigns a default name in the format "Jugador {id}".
     *
     * @param jugadores A list of [JugadorGenericoUiState] objects representing the players in the game.
     * @param context The Android context required for saving the state.
     * @param isPocha A boolean indicating if the current game is a "Pocha" game.
     */
    fun startGame(jugadores: List<JugadorGenericoUiState>, context: Context, isPocha: Boolean = false) {
        val updatedJugadores = jugadores.map { jugador ->
            jugador.copy(nombre = jugador.nombre.ifBlank { "Jugador ${jugador.id}" })
        }
        _uiState.update { currentState ->
            currentState.copy(
                jugadores = updatedJugadores,
                isPocha = isPocha
            )
        }
        saveState(context = context)
    }

    /**
     * Enum class representing the different types of points that can be updated for a player.
     *
     * - [TOTAL]: Represents the total points of a player.
     * - [APUESTA]: Represents the points a player has bet.
     * - [VICTORIA]: Represents the points a player has won.
     * - [INCREMENTO]: Represents the points to be added or subtracted from the total points.
     */
    enum class PointType {
        TOTAL,
        APUESTA,
        VICTORIA,
        INCREMENTO
    }

    /**
     * Updates the points of a specific player.
     *
     * @param jugadorId The ID of the player whose points are to be updated.
     * @param newPoints The number of points to add.
     * @param pointType The type of points to update (TOTAL, APUESTA, VICTORIA, INCREMENTO).
     * @param context The Android context required for saving the state.
     */
    fun updatePoints(jugadorId: Int, newPoints: Int, pointType: PointType, context: Context) {
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
            saveState(context = context)
        }
    }

    /**
     * Changes the current round of the game.
     *
     * This function updates the game state to reflect the transition to a new round.
     * It handles the application of points based on whether the game is a "Pocha" game
     * and whether the current round is for betting or playing.
     *
     * @param isPocha A boolean indicating if the current game is a "Pocha" game.
     *                In Pocha, scoring rules might differ.
     */
    fun changeRound(isPocha: Boolean, context: Context) {
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
        saveState(context = context)
    }

    /**
     * Applies points to a player based on their bets and victories.
     *
     * @param jugador The player to apply points to.
     * @param duplica Whether to double the points.
     * @param isPocha Whether the current round is a "Pocha" round.
     * @return The updated player with the new points.
     */
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

    /**
     * Sets the value of the 'duplica' flag in the UI state.
     * This flag typically indicates if points should be doubled in the current game context.
     *
     * @param duplica A boolean value indicating whether the 'duplica' flag should be set to true or false.
     * @param context The Android context required for saving the state.
     */
    fun setDuplica(duplica: Boolean, context: Context) {
        _uiState.update { currentState ->
            currentState.copy(duplica = duplica)
        }
        saveState(context = context)
    }

    /**
     * Comprueba si la suma de las apuestas de todos los jugadores es igual a la suma de las victorias de todos los jugadores.
     *
     * @return True si la suma de las apuestas es igual a la suma de las victorias, False en caso contrario.
     */
    fun apuestasEqualVictorias(): Boolean {
        var value = 0

        uiState.value.jugadores.forEach {
            value += it.apuesta - it.victoria
        }

        return value == 0
    }

    /**
     * Saves the current game state to a file.
     *
     * The game state is serialized to JSON and saved to a file named "pocha.json" if it's a "Pocha" game,
     * or "generico.json" otherwise.
     *
     * @param context The Android context required for file operations.
     */
    private fun saveState(context: Context) {
        val json = Json.encodeToString(_uiState.value)
        StateSaverManager.writteFile(filename = if (_uiState.value.isPocha) "pocha.json" else "generico.json", context = context, content = json)
    }

    /**
     * Loads the game state from a file.
     *
     * This function reads the game state from a JSON file ("pocha.json" or "generico.json")
     * and updates the UI state with the loaded data.
     *
     * @param context The Android context required for reading the file.
     * @param isPocha A boolean indicating whether to load the "Pocha" game state or the generic game state.
     */
    fun loadState(context: Context, isPocha: Boolean) {
        val temp = StateSaverManager.readFile(filename = if (isPocha) "pocha.json" else "generico.json", context = context)
        if (temp != null) {
            _uiState.update {
                Json.decodeFromString(temp)
            }
        }
    }

    /**
     * Checks if a saved game state can be loaded.
     *
     * This function determines whether a saved game state file exists for either a "Pocha" game
     * or a generic game, based on the `isPocha` parameter.
     *
     * @param context The Android context used to access the file system.
     * @param isPocha A boolean indicating if the game to check is a "Pocha" game.
     *                If true, it checks for "pocha.json"; otherwise, it checks for "generico.json".
     * @return `true` if a saved game state file exists, `false` otherwise.
     */
    fun canLoadState(context: Context, isPocha: Boolean): Boolean {
        return StateSaverManager.fileExists(filename = if (isPocha) "pocha.json" else "generico.json", context = context)
    }

    enum class SortType {
        NAME,
        POINTS,
        ID
    }

    fun sortPlayersBy(sortType: SortType) {
        val sortedJugadores = _uiState.value.jugadores.sortedWith(compareBy { jugador ->
            when (sortType) {
                SortType.NAME -> jugador.nombre
                SortType.POINTS -> jugador.puntos
                SortType.ID -> jugador.id
            }
        })
        _uiState.update { currentState ->
            currentState.copy(jugadores = sortedJugadores)
        }
    }
}
