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
    live_stream_preview_created, cancel_live_stream_preview,
    live_stream_started, live_stream_finished, live_stream_url_copied,
    cancel_prerecord_video_preview, prerecord_video_started,
    prerecord_video_saved_internal, prerecord_video_deleted_internal,
    prerecord_video_published, asset_download_completed, linked_listing_clicked,
    manage_video_opened, mux_player_opened, asset_data_updated,
    all_plans_viewed, plan_usage_viewed
}

//Event Tracks

fun TrackPlanViewed() {
    recordEvent(Event.all_plans_viewed, null)
}

fun TrackPlanUsage() {
    recordEvent(Event.plan_usage_viewed, null)
}

fun TrackAssetUpdate(asset_id: Int) {
    val params = mapOf("asset_id" to asset_id)
    recordEvent(Event.asset_data_updated, params)
}

fun TrackMuxPlayer(asset_id: Int) {
    val params = mapOf("asset_id" to asset_id)
    recordEvent(Event.mux_player_opened, params)
}

fun TrackAssetManageVideo(asset_id: Int) {
    val params = mapOf("asset_id" to asset_id)
    recordEvent(Event.manage_video_opened, params)
}

fun TrackAssetClicked(property_listing_id: Int, asset_id: Int) {
    val params = mapOf("property_listing_id" to property_listing_id, "asset_id" to asset_id)
    recordEvent(Event.linked_listing_clicked, params)
}

fun TrackAssetDownloadDone(asset_id: Int) {
    val params = mapOf("asset_id" to asset_id)
    recordEvent(Event.asset_download_completed, params)
}

fun TrackPreRecordPublished(
    property_listing_id: Int,
    screen: String,
    is_public: Boolean,
    live_cast_for: String,
    is_vertical_orientation: Boolean
) {
    val params = mapOf(
        "property_listing_id" to property_listing_id,
        "screen" to screen,
        "is_public" to is_public,
        "live_cast_for" to live_cast_for,
        "is_vertical_orientation" to is_vertical_orientation
    )
    recordEvent(Event.prerecord_video_published, params)
}

fun TrackPreRecordDelete(property_listing_id: Int, screen: String) {
    val params = mapOf(
        "property_listing_id" to property_listing_id,
        "screen" to screen
    )
    recordEvent(Event.prerecord_video_deleted_internal, params)
}

fun TrackPreRecordSaved(property_listing_id: Int, is_private: Boolean) {
    val params = mapOf(
        "property_listing_id" to property_listing_id,
        "is_private" to is_private
    )
    recordEvent(Event.prerecord_video_saved_internal, params)
}

fun TrackPreRecordStarted(
    property_listing_id: Int,
    is_branding_enabled: Boolean
) {
    val params = mapOf(
        "property_listing_id" to property_listing_id,
        "is_branding_enabled" to is_branding_enabled
    )
    recordEvent(Event.prerecord_video_started, params)
}

fun TrackPreRecordPreviewCancel() {
    recordEvent(Event.cancel_prerecord_video_preview, null)
}

fun TrackLiveStreamCopyUrl(streamId: String, screen: String) {
    val params = mapOf(
        "stream_id" to streamId,
        "screen" to screen
    )
    recordEvent(Event.live_stream_url_copied, params)
}

fun TrackLiveStreamFinished(streamId: String) {
    val params = mapOf("stream_id" to streamId)
    recordEvent(Event.live_stream_finished, params)
}

fun TrackLiveStreamStarted(
    streamId: String,
    property_listing_id: Int,
    is_vertical_orientation: String,
    is_branding_enabled: Boolean,
    is_facebook_connected: Boolean,
    is_youtube_connected: Boolean
) {
    val isVertical = is_vertical_orientation == Orientation.LAND.name

    val params = mapOf(
        "stream_id" to streamId,
        "property_listing_id" to property_listing_id,
        "is_vertical_orientation" to isVertical,
        "is_branding_enabled" to is_branding_enabled,
        "is_facebook_connected" to is_facebook_connected,
        "is_youtube_connected" to is_youtube_connected
    )
    recordEvent(Event.live_stream_started, params)
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
private fun recordEvent(event: Event, parameters: Map<String, Any?>?) {
    val bundle = Bundle()

    parameters?.let {
        for ((key, value) in it) {
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Double -> bundle.putDouble(key, value)
                is Boolean -> bundle.putBoolean(key, value)
            }
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