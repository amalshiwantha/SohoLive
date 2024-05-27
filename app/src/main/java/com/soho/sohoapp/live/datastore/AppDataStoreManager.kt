package com.soho.sohoapp.live.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

const val APP_DATASTORE = "com.soho.sohoapp.live"

class AppDataStoreManager(private val context: Context) : AppDataStore {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(APP_DATASTORE)

    override suspend fun setValue(
        key: String, `object`: String
    ) {
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = `object`
        }
    }

    override suspend fun readValue(
        key: String,
    ): String {
        return context.dataStore.data.first()[stringPreferencesKey(key)] ?: ""
    }
}