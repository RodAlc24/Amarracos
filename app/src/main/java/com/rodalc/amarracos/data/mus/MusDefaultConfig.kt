package com.rodalc.amarracos.data.mus

import android.content.Context
import com.rodalc.amarracos.storage.StateSaverManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object MusDefaultConfig {
    private const val FILENAME = "mus_default.json"

    @Serializable
    data class DefaultMus(
        val nameBuenos: String = "Buenos",
        val nameMalos: String = "Malos",
        val puntos30: Boolean = true,
    )

    fun load(context: Context): DefaultMus {
        val temp = StateSaverManager.readFile(filename = FILENAME, context = context)
        return if (temp != null) {
            Json.decodeFromString(temp)
        } else {
            DefaultMus()
        }
    }

    fun save(context: Context, defaultMus: DefaultMus) {
        StateSaverManager.writteFile(
            filename = FILENAME,
            context = context,
            content = Json.encodeToString(defaultMus)
        )
    }
}