package com.rodalc.amarracos.data.tabs

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rodalc.amarracos.data.generico.JugadorGenericoUiState
import com.rodalc.amarracos.data.mus.MusDefaultConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for managing the state of all tabs displayed on the main screen.
 *
 * This ViewModel holds the UI state common to all tabs, such as whether the
 * game is played to 30 points, and the names of the "good" and "bad" teams.
 * It exposes this state as a [StateFlow] and provides methods to update it.
 */
class TabViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TabUiState())
    val uiState: StateFlow<TabUiState> = _uiState.asStateFlow()

    /**
     * Initializes the UI state with default values loaded from [MusDefaultConfig].
     *
     * This function loads the default settings for whether the game is played to 30 points,
     * and the default names for the "good" and "bad" teams, and updates the UI state accordingly.
     *
     * @param context The application context, used to access shared preferences for loading default configurations.
     */
    fun initialize(context: Context) {
        _uiState.update { currentState ->
            currentState.copy(
                puntos30 = MusDefaultConfig.load(context = context).puntos30,
                labelBuenos = MusDefaultConfig.load(context = context).nameBuenos,
                labelMalos = MusDefaultConfig.load(context = context).nameMalos
            )
        }
    }

    /**
     * Updates the UI state to reflect whether the Mus game is being played to 30 points.
     *
     * @param puntos30 True if the Mus game is set to be played to 30 points, false otherwise (e.g., 40 points).
     */
    fun setPuntos30(puntos30: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                puntos30 = puntos30
            )
        }
    }

    /**
     * Updates the name of the "good" team in the UI state.
     *
     * @param nombreBuenos The new name for the "good" team.
     */
    fun setNombreBuenos(nombreBuenos: String) {
        _uiState.update { currentState ->
            currentState.copy(
                nombreBuenos = nombreBuenos
            )
        }
    }

    /**
     * Sets the name of the "malos" team.
     *
     * @param nombreMalos The name of the "malos" team.
     */
    fun setNombreMalos(nombreMalos: String) {
        _uiState.update { currentState ->
            currentState.copy(
                nombreMalos = nombreMalos
            )
        }
    }

    /**
     * Removes the last player from the list of players for either the Pocha or Generico game.
     *
     * @param isPocha True if the player should be removed from the Pocha game, false for the Generico game.
     */
    fun removeLast(isPocha: Boolean) {
        val jugadores =
            if (isPocha) _uiState.value.jugadoresPocha else _uiState.value.jugadoresGenerico
        val updatedJugadores = jugadores.dropLast(1)

        _uiState.update { currentState ->
            currentState.copy(
                jugadoresPocha = if (isPocha) updatedJugadores else currentState.jugadoresPocha,
                jugadoresGenerico = if (!isPocha) updatedJugadores else currentState.jugadoresGenerico
            )
        }
    }

    /**
     * Adds a new player to the game.
     *
     * @param isPocha True if the player should be added to the Pocha game, false for the generic game.
     */
    fun addPlayer(isPocha: Boolean) {
        val jugadores =
            if (isPocha) _uiState.value.jugadoresPocha else _uiState.value.jugadoresGenerico
        val updatedJugadores = jugadores.plus(JugadorGenericoUiState(id = jugadores.size + 1))

        _uiState.update { currentState ->
            currentState.copy(
                jugadoresPocha = if (isPocha) updatedJugadores else currentState.jugadoresPocha,
                jugadoresGenerico = if (!isPocha) updatedJugadores else currentState.jugadoresGenerico
            )
        }
    }

    /**
     * Changes the name of a player in either the "Pocha" or "Generico" game.
     *
     * @param isPocha A boolean indicating whether the player is in the "Pocha" game (true) or "Generico" game (false).
     * @param id The ID of the player whose name is to be changed.
     * @param name The new name for the player.
     */
    fun changeName(isPocha: Boolean, id: Int, name: String) {
        val jugadores =
            if (isPocha) _uiState.value.jugadoresPocha else _uiState.value.jugadoresGenerico
        val updatedJugadores = jugadores.map { jugador ->
            if (jugador.id == id) {
                jugador.copy(nombre = name)
            } else {
                jugador
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                jugadoresPocha = if (isPocha) updatedJugadores else currentState.jugadoresPocha,
                jugadoresGenerico = if (!isPocha) updatedJugadores else currentState.jugadoresGenerico
            )
        }
    }
}