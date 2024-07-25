package com.soho.sohoapp.live.network.response

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


@Serializable
data class VidLibResponse(
    @SerialName("data") val data: DataVidRes,
    @SerialName("response") val response: String? = null,
    @SerialName("response_type") val responseType: String? = null,
)

@Serializable
data class DataVidRes(
    @SerialName("assets") val assets: List<VideoItem>,
    @SerialName("meta") val meta: MetaVidRes
)

@Serializable
data class VideoItem(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String?,
    @SerialName("description") val description: String?,
    @SerialName("stream_type") val streamType: String,
    @SerialName("duration") val duration: Int?,
    @SerialName("started_at") val startedAt: String,
    @SerialName("playback_ids") val playbackIds: List<String>,
    @SerialName("unlisted") var unlisted: Boolean,
    @SerialName("property_listing_id") val propertyListingId: Int,
    @SerialName("agent_profile_id") val agentProfileId: Int,
    @SerialName("live_stream_id") val liveStreamId: Int,
    @SerialName("shareable_link") val shareableLink: String?,
    @SerialName("download_link") val downloadLink: String?,
    @SerialName("soho_link") val sohoLink: String,
    @SerialName("status") val status: String,
    var property: Document? = null,
) {
    fun getDisplayDate(): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date: Date? = inputFormat.parse(startedAt)
            outputDateFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            startedAt
        }
    }

    @SuppressLint("DefaultLocale")
    fun getDisplayDuration(): String {
        return duration?.let {
            val hours = TimeUnit.SECONDS.toHours(it.toLong())
            val minutes = TimeUnit.SECONDS.toMinutes(it.toLong()) % 60
            val seconds = it % 60
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } ?: run {
            "00:00"
        }
    }
}

@Serializable
data class MetaVidRes(
    @SerialName("total_results") val totalResults: Int,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("next_page") val nextPage: Int?,
    @SerialName("per_page") val perPage: Int
)
