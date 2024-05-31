package com.rodalc.amarracos.storage

import android.content.Context
import com.rodalc.amarracos.pocha.Jugador
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Un objeto que proporciona métodos para guardar y cargar el estado de un juego.
 *
 * Este objeto se encarga de manejar la persistencia de datos de los jugadores en un archivo JSON.
 * De momento solo tiene un archivo, para la pocha. En el futuro se generalizará.
 */
object StateSaver {
    private var filename = "pocha.json"

    /**
     * Verifica si el archivo de estado existe en el contexto dado.
     *
     * @param context El contexto de la aplicación o actividad.
     * @return true si el archivo existe, false en caso contrario.
     */
    fun fileExist(context: Context): Boolean {
        val file = context.getFileStreamPath(filename)
        return file != null && file.exists()
    }

    /**
     * Elimina el archivo de estado en el contexto dado.
     *
     * @param context El contexto de la aplicación o actividad.
     * @return true si el archivo se eliminó con éxito, false en caso contrario.
     */
    fun deleteFile(context: Context): Boolean {
        return context.deleteFile(filename)
    }

    /**
     * Guarda el estado de los jugadores en un archivo JSON.
     *
     * @param context El contexto de la aplicación o actividad.
     * @param jugadores La lista de jugadores a guardar.
     */
    fun savePocha(context: Context, jugadores: List<Jugador>) {
        val json = Json.encodeToString(jugadores)
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    /**
     * Carga el estado de los jugadores desde un archivo JSON.
     *
     * @param context El contexto de la aplicación o actividad.
     * @return La lista de jugadores cargados, o una lista vacía si el archivo no existe o hay un error al leerlo.
     */
    fun loadPocha(context: Context): List<Jugador>? {
        var jugadores: List<Jugador>? = null

        try {
            val inputStream = context.openFileInput(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val jsonString = String(buffer, Charsets.UTF_8)
            jugadores = Json.decodeFromString(jsonString)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return jugadores
    }
}