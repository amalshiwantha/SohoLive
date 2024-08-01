package com.soho.sohoapp.live.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveRequest(
    @SerialName("simulcast_targets") val simulcastTargets: LiveTarget,
    val streamKey: String,
    val liveStreamId: Int,
)

@Serializable
data class LiveTarget(
    @SerialName("platform") var platform: List<String> = mutableListOf(),
    @SerialName("access_token") var accessToken: List<String> = mutableListOf(),
    @SerialName("target_feed_id") var targetFeedId: List<String> = mutableListOf(),
    @SerialName("privacy") var privacy: List<String> = mutableListOf()
)

@Serializable
data class LiveResponse(
    @SerialName("response") val response: String? = null,
    @SerialName("response_type") val responseType: String? = null,
)