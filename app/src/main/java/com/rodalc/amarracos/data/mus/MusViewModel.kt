package com.rodalc.amarracos.data.mus

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the Mus game.
 *
 * This ViewModel manages the state of the Mus game, including player names, scores, and envites.
 * It provides functions to start the game, update envites, and determine the winner of a game.
 */
class MusViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MusUiState())
    val uiState: StateFlow<MusUiState> = _uiState.asStateFlow()

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
     * @param puntosParaGanar The number of points required to win the game.
     */
    fun startGame(nombreBuenos: String, nombreMalos: String, puntosParaGanar: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                nombreBuenos = nombreBuenos,
                nombreMalos = nombreMalos,
                puntosParaGanar = puntosParaGanar
            )
        }
    }

    /**
     * Updates the game state when a team wins a game.
     *
     * This function increments the game count for the winning team, resets the points for both teams,
     * and resets all envite values to 0.
     *
     * @param winner The team that won the game.
     */
    fun ganadorJuego(winner: Teams) {
        var juegosBuenos = uiState.value.juegosBuenos
        var juegosMalos = uiState.value.juegosMalos

        if (winner == Teams.BUENOS) {
            juegosBuenos += 1
        } else {
            juegosMalos += 1
        }

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
     */
    fun updateEnvites(envite: Envites, team: Teams) {
        var increment = 0
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

        if (team == Teams.BUENOS) {
            puntosBuenos += increment
        } else {
            puntosMalos += increment
        }

        if (puntosBuenos >= uiState.value.puntosParaGanar) {
            ganadorJuego(Teams.BUENOS)
        } else if (puntosMalos >= uiState.value.puntosParaGanar) {
            ganadorJuego(Teams.MALOS)
        } else {
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
        }
    }
}