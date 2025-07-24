package com.rodalc.amarracos.data.generico

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rodalc.amarracos.R
import com.rodalc.amarracos.data.generico.GenericoViewModel.PointType.APUESTA
import com.rodalc.amarracos.data.generico.GenericoViewModel.PointType.INCREMENTO
import com.rodalc.amarracos.data.generico.GenericoViewModel.PointType.TOTAL
import com.rodalc.amarracos.data.generico.GenericoViewModel.PointType.VICTORIA
import com.rodalc.amarracos.data.generico.GenericoViewModel.SortType.ID
import com.rodalc.amarracos.data.generico.GenericoViewModel.SortType.NAME
import com.rodalc.amarracos.data.generico.GenericoViewModel.SortType.POINTS
import com.rodalc.amarracos.storage.StateSaverManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import kotlin.math.abs

/**
 * ViewModel for managing the state of a generic game.
 *
 * This ViewModel is responsible for the business logic of a generic game, which can be configured
 * for different game types (e.g., "Pocha"). It manages the game state, including player information,
 * scores, rounds, and game-specific rules like "duplica".
 *
 * Key functionalities include:
 * - Starting a new game with a list of players.
 * - Updating player points based on different point types (total, bet, win, increment).
 * - Managing game rounds and applying points according to round-specific rules.
 * - Handling "duplica" state, which can modify point calculations.
 * - Saving and loading game state to persist progress.
 * - Providing undo functionality to revert the last action.
 * - Sorting players based on different criteria (name, points, ID).
 */
class GenericoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GenericoUiState())
    val uiState = _uiState.asStateFlow()

    private val undoStack = mutableListOf<GenericoUiState>()

    private val _canUndo = MutableStateFlow(undoStack.isNotEmpty())
    val canUndo = _canUndo.asStateFlow()

    private val _apuestas = MutableStateFlow(0)
    val apuestas = _apuestas.asStateFlow()

    private val _sortMethod = MutableStateFlow(ID)
    val sortMethod = _sortMethod.asStateFlow()

    private val filenamePocha = "pocha2.json"
    private val filenameGenerico = "generico2.json"

    /**
     * Starts the game with the given list of players.
     *
     * This function initializes the game state with the provided list of players.
     * If a player's name is blank, it assigns a default name in the format "Jugador {id}".
     *
     * @param jugadores A list of [JugadorGenericoUiState] objects representing the players.
     * @param context The Android context required for saving the state.
     * @param isPocha A boolean indicating if the current game is a "Pocha" game.
     */
    fun startGame(
        jugadores: List<JugadorGenericoUiState>,
        context: Context,
        isPocha: Boolean = false
    ) {
        val updatedJugadores = jugadores.map { jugador ->
            jugador.copy(nombre = jugador.nombre.ifBlank { "Jugador ${jugador.id}" })
        }
        _uiState.update { currentState ->
            GenericoUiState(
                jugadores = updatedJugadores,
                isPocha = isPocha
            )
        }
        updateApuestas()
        sortPlayers()
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
     * @param newPoints The number of points to add (can be negative for subtraction).
     * @param pointType The type of points to update, as defined by [PointType].
     * @param context The Android context required for saving the state.
     */
    fun updatePoints(jugadorId: Int, newPoints: Int, pointType: PointType, context: Context) {
        val newJugador = _uiState.value.jugadores.find { it.id == jugadorId }
        if (newJugador != null) {
            val updatedJugador = newJugador.copy(
                apuesta = if (pointType == APUESTA) newPoints else newJugador.apuesta,
                victoria = if (pointType == VICTORIA) newPoints else newJugador.victoria,
                incremento = if (pointType == INCREMENTO) newPoints else newJugador.incremento,
                puntos = if (pointType == TOTAL) newPoints else newJugador.puntos
            )

            val updatedJugadores = _uiState.value.jugadores.map { jugador ->
                if (jugador.id == jugadorId) {
                    updatedJugador
                } else {
                    jugador
                }
            }
            //pushUndo()
            _uiState.update { currentState ->
                currentState.copy(jugadores = updatedJugadores)
            }
            updateApuestas()
            sortPlayers()
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
     * @param isPocha A boolean indicating if the current game is a "Pocha" game. In "Pocha", scoring rules might differ, and rounds can be split into betting and playing phases.
     * @param context The Android context required for saving the state.
     */
    fun changeRound(isPocha: Boolean, context: Context) {
        val rondaApuestas = if (isPocha) !_uiState.value.rondaApuestas else true
        var updatedJugadores = _uiState.value.jugadores
        var duplica = _uiState.value.duplica

        pushUndo()
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
        updateApuestas()
        sortPlayers()
        saveState(context = context)
    }

    /**
     * Applies points to a player based on their bets and victories.
     *
     * @param jugador The player to apply points to.
     * @param duplica A boolean indicating whether to double the points (specific to "Pocha" rules).
     * @param isPocha A boolean indicating if the current game is a "Pocha" game. This affects
     *                how points are calculated (e.g., bonus for matching bets and victories).
     * @return The updated [JugadorGenericoUiState] with the new points and updated history.
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

        if (increment > _uiState.value.playOfTheGame) {
            _uiState.update { currentState ->
                currentState.copy(
                    playOfTheGame = increment,
                    potgPlayer = jugador.nombre
                )
            }
        }

        return jugador.copy(
            apuesta = 0,
            victoria = 0,
            incremento = 0,
            puntos = jugador.puntos + increment,
            historicoPuntos = jugador.historicoPuntos.plus(
                Pair(
                    jugador.historicoPuntos.size,
                    jugador.puntos + increment
                )
            )
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
        //pushUndo()
        _uiState.update { currentState ->
            currentState.copy(duplica = duplica)
        }
        updateApuestas()
        sortPlayers()
        saveState(context = context)
    }

    /**
     * Checks if the sum of all players' bets equals the sum of all players' victories.
     * This is typically used in games like "Pocha" to ensure that the total bets match the total wins.
     *
     * @return `true` if the sum of bets equals the sum of victories, `false` otherwise.
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
     * The game state is serialized to JSON and saved to a file named $filenamePocha if it's a "Pocha" game,
     * or $filenameGenerico if otherwise.
     *
     * @param context The Android context required for file operations.
     */
    private fun saveState(context: Context) {
        val json = Json.encodeToString(_uiState.value)
        StateSaverManager.writteFile(
            filename = if (_uiState.value.isPocha) filenamePocha else filenameGenerico,
            context = context,
            content = json
        )
    }

    /**
     * Loads the game state from a file.
     *
     * This function reads the game state from a JSON file ($filenamePocha or $filenameGenerico)
     * and updates the UI state with the loaded data.
     *
     * @param context The Android context required for reading the file.
     * @param isPocha A boolean indicating whether to load the "Pocha" game state or the generic game state.
     */
    fun loadState(context: Context, isPocha: Boolean) {
        val temp = StateSaverManager.readFile(
            filename = if (isPocha) filenamePocha else filenameGenerico,
            context = context
        )
        if (temp != null) {
            _uiState.update {
                Json.decodeFromString(temp)
            }
            updateApuestas()
            sortPlayers()
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
     *                If true, it checks for $filenamePocha; otherwise, it checks for $filenameGenerico.
     * @return `true` if a saved game state file exists, `false` otherwise.
     */
    fun canLoadState(context: Context, isPocha: Boolean): Boolean {
        return StateSaverManager.fileExists(
            filename = if (isPocha) filenamePocha else filenameGenerico,
            context = context
        )
    }

    /**
     * Pushes the current UI state onto the undo stack.
     *
     * This function saves the current state of the UI, allowing it to be restored later
     * by the `undo` function. It also updates the `_canUndo` flag to reflect whether
     * there are any states in the undo stack.
     */
    private fun pushUndo() {
        undoStack.add(uiState.value)
        _canUndo.update { undoStack.isNotEmpty() }
    }

    /**
     * Undoes the last action performed in the game.
     *
     * This function reverts the game state to the state before the last action was taken.
     * It uses an undo stack to store previous game states. If the undo stack is not empty,
     * the last state is popped from the stack and applied as the current game state.
     * After undoing, the current state is saved, and the `canUndo` flag is updated.
     *
     * @param context The Android context required for saving the state after undoing.
     */
    fun undo(context: Context) {
        if (_canUndo.value) {
            _uiState.update {
                undoStack.removeAt(undoStack.lastIndex)
            }
            updateApuestas()
            sortPlayers()
            saveState(context = context)
            _canUndo.update { undoStack.isNotEmpty() }
        }
    }

    /**
     * Enum class representing the different criteria for sorting players.
     *
     * - [NAME]: Sorts players alphabetically by their name.
     * - [POINTS]: Sorts players by their current points, typically in descending order.
     * - [ID]: Sorts players by their unique identifier, often used for maintaining a consistent order.
     */
    enum class SortType(val stringId: Int) {
        NAME(stringId = R.string.text_name_order),
        POINTS(stringId = R.string.text_points_order),
        ID(stringId = R.string.text_id_order)
    }

    /**
     * Sorts the list of players based on the current sort method.
     *
     * This function sorts the players in the current UI state according to the `sortMethod` value.
     * The sorting can be done by player name, points, or ID.
     * If sorting by points, the order is reversed (descending).
     * After sorting, the UI state is updated with the new sorted list of players.
     */
    private fun sortPlayers() {
        val sortedJugadores = when (sortMethod.value) {
            NAME -> _uiState.value.jugadores.sortedBy { it.nombre }
            POINTS -> _uiState.value.jugadores.sortedWith(
                compareByDescending<JugadorGenericoUiState> { it.puntos }
                    .thenBy { it.nombre }
            )

            ID -> _uiState.value.jugadores.sortedBy { it.id }
        }

        _uiState.update { currentState ->
            currentState.copy(jugadores = sortedJugadores)
        }
    }

    /**
     * Sets the sort method for the player list and re-sorts the players.
     *
     * This function updates the `_sortMethod` state with the provided `sortType`
     * and then calls `sortPlayers()` to apply the new sorting order to the player list.
     *
     * @param sortType The [SortType] to be used for sorting the players (e.g., NAME, POINTS, ID).
     */
    fun setSortMethod(sortType: SortType) {
        _sortMethod.update { sortType }
        sortPlayers()
    }

    /**
     * Updates the total sum of bets (_apuestas) from all players.
     *
     * This function calculates the sum of the 'apuesta' (bet) property for each player
     * in the current UI state and updates the `_apuestas` MutableStateFlow with this sum.
     * This is useful for displaying the total amount bet in the current round or game.
     */
    private fun updateApuestas() {
        _apuestas.update {
            _uiState.value.jugadores.sumOf { it.apuesta }
        }
    }
}
