package com.rodalc.amarracos.pocha

import android.content.Context
import com.rodalc.amarracos.storage.StateSaver
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
        stack.push(this.jugadores.map { it.copy() })
    }

    fun popState() {
        this.jugadores = stack.pop()?.toMutableList() ?: this.jugadores
    }

    fun canUndo(): Boolean {
        return this.stack.size() > 0
    }

    fun save(context: Context) {
        StateSaver.savePocha(context, this.jugadores)
    }

    fun load(context: Context) {
        stack = UndoStack()
        this.jugadores = StateSaver.loadPocha(context).toMutableList()
    }

    fun discardBackup(context: Context): Boolean {
        stack = UndoStack()
        this.jugadores = mutableListOf(Jugador(0), Jugador(1));
        return StateSaver.deleteFile(context)
    }

    fun canLoad(context: Context): Boolean {
        return StateSaver.fileExist(context)
    }

    fun actualizarPuntuacion(duplica: Boolean) {
        for (jugador in this.jugadores) {
            jugador.actualizarPuntuacion(duplica)
        }
    }
}

