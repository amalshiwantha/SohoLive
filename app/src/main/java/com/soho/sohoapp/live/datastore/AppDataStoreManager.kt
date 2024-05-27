package com.soho.sohoapp.live.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.soho.sohoapp.live.datastore.DataStoreKeys.PREF_KEY_LOGIN_STATE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val APP_DATASTORE = "sohoLive-pref"
private val Context.dataStore by preferencesDataStore(APP_DATASTORE)

class AppDataStoreManager(private val context: Context) {

    val loginState: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[PREF_KEY_LOGIN_STATE] ?: false
        }

    suspend fun setLoginState(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PREF_KEY_LOGIN_STATE] = isLoggedIn
        }
    }
}