package com.rodalc.amarracos.comun

import android.content.Context
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Se encarga de gestionar los jugadores y guardar el progreso para poder volver o restaurar el estado de la partida anterior.
 */
object Generico {
    /**
     * Lista de jugadores actuales.
     */
    private var jugadores = mutableListOf(Jugador(1), Jugador(2))

    /** * Devuelve la lista actual de jugadores.
     *
     * @return La lista actual de jugadores
     */
    fun getJugadores(): List<Jugador> {
        return this.jugadores
    }
}