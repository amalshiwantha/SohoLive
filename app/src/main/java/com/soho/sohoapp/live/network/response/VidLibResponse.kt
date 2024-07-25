package com.soho.sohoapp.live.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VidLibResponse(
    @SerialName("data") val data: DataGoLiveSubmit? = null,
    @SerialName("response") val response: String? = null,
    @SerialName("response_type") val responseType: String? = null,
)