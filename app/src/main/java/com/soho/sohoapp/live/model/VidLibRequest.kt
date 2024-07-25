package com.soho.sohoapp.live.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VidLibRequest(
    @SerialName("page") var page: Int = 0,
    @SerialName("per_page") var perPage: Int = 0,
    @SerialName("sort_by") var sortBy: String? = null,
    @SerialName("sort_order") var sortOrder: String? = null
)