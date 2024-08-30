package com.soho.sohoapp.live.datastore

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.soho.sohoapp.live.datastore.DataStoreKeys.PREF_KEY_LOGIN_STATE
import com.soho.sohoapp.live.datastore.DataStoreKeys.PREF_KEY_USER_SM_PROFILES
import com.soho.sohoapp.live.model.ConnectedSocialProfile
import com.soho.sohoapp.live.network.response.Data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val APP_DATASTORE = "sohoLive-pref"
private val Context.dataStore by preferencesDataStore(APP_DATASTORE)


class AppDataStoreManager(private val context: Context) {

    //sharedPref
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(APP_DATASTORE, Context.MODE_PRIVATE)

    fun saveSMProfileList(smProfileList: ConnectedSocialProfile?) {
        val jsonString = Json.encodeToString(smProfileList)
        val editor = sharedPref.edit()
        editor.putString(PREF_KEY_USER_SM_PROFILES, jsonString)
        editor.apply()
    }

    fun getSMProfileList(): ConnectedSocialProfile? {
        val jsonString = sharedPref.getString(PREF_KEY_USER_SM_PROFILES, null) ?: return null
        return Json.decodeFromString(jsonString)
    }
    //sharedPref End

    //get and set login state
    val loginState: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[PREF_KEY_LOGIN_STATE] ?: false
        }

    suspend fun setLoginState(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PREF_KEY_LOGIN_STATE] = isLoggedIn
        }
    }

    //get and set user profile
    suspend fun saveUserProfile(userProfile: Data) {
        val jsonString = Json.encodeToString(userProfile)
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.PREF_KEY_USER_PROFILE] = jsonString
        }
    }

    val userProfile: Flow<Data?>
        get() = context.dataStore.data.map { preferences ->
            preferences[DataStoreKeys.PREF_KEY_USER_PROFILE]?.let { jsonString ->
                Json.decodeFromString<Data>(jsonString)
            }
        }

    // Clear all data on logout
    suspend fun clearAllData() {
        //sharedPref.edit().clear().apply()
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}