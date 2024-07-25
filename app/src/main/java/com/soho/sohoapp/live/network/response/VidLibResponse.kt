package com.soho.sohoapp.live.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class VidLibResponse(
    @SerialName("data") val data: DataVidRes,
    @SerialName("response") val response: String? = null,
    @SerialName("response_type") val responseType: String? = null,
)

@Serializable
data class DataVidRes(
    @SerialName("assets") val assets: List<Asset>,
    @SerialName("meta") val meta: MetaVidRes
)

@Serializable
data class Asset(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String?,
    @SerialName("stream_type") val streamType: String,
    @SerialName("duration") val duration: Int?,
    @SerialName("started_at") val startedAt: String,
    @SerialName("playback_ids") val playbackIds: List<String>,
    @SerialName("unlisted") val unlisted: Boolean,
    @SerialName("property_listing_id") val propertyListingId: Int,
    @SerialName("agent_profile_id") val agentProfileId: Int,
    @SerialName("live_stream_id") val liveStreamId: Int,
    @SerialName("shareable_link") val shareableLink: String?,
    @SerialName("soho_link") val sohoLink: String,
    @SerialName("status") val status: String
)

@Serializable
data class MetaVidRes(
    @SerialName("total_results") val totalResults: Int,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("next_page") val nextPage: Int?,
    @SerialName("per_page") val perPage: Int
)
