package com.soho.sohoapp.live.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.soho.sohoapp.live.datastore.DataStoreKeys.PREF_KEY_LOGIN_STATE
import com.soho.sohoapp.live.model.ConnectedSocialProfile
import com.soho.sohoapp.live.model.SMProfile
import com.soho.sohoapp.live.network.response.Data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val APP_DATASTORE = "sohoLive-pref"
private val Context.dataStore by preferencesDataStore(APP_DATASTORE)

class AppDataStoreManager(private val context: Context) {

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

    //get and set FaceBook Profile
    suspend fun saveFBProfile(profile: SMProfile) {
        val jsonString = Json.encodeToString(profile)
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.PREF_KEY_USER_PROFILE_FB] = jsonString
        }
    }

    val facebookProfile: Flow<SMProfile?>
        get() = context.dataStore.data.map { preferences ->
            preferences[DataStoreKeys.PREF_KEY_USER_PROFILE_FB]?.let { jsonString ->
                Json.decodeFromString<SMProfile>(jsonString)
            }
        }


    //Save Connected SocialMedia Profile List
    suspend fun saveConnectedSMProfile(profileList: ConnectedSocialProfile) {
        val jsonString = Json.encodeToString(profileList)
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.PREF_KEY_USER_SM_PROFILES] = jsonString
        }
    }

    val connectedSMProfile: Flow<ConnectedSocialProfile?>
        get() = context.dataStore.data.map { preferences ->
            preferences[DataStoreKeys.PREF_KEY_USER_SM_PROFILES]?.let { jsonString ->
                Json.decodeFromString<ConnectedSocialProfile>(jsonString)
            }
        }
}