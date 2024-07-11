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
    @SerialName("id") val id: Int,
    @SerialName("property_listing_id") val propertyListingId: Int,
    @SerialName("live_stream_at") val liveStreamAt: String?,
    @SerialName("stream_type") val streamType: String,
    @SerialName("title") val title: String,
    @SerialName("agent_user_id") val agentUserId: Int,
    @SerialName("description") val description: String?,
    @SerialName("stream_key") val streamKey: String,
    @SerialName("unlisted") val unlisted: Boolean,
    @SerialName("agent_profile_id") val agentProfileId: Int,
    @SerialName("shareable_link") val shareableLink: String,
    @SerialName("simulcast_targets") val simulcastTargets: List<SimulcastTarget>
)

@Serializable
data class SimulcastTarget(
    @SerialName("platform") val platform: String,
    @SerialName("feed_id") val feedId: String,
    @SerialName("shareable_link") val shareableLink: String
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
    @SerialName("agent_profile_ids") val agentProfileIds: List<Int>,
    //@SerialName("live_streams_ids") val liveStreamsIds: List<Int>
)

@Serializable
data class AgentProfileGoLive(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String?,
    @SerialName("email") val email: String?,
    @SerialName("rating") val rating: Float = 0f,
    @SerialName("profile_image_url") val imageUrl: String?,
    @SerialName("sale_listing_count") val saleListingCount: Int,
    @SerialName("rent_listing_count") val rentListingCount: Int
)
