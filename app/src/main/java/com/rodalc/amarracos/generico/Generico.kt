package com.rodalc.amarracos.generico

import android.content.Context
import com.rodalc.amarracos.comun.Jugador
import com.rodalc.amarracos.storage.StateSaver
import com.rodalc.amarracos.storage.UndoStack
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Se encarga de gestionar los jugadores y guardar el progreso para poder volver o restaurar el estado de la partida anterior.
 */
object Generico : StateSaver("generico.json") {
    /**
     * Lista de jugadores actuales.
     */
    private var jugadores = mutableListOf(Jugador(1), Jugador(2))

    /**
     * Stack para poder deshacer acciones.
     * @see UndoStack
     */
    private var stack = UndoStack<UndoGenerico>()

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
        val temp = UndoGenerico(this.jugadores.map { it.copy() })
        this.stack.push(temp)
    }

    /**
     * Recupera el último estado de la partida a partir del stack.
     * Si hay un error (no hay nada que recuperar, por ejemplo) no hace nada.
     */
    fun popState() {
        val temp = this.stack.pop() ?: UndoGenerico(this.jugadores)
        this.jugadores = temp.jugadores.toMutableList()
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
     * Clase auxiliar para almacenar el estado de una partida.
     */
    private data class UndoGenerico(
        val jugadores: List<Jugador>
    )

    /**
     * Almacena en disco el estado de la partida (excepto el stack)
     *
     * @param context El contexto actual
     */
    override fun saveState(context: Context) {
        val json = Json.encodeToString(this.jugadores)
        context.openFileOutput(this.filename, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    /**
     * Recupera del disco la última partida, reiniciando el stack.
     *
     * @param context El contexto actual
     */
    override fun loadState(context: Context) {
        stack = UndoStack()
        try {
            val inputStream = context.openFileInput(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val jsonString = String(buffer, Charsets.UTF_8)
            this.jugadores = Json.decodeFromString<MutableList<Jugador>>(jsonString).toMutableList()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
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
        this.jugadores = mutableListOf(Jugador(1), Jugador(2))
        return this.deleteFile(context)
    }

    /**
     * Devuelve si se puedde cargar el estado de una partida anterior desde el disco o no.
     * TODO ¿Es necesario?
     *
     * @return Si se puedde cargar el estado de una partida anterior desde el disco o no
     */
    fun canLoadState(context: Context): Boolean {
        return this.fileExist(context)
    }

    /**
     * Actualiza la puntuación de todos los jugadores.
     *
     */
    fun actualizarPuntuacion() {
        for (jugador in this.jugadores) {
            jugador.actualizarPuntuacion()
        }
    }
}

