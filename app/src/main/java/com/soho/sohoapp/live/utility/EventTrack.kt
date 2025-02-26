package com.soho.sohoapp.live.utility

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.soho.sohoapp.live.SohoLiveApp.Companion.context

enum class Event { user_logged_in }

/*@Composable
fun TrackLogin(email: String) {
    LaunchedEffect(Unit) {
        val params = mapOf("email" to email)
        recordEvent(Event.user_logged_in, params)
    }
}*/

fun TrackLogin(email: String) {
    val params = mapOf("email" to email)
    recordEvent(Event.user_logged_in, params)
}

//Event Track
private fun recordEvent(event: Event, parameters: Map<String, Any?>) {
    val bundle = Bundle()

    for ((key, value) in parameters) {
        when (value) {
            is String -> bundle.putString(key, value)
            is Int -> bundle.putInt(key, value)
            is Double -> bundle.putDouble(key, value)
            is Boolean -> bundle.putBoolean(key, value)
        }
    }

    FirebaseAnalytics.getInstance(context).logEvent(
        event.toString().lowercase(), bundle
    )

    Log.i("TrackEvent", event.toString() + " :: " + Gson().toJson(bundle))
}