package com.soho.sohoapp.live.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoLiveSubmitResponse(
    @SerialName("data") val data: DataGoLiveSubmit? = null,
    @SerialName("response") val response: String? = null,
    @SerialName("response_type") val responseType: String? = null,
)

@Serializable
data class DataGoLiveSubmit(
    @SerialName("id") val id: String,
    @SerialName("property_listing_id") val propertyListingId: Int,
    @SerialName("live_stream_at") val liveStreamAt: String?,
    @SerialName("stream_type") val streamType: String,
    @SerialName("title") val title: String,
    @SerialName("agent_user_id") val agentUserId: Int,
    @SerialName("description") val description: String?,
    @SerialName("stream_key") val streamKey: String,
    @SerialName("unlisted") val unlisted: Boolean,
    @SerialName("agent_profile_id") val agentProfileId: Int?,
    @SerialName("shareable_link") val shareableLink: String,
    @SerialName("simulcast_targets") val simulcastTargets: List<SimulcastTarget>,
    @SerialName("mux_status") val muxStatus: String,
    @SerialName("soho_link") val sohoLink: String,
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerialName("playback_id") val playbackId: String,
)

@Serializable
data class SimulcastTarget(
    @SerialName("platform") val platform: String,
    @SerialName("feed_id") val feedId: String,
    @SerialName("shareable_link") val shareableLink: String,
    @SerialName("shareable_post") val shareablePost: String? = null
)

@Serializable
data class GoLiveResponse(
    @SerialName("data") val data: DataGoLive? = null,
    @SerialName("response") val response: String? = null,
    @SerialName("response_type") val responseType: String? = null,
)

@Serializable
data class DataGoLive(
    @SerialName("listings") val listings: List<Listing>,
    @SerialName("agent_profiles") val agentProfiles: List<AgentProfileGoLive>
)

@Serializable
data class Listing(
    @SerialName("id") val id: Int,
    @SerialName("agent_profile_ids") val agentProfileIds: List<Int?>,
    //@SerialName("live_streams_ids") val liveStreamsIds: List<Int>
)

@Serializable
data class AgentProfileGoLive(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String?,
    @SerialName("review_count") val currentStars: Float? = 0f,
    @SerialName("overall_stars") val maxStars: Float? = 0f,
    @SerialName("profile_image_url") val imageUrl: String?,
    @SerialName("banner_image") val bannerImage: String?,
    @SerialName("agency_name") val agencyName: String?,
    @SerialName("agency_bg_color") val agencyBgColor: String?,
    @SerialName("agent_bg_color") val agentBgColor: String?,
    @SerialName("sale_listing_count") val saleListingCount: Int = 0,
    @SerialName("rent_listing_count") val rentListingCount: Int = 0,
    @SerialName("avatar") val avatar: Avatar?
)

@Serializable
data class Avatar(
    @SerialName("url") val url: String?,
    @SerialName("name") val name: String?
)
