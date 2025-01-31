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
    enum class Key {
        KEEP_SCREEN_ON,
        MUS_A_30,
    }

    fun readDataStore(context: Context, key: Key): Flow<Boolean> {
        return context.dataStore.data.map { it[booleanPreferencesKey(key.name)] != false }
    }

    suspend fun setDataStore(context: Context, key: Key, value: Boolean) {
        context.dataStore.edit { settings ->
            settings[booleanPreferencesKey(key.name)] = value
        }
    }
}