package com.soho.sohoapp.live.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.soho.sohoapp.live.datastore.DataStoreKeys.PREF_KEY_LOGIN
import kotlinx.coroutines.flow.first

const val APP_DATASTORE = "soholive"
private val Context.dataStore by preferencesDataStore(APP_DATASTORE)

class AppDataStoreManager(context: Context) {
    private val dataStore = context.dataStore

    suspend fun setLoginState(isLogged: Boolean) {
        dataStore.edit {
            it[PREF_KEY_LOGIN] = isLogged
        }
    }

    suspend fun readLoginState(): Boolean {
        return dataStore.data.first()[PREF_KEY_LOGIN] ?: false
    }
}