package com.rodalc.amarracos.pocha

import com.rodalc.amarracos.storage.UndoStack

object Pocha {
    private var jugadores = mutableListOf(Jugador(0), Jugador(1));
    private var stack = UndoStack<List<Jugador>>()

    fun getJugadores(): List<Jugador> {
        return this.jugadores
    }

    fun setJugadores(jugadores: List<Jugador>) {
        this.jugadores = jugadores.toMutableList()
    }

    fun pushState() {
        stack.push(this.jugadores.map {it.copy()})
    }

    fun popState() {
        this.jugadores = stack.pop()?.toMutableList() ?: this.jugadores
    }

    fun canUndo(): Boolean {
        return this.stack.size() > 0
    }

    fun actualizarPuntuacion(duplica: Boolean) {
        for (jugador in this.jugadores) {
            jugador.actualizarPuntuacion(duplica)
        }
    }
}

