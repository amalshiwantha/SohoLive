package com.soho.sohoapp.live.utility

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.soho.sohoapp.live.SohoLiveApp.Companion.context

enum class Event { user_logged_in, password_reset_requested, asset_step1_next_clicked }

/*@Composable
fun TrackLogin(email: String) {
    LaunchedEffect(Unit) {
        val params = mapOf("email" to email)
        recordEvent(Event.user_logged_in, params)
    }
}*/

fun TrackStep1(
    property_listing_id: Int,
    property_state: String,
    property_type: String,
    step: Int,
) {
    val params = mapOf(
        "property_listing_id" to property_listing_id,
        "property_state" to property_state,
        "property_type" to property_type,
        "step" to step,
    )
    recordEvent(Event.asset_step1_next_clicked, params)
}

fun TrackPwResetRequest(email: String) {
    val params = mapOf("email" to email)
    recordEvent(Event.password_reset_requested, params)
}

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