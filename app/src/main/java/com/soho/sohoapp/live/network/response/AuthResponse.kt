package com.soho.sohoapp.live.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("data") val data: Data? = null,
    @SerialName("response_type") val responseType: String? = null,
    @SerialName("response") val response: String? = null,
)

@Serializable
data class Data(
    @SerialName("id") val id: Int,
    @SerialName("authentication_token") val authenticationToken: String,
    @SerialName("email") val email: String,
    @SerialName("last_session_profile_id") val lastSessionProfileId: Int? = null,
    @SerialName("agent_profiles") val agentProfiles: List<AgentProfile>
)

@Serializable
data class AgentProfile(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("profile_image_url") val profileImageUrl: String? = null,
    @SerialName("sale_listing_count") val saleListingCount: Int,
    @SerialName("rent_listing_count") val rentListingCount: Int
)
