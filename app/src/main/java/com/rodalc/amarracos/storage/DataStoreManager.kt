package com.rodalc.amarracos.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extension property to provide a [DataStore] instance for [Preferences] specific to the application.
 * The data store is named "settings".
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Singleton object for managing application settings using DataStore.
 */
object DataStoreManager {
    /**
     * Enum representing the keys for preferences stored in DataStore.
     */
    enum class Key {
        /**
         * Key for the "Keep Screen On" preference.
         */
        KEEP_SCREEN_ON,
    }

    /**
     * Reads a boolean value from DataStore based on the provided [key].
     *
     * @param context The application context.
     * @param key The [Key] for the preference to read.
     * @return A [Flow] emitting the boolean value of the preference. Defaults to `false` if the key is not found.
     */
    fun readDataStore(context: Context, key: Key): Flow<Boolean> {
        return context.dataStore.data.map { it[booleanPreferencesKey(key.name)] != false }
    }

    /**
     * Writes a boolean [value] to DataStore for the given [key].
     * This is a suspend function and should be called from a coroutine.
     *
     * @param context The application context.
     * @param key The [Key] for the preference to write.
     * @param value The boolean value to store.
     */
    suspend fun setDataStore(context: Context, key: Key, value: Boolean) {
        context.dataStore.edit { settings ->
            settings[booleanPreferencesKey(key.name)] = value
        }
    }
}