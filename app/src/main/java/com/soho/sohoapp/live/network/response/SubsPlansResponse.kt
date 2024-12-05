package com.soho.sohoapp.live.network.response

import com.soho.sohoapp.live.model.SubscriptionCategory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubsPlansResponse(
    @SerialName("data") val data: List<SubscriptionCategory>
)