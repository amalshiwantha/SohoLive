package com.soho.sohoapp.live.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoLiveResponse(
    @SerialName("data") val data: Data? = null,
    @SerialName("response_type") val responseType: String? = null,
    @SerialName("response") val response: String? = null,
)