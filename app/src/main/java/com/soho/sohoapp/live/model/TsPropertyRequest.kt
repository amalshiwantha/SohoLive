package com.soho.sohoapp.live.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TsPropertyRequest(
    @SerialName("q") var query: String? = null,
    @SerialName("query_by") var queryBy: String? = null,
    @SerialName("filter_by") var filterBy: String? = null,
    @SerialName("per_page") var perPage: String? = null,
    @SerialName("page") var page: String? = null
)