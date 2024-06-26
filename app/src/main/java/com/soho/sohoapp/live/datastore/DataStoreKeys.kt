package com.soho.sohoapp.live.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    val PREF_KEY_LOGIN_STATE = booleanPreferencesKey("login_state")
    val PREF_KEY_USER_PROFILE = stringPreferencesKey("user_profile")
    val PREF_KEY_USER_PROFILE_FB = stringPreferencesKey("user_fb_profile")
}