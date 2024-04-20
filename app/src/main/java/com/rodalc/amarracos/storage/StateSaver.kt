package com.rodalc.amarracos.storage

import android.content.Context
import com.rodalc.amarracos.pocha.Jugador
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException
import java.io.IOException

fun savePocha(context: Context, jugadores: List<Jugador>) {
    val filename = "myfile"
    val json = Json.encodeToString(jugadores)
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(json.toByteArray())
    }
}

fun loadPocha(context: Context): List<Jugador>? {
    val filename = "myfile"
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