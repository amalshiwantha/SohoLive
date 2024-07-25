package com.soho.sohoapp.live.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VidLibRequest(
    @SerialName("page") var page: Int = 1,
    @SerialName("per_page") var perPage: Int = 20,
    @SerialName("sort_by") var sortBy: String = "created_at",
    @SerialName("sort_order") var sortOrder: String = "desc"
)