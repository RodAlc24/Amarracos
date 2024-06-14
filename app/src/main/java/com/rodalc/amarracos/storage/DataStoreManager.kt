package com.rodalc.amarracos.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object DataStoreManager {
    public enum class Keys(private val keyName: String) {
        KEEP_SCREEN_ON("keep_screen_on"),
        MUS_A_30("mus_a_30");

        val booleanKey: Preferences.Key<Boolean> = booleanPreferencesKey(keyName)
    }

    fun readDataStore(context: Context, key: Keys): Flow<Boolean> {
        return context.dataStore.data.map { it[key.booleanKey] ?: true }
    }

    suspend fun setDataStore(context: Context, key: Keys, value: Boolean) {
        context.dataStore.edit { settings ->
            settings[key.booleanKey] = value
        }
    }
}