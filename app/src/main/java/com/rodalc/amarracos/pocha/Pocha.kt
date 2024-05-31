package com.rodalc.amarracos.pocha

import android.content.Context
import com.rodalc.amarracos.storage.StateSaver
import com.rodalc.amarracos.storage.UndoStack

/**
 * Se encarga de gestionar los jugadores y guardar el progreso para poder volver o restaurar el estado de la partida anterior.
 */
object Pocha {
    /**
     * Lista de jugadores actuales.
     */
    private var jugadores = mutableListOf(Jugador(0), Jugador(1))

    /**
     * Stack para poder deshacer acciones.
     * @see UndoStack
     */
    private var stack = UndoStack<List<Jugador>>()

    /**
     * Devuelve la lista actual de jugadores.
     *
     * @return La lista actual de jugadores
     */
    fun getJugadores(): List<Jugador> {
        return this.jugadores
    }

    /**
     * Permite cambiar la lista de jugadores por otra.
     *
     * @param jugadores Los nuevos jugadores
     */
    fun setJugadores(jugadores: List<Jugador>) {
        this.jugadores = jugadores.toMutableList()
    }

    /**
     * Almacena en el stack el estado actual de la partida.
     */
    fun pushState() {
        stack.push(this.jugadores.map { it.copy() })
    }

    /**
     * Recupera el último estado de la partida a partir del stack.
     * Si hay un error (no hay nada que recuperar, por ejemplo) no hace nada.
     */
    fun popState() {
        this.jugadores = stack.pop()?.toMutableList() ?: this.jugadores
    }

    /**
     * Indica si se puede deshacer o no.
     *
     * @return Si se puede deshacer o no
     */
    fun canUndo(): Boolean {
        return this.stack.size() > 0
    }

    /**
     * Almacena en disco el estado de la partida (excepto el stack)
     *
     * @param context El contexto actual
     */
    fun save(context: Context) {
        StateSaver.savePocha(context, this.jugadores)
    }

    /**
     * Recupera del disco la última partida, reiniciando el stack.
     *
     * @param context El contexto actual
     */
    fun load(context: Context) {
        stack = UndoStack()
        this.jugadores =
            StateSaver.loadPocha(context)?.toMutableList() ?: mutableListOf(Jugador(0), Jugador(1))
    }

    /**
     * Elimina la copia del disco de la última partida.
     * Reinicia el stack y los jugadores.
     *
     * @param context El contexto actual
     * @return Si se ha eliminado o no
     */
    fun discardBackup(context: Context): Boolean {
        stack = UndoStack()
        this.jugadores = mutableListOf(Jugador(0), Jugador(1))
        return StateSaver.deleteFile(context)
    }

    /**
     * Devuelve si se puedde cargar el estado de una partida anterior desde el disco o no.
     *
     * @return Si se puedde cargar el estado de una partida anterior desde el disco o no
     */
    fun canLoad(context: Context): Boolean {
        return StateSaver.fileExist(context)
    }

    /**
     * Actualiza la puntuación de todos los jugadores.
     *
     * @param duplica Si se duplican los puntos o no
     */
    fun actualizarPuntuacion(duplica: Boolean) {
        for (jugador in this.jugadores) {
            jugador.actualizarPuntuacion(duplica)
        }
    }
}

