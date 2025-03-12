package com.soho.sohoapp.live.utility

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.enums.LiveFormat
import com.soho.sohoapp.live.enums.Orientation

enum class Event {
    user_logged_in, password_reset_requested,
    asset_step1_next_clicked, asset_step2_next_clicked,
    asset_step3_next_clicked, asset_step4_preview_clicked,
    live_stream_preview_created, cancel_live_stream_preview
}


fun TrackLiveStreamCancel(streamId: String) {
    val params = mapOf("stream_id" to streamId)
    recordEvent(Event.cancel_live_stream_preview, params)
}

fun TrackLiveStreamPreview(
    property_listing_id: Int,
    live_cast_for: String,
    stream_id: String,
    is_facebook_connected: Boolean,
    is_youtube_connected: Boolean,
    is_vertical_orientation: String,
) {
    val isVertical = is_vertical_orientation == Orientation.LAND.name

    val params = mapOf(
        "property_listing_id" to property_listing_id,
        "live_cast_for" to live_cast_for,
        "stream_id" to stream_id,
        "is_facebook_connected" to is_facebook_connected,
        "is_youtube_connected" to is_youtube_connected,
        "is_vertical_orientation" to isVertical
    )
    recordEvent(Event.live_stream_preview_created, params)
}

fun TrackStep4(
    property_listing_id: Int,
    live_cast_for: String,
    video_format: String,
    is_facebook_connected: Boolean,
    is_youtube_connected: Boolean,
    is_vertical_orientation: String,
    step: Int
) {
    val isVertical = is_vertical_orientation == Orientation.LAND.name

    val format = if (video_format == LiveFormat.PRE.name) {
        "PreRecord"
    } else {
        "LiveStream"
    }

    val params = mapOf(
        "property_listing_id" to property_listing_id,
        "live_cast_for" to live_cast_for,
        "video_format" to format,
        "is_facebook_connected" to is_facebook_connected,
        "is_youtube_connected" to is_youtube_connected,
        "is_vertical_orientation" to isVertical,
        "step" to step,
    )
    recordEvent(Event.asset_step4_preview_clicked, params)
}

fun TrackStep3(
    property_listing_id: Int,
    live_cast_for: String,
    step: Int,
) {
    val params = mapOf(
        "property_listing_id" to property_listing_id,
        "live_cast_for" to live_cast_for,
        "step" to step,
    )
    recordEvent(Event.asset_step3_next_clicked, params)
}

fun TrackStep2(
    property_listing_id: Int,
    is_show_profile: Boolean,
    profile_id: Int?,
    step: Int,
) {
    val params = mapOf(
        "property_listing_id" to property_listing_id,
        "is_show_profile" to is_show_profile,
        "profile_id" to profile_id,
        "step" to step,
    )
    recordEvent(Event.asset_step2_next_clicked, params)
}

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

/*@Composable
fun TrackLogin(email: String) {
    LaunchedEffect(Unit) {
        val params = mapOf("email" to email)
        recordEvent(Event.user_logged_in, params)
    }
}*/