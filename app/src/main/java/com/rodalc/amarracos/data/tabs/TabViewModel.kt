package com.rodalc.amarracos.data.tabs

import androidx.lifecycle.ViewModel
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
}