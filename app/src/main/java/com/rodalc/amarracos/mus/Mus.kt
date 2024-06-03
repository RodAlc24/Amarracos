package com.rodalc.amarracos.mus

import android.content.Context
import com.rodalc.amarracos.storage.StateSaver
import com.rodalc.amarracos.storage.UndoStack
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException
import java.io.IOException

object Mus : StateSaver("mus.json") {
    private var buenos: Pareja = Pareja("Buenos")
    private var malos: Pareja = Pareja("Malos")
    private var envites: Envites = Envites()
    private var puntos = 30

    private var stack = UndoStack<List<Any>>()

    fun getBuenos(): Pareja {
        return this.buenos
    }

    fun setBuenos(buenos: Pareja) {
        this.buenos = buenos
    }

    fun getMalos(): Pareja {
        return this.malos
    }

    fun setMalos(malos: Pareja) {
        this.malos = malos
    }

    fun getPuntos(): Int {
        return this.puntos
    }

    fun setPuntos(puntos: Int) {
        this.puntos = puntos
    }

    fun getEnvites(): Envites {
        return this.envites
    }

    fun setEnvites(envites: Envites) {
        this.envites = envites
    }

    fun reset() {
        this.buenos = Pareja("Buenos")
        this.malos = Pareja("Malos")
        this.envites = Envites()
        this.puntos = 30
    }

    /**
     * Almacena en el stack el estado actual de la partida.
     */
    fun pushState() {
        this.stack.push(
            listOf(
                this.buenos.copy(),
                this.malos.copy(),
                this.envites.copy(),
                this.puntos
            )
        )
    }

    /**
     * Recupera el último estado de la partida a partir del stack.
     * Si hay un error (no hay nada que recuperar, por ejemplo) no hace nada.
     */
    fun popState() {
        val temp = this.stack.pop()
        this.buenos = (temp?.get(0) ?: Pareja("Buenos")) as Pareja
        this.malos = (temp?.get(1) ?: Pareja("Malos")) as Pareja
        this.envites = (temp?.get(2) ?: Envites()) as Envites
        this.puntos = (temp?.get(3) ?: 30) as Int
    }

    /**
     * Indica si se puede deshacer o no.
     *
     * @return Si se puede deshacer o no
     */
    fun canUndo(): Boolean {
        return this.stack.size() > 0
    }

    fun deleteStack() {
        this.stack = UndoStack()
    }

    override fun loadState(context: Context) {
        stack = UndoStack()
        try {
            val inputStream = context.openFileInput(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val jsonString = String(buffer, Charsets.UTF_8)
            val temp = Json.decodeFromString<SerialicerMus>(jsonString)
            this.buenos = temp.buenos
            this.malos = temp.malos
            this.envites = temp.envites
            this.puntos = temp.puntos
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun saveState(context: Context) {
        val temp = SerialicerMus(
            buenos = this.buenos,
            malos = this.malos,
            envites = this.envites,
            puntos = this.puntos
        )
        val json =
            Json.encodeToString(temp)
        println(json)
        context.openFileOutput(this.filename, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
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
     * Elimina la copia del disco de la última partida.
     * Reinicia el stack
     *
     * @param context El contexto actual
     * @return Si se ha eliminado o no
     */
    fun discardBackup(context: Context): Boolean {
        this.stack = UndoStack()
        return this.deleteFile(context)
    }
}
