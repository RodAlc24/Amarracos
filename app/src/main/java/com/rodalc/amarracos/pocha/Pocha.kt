package com.rodalc.amarracos.pocha

import android.content.Context
import com.rodalc.amarracos.storage.StateSaver
import com.rodalc.amarracos.storage.UndoStack
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Se encarga de gestionar los jugadores y guardar el progreso para poder volver o restaurar el estado de la partida anterior.
 */
object Pocha : StateSaver("pocha.json") {
    /**
     * Lista de jugadores actuales.
     */
    private var jugadores = mutableListOf(JugadorPocha(1), JugadorPocha(2))

    /**
     * Si duplica la ronda actual o no.
     */
    private var duplica = false

    /**
     * Stack para poder deshacer acciones.
     * @see UndoStack
     */
    private var stack = UndoStack<UndoMus>()

    /**
     * Devuelve la lista actual de jugadores.
     *
     * @return La lista actual de jugadores
     */
    fun getJugadores(): List<JugadorPocha> {
        return this.jugadores
    }

    /**
     * Permite cambiar la lista de jugadores por otra.
     *
     * @param jugadores Los nuevos jugadores
     */
    fun setJugadores(jugadores: List<JugadorPocha>) {
        this.jugadores = jugadores.toMutableList()
    }

    /**
     * Devuelve si duplica o no la ronda actual.
     *
     * @return Si duplica la ronda actual
     */
    fun getDuplica(): Boolean {
        return this.duplica
    }

    /**
     * Indica si duplica o no la ronda actual.
     *
     * @param duplica Si duplica la ronda actual
     */
    fun setDuplica(duplica: Boolean) {
        this.duplica = duplica
    }

    /**
     * Almacena en el stack el estado actual de la partida.
     */
    fun pushState() {
        val temp = UndoMus(this.duplica, this.jugadores.map { it.copy() })
        this.stack.push(temp)
    }

    /**
     * Recupera el último estado de la partida a partir del stack.
     * Si hay un error (no hay nada que recuperar, por ejemplo) no hace nada.
     */
    fun popState() {
        val temp = this.stack.pop() ?: UndoMus(this.duplica, this.jugadores)
        this.jugadores = temp.jugadores.toMutableList()
        this.duplica = temp.duplica
    }

    /**
     * Indica si se puede deshacer o no.
     *
     * @return Si se puede deshacer o no
     */
    fun canUndo(): Boolean {
        return this.stack.size() > 0
    }

    private data class UndoMus(
        val duplica: Boolean,
        val jugadores: List<JugadorPocha>
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
            this.jugadores = Json.decodeFromString<MutableList<JugadorPocha>>(jsonString).toMutableList()
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
        this.jugadores = mutableListOf(JugadorPocha(1), JugadorPocha(2))
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
     * @param duplica Si se duplican los puntos o no
     */
    fun actualizarPuntuacion(duplica: Boolean) {
        for (jugador in this.jugadores) {
            jugador.actualizarPuntuacion(duplica)
        }
    }

    /**
     * Indica si se puede continuar con la partida, es decir, las apuestas no coinciden con el número de rondas.
     *
     * @return true si se puede continuar, false en caso contrario
     */
    fun canContinue(): Boolean {
        var puntos = 0
        for (jugador in this.jugadores) {
            puntos += jugador.apuesta - jugador.victoria
        }
        return puntos != 0
    }

    /**
     * Devuelve la suma total de las apuestas.
     *
     * @return La suma total de las apuestas
     */
    fun getTotalApuestas(): Int {
        var apuestas = 0
        for (jugador in this.jugadores) {
            apuestas += jugador.apuesta
        }
        return apuestas
    }

}

