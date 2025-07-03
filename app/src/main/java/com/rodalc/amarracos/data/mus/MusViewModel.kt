package com.rodalc.amarracos.data.mus

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rodalc.amarracos.data.mus.MusViewModel.Teams.BUENOS
import com.rodalc.amarracos.data.mus.MusViewModel.Teams.MALOS
import com.rodalc.amarracos.storage.StateSaverManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

/**
 * ViewModel for the Mus game.
 *
 * This ViewModel manages the state of the Mus game, including player names, scores, and envites.
 * It provides functions to start the game, update envites, and determine the winner of a game.
 */
class MusViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MusUiState())
    val uiState: StateFlow<MusUiState> = _uiState.asStateFlow()

    private val undoStack = mutableListOf<MusUiState>()

    private val _canUndo = MutableStateFlow(undoStack.isNotEmpty())
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val filename = "mus2.json"

    /**
     * Represents the different types of bets (envites) in a game of Mus.
     * Each enum constant corresponds to a specific betting phase of the game.
     */
    enum class Envites {
        GRANDE,
        CHICA,
        PARES,
        JUEGO
    }

    /**
     * Enum class representing the teams in a Mus game.
     *
     * @property BUENOS Represents the "good" team.
     * @property MALOS Represents the "bad" team.
     */
    enum class Teams {
        BUENOS,
        MALOS
    }

    /**
     * Starts a new game with the given team names and points to win.
     *
     * @param nombreBuenos The name of the "good" team.
     * @param nombreMalos The name of the "bad" team.
     * @param puntos30 Indicates whether 30 points should be used for winning the game.
     * @param context The Android context required for saving the state.
     */
    fun startGame(nombreBuenos: String, nombreMalos: String, puntos30: Boolean, context: Context) {
        _uiState.update { currentState ->
            currentState.copy(
                nombreBuenos = nombreBuenos,
                nombreMalos = nombreMalos,
                puntosParaGanar = if (puntos30) 30 else 40
            )
        }
        saveState(context = context)
    }

    /**
     * Updates the game state when a team wins a game.
     *
     * This function increments the game count for the winning team, resets the points for both teams,
     * and resets all envite values to 0.
     *
     * @param winner The team that won the game.
     * @param context The Android context required for saving the state.
     */
    fun ganadorJuego(winner: Teams, context: Context) {
        var juegosBuenos = uiState.value.juegosBuenos
        var juegosMalos = uiState.value.juegosMalos

        if (winner == BUENOS) {
            juegosBuenos += 1
        } else {
            juegosMalos += 1
        }

        pushUndo()
        _uiState.update { currentState ->
            currentState.copy(
                puntosBuenos = 0,
                puntosMalos = 0,
                juegosBuenos = juegosBuenos,
                juegosMalos = juegosMalos,
                enviteGrande = 0,
                enviteChica = 0,
                envitePares = 0,
                enviteJuego = 0,
            )
        }
        saveState(context = context)
    }

    /**
     * Updates the score based on the result of an envite (a bet in Mus).
     *
     * This function takes the type of envite and the winning team as input.
     * It retrieves the current value of the specified envite from the UI state,
     * adds that value to the winning team's score, and then resets the envite's value to 0.
     *
     * After updating the scores, it checks if either team has reached the points needed to win the game.
     * If a team has won, it calls the `ganadorJuego` function.
     * Otherwise, it updates the UI state with the new scores and reset envite values.
     *
     * @param envite The type of envite that was won (e.g., GRANDE, CHICA).
     * @param team The team that won the envite (BUENOS or MALOS).
     * @param context The Android context required for saving the state.
     */
    fun updateEnvite(envite: Envites, team: Teams, context: Context) {
        var increment: Int
        var enviteGrande = uiState.value.enviteGrande
        var enviteChica = uiState.value.enviteChica
        var envitePares = uiState.value.envitePares
        var enviteJuego = uiState.value.enviteJuego
        var puntosBuenos = uiState.value.puntosBuenos
        var puntosMalos = uiState.value.puntosMalos

        when (envite) {
            Envites.GRANDE -> {
                increment = enviteGrande
                enviteGrande = 0
            }

            Envites.CHICA -> {
                increment = enviteChica
                enviteChica = 0
            }

            Envites.PARES -> {
                increment = envitePares
                envitePares = 0
            }

            Envites.JUEGO -> {
                increment = enviteJuego
                enviteJuego = 0
            }
        }

        if (team == BUENOS) {
            puntosBuenos += increment
        } else {
            puntosMalos += increment
        }

        if (puntosBuenos >= uiState.value.puntosParaGanar) {
            ganadorJuego(winner = BUENOS, context = context)
        } else if (puntosMalos >= uiState.value.puntosParaGanar) {
            ganadorJuego(winner = MALOS, context = context)
        } else {
            pushUndo()
            _uiState.update { currentState ->
                currentState.copy(
                    puntosBuenos = puntosBuenos,
                    puntosMalos = puntosMalos,
                    enviteGrande = enviteGrande,
                    enviteChica = enviteChica,
                    envitePares = envitePares,
                    enviteJuego = enviteJuego,
                )
            }
            saveState(context = context)
        }
    }

    /**
     * Increments the value of a specific envite (bet) by a given amount.
     *
     * This function updates the UI state by adding the `increment` value
     * to the current value of the specified `envite`.
     *
     * @param envite The type of envite to increment (e.g., GRANDE, CHICA).
     * @param increment The amount by which to increment the envite's value. Defaults to 1.
     * @param context The Android context required for saving the state.
     */
    fun incrementEnvite(envite: Envites, increment: Int = 1, context: Context) {
        pushUndo()
        _uiState.update { currentState ->
            currentState.copy(
                enviteGrande = currentState.enviteGrande + if (envite == Envites.GRANDE) increment else 0,
                enviteChica = currentState.enviteChica + if (envite == Envites.CHICA) increment else 0,
                envitePares = currentState.envitePares + if (envite == Envites.PARES) increment else 0,
                enviteJuego = currentState.enviteJuego + if (envite == Envites.JUEGO) increment else 0,
            )
        }
        saveState(context = context)
    }

    /**
     * Increments the points for a specified team.
     *
     * This function adds the given increment (defaulting to 1) to the points of the specified team.
     * After updating the points, it checks if either team has reached the points needed to win the game.
     * If a team has won, it calls the `ganadorJuego` function.
     * Otherwise, it updates the UI state with the new scores.
     *
     * @param team The team whose points are to be incremented (BUENOS or MALOS).
     * @param increment The number of points to add (defaults to 1).
     * @param context The Android context required for saving the state.
     */
    fun incrementarPuntos(team: Teams, increment: Int = 1, context: Context) {
        val puntosBuenos = uiState.value.puntosBuenos + if (team == BUENOS) increment else 0
        val puntosMalos = uiState.value.puntosMalos + if (team == MALOS) increment else 0

        if (puntosBuenos >= uiState.value.puntosParaGanar) {
            ganadorJuego(winner = BUENOS, context = context)
        } else if (puntosMalos >= uiState.value.puntosParaGanar) {
            ganadorJuego(winner = MALOS, context = context)
        } else {
            pushUndo()
            _uiState.update { currentState ->
                currentState.copy(
                    puntosBuenos = puntosBuenos,
                    puntosMalos = puntosMalos
                )
            }
            saveState(context = context)
        }
    }

    /**
     * Saves the current UI state to a JSON file.
     *
     * This function serializes the current `_uiState` value into a JSON string
     * and then writes it to a file named $filename using the `StateSaverManager`.
     *
     * @param context The Android context required for file operations.
     */
    private fun saveState(context: Context) {
        val json = Json.encodeToString(_uiState.value)
        StateSaverManager.writteFile(filename = filename, content = json, context = context)
    }

    /**
     * Loads the game state from a file.
     *
     * This function reads the game state from a JSON file named $filename.
     * If the file exists and contains valid data, the UI state is updated
     * with the loaded game state.
     *
     * @param context The Android context required for reading the file.
     */
    fun loadState(context: Context) {
        val temp = StateSaverManager.readFile(filename = filename, context = context)
        if (temp != null) {
            _uiState.update {
                Json.decodeFromString(temp)
            }
        }
    }

    /**
     * Checks if a saved game state file exists.
     *
     * This function uses the `StateSaverManager` to determine if a file named $filename
     * exists in the application's storage. This file is expected to contain the saved
     * state of a previous Mus game.
     *
     * @param context The Android context, used by `StateSaverManager` to access application storage.
     * @return `true` if the saved game state file exists, `false` otherwise.
     */
    fun canLoadState(context: Context): Boolean {
        return StateSaverManager.fileExists(filename = filename, context = context)
    }

    /**
     * Pushes the current UI state onto the undo stack.
     *
     * This function is typically called before making a change to the UI state
     * that might need to be undone. It adds a copy of the current `uiState.value`
     * to the `undoStack` list.
     */
    private fun pushUndo() {
        undoStack.add(uiState.value)
        _canUndo.update { undoStack.isNotEmpty() }
    }

    /**
     * Reverts the game state to the previous state stored in the undo stack.
     *
     * This function checks if there are any states in the `undoStack`.
     * If the stack is not empty, it removes the last state from the stack and updates
     * the `_uiState` to this reverted state. After updating the UI state,
     * it calls `saveState` to persist the changes.
     *
     * @param context The Android context required for saving the state after reverting.
     */
    fun undo(context: Context) {
        if (_canUndo.value) {
            _uiState.update {
                undoStack.removeAt(undoStack.lastIndex)
            }
            saveState(context = context)
            _canUndo.update { undoStack.isNotEmpty() }
        }
    }
}