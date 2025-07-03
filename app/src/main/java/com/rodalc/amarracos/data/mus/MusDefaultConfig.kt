package com.rodalc.amarracos.data.mus

import android.content.Context
import com.rodalc.amarracos.storage.StateSaverManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Object responsible for managing the default Mus game configuration.
 * It handles loading and saving the default settings to a JSON file.
 */
object MusDefaultConfig {
    private const val FILENAME = "mus_default.json"

    /**
     * Data class representing the default Mus game settings.
     *
     * @property nameBuenos The default name for the "good" team. Defaults to "Buenos".
     * @property nameMalos The default name for the "bad" team. Defaults to "Malos".
     * @property puntos30 A boolean indicating whether the game is played to 30 points (true) or 40 points (false). Defaults to true.
     */
    @Serializable
    data class DefaultMus(
        val nameBuenos: String = "Buenos",
        val nameMalos: String = "Malos",
        val puntos30: Boolean = true,
    )

    /**
     * Loads the default Mus game configuration from the JSON file.
     * If the file doesn't exist or an error occurs during reading, it returns a [DefaultMus] object with default values.
     */
    fun load(context: Context): DefaultMus {
        val temp = StateSaverManager.readFile(filename = FILENAME, context = context)
        return if (temp != null) {
            Json.decodeFromString(temp)
        } else {
            DefaultMus()
        }
    }

    /**
     * Saves the provided [DefaultMus] configuration to the JSON file.
     *
     * @param context The application context.
     * @param defaultMus The [DefaultMus] object to save.
     */
    fun save(context: Context, defaultMus: DefaultMus) {
        StateSaverManager.writteFile(
            filename = FILENAME,
            context = context,
            content = Json.encodeToString(defaultMus)
        )
    }
}