package com.soho.sohoapp.live.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveRequest(
    @SerialName("platform") val platform: List<String>,
    @SerialName("url") val url: String,
    @SerialName("stream_key") val streamKey: String,
    @SerialName("live_stream_id") val liveStreamId: Int,
)

@Serializable
data class LiveResponse(
    @SerialName("response") val response: String? = null,
    @SerialName("response_type") val responseType: String? = null,
)