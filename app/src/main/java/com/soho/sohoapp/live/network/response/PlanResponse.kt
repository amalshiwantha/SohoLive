package com.soho.sohoapp.live.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanResponse(
    @SerialName("data") val data: PlanData
)

@Serializable
data class PlanData(
    @SerialName("id") val id: Int,
    @SerialName("interval") val interval: String,
    @SerialName("name") val name: String,
    @SerialName("price") val price: String,
    @SerialName("terms") val terms: PlanTerms,
    @SerialName("plan_type") val planType: String,
    @SerialName("best_value") val bestValue: Boolean
)

@Serializable
data class PlanTerms(
    @SerialName("viewing_minutes") val viewingMinutes: String,
    @SerialName("streaming_minutes") val streamingMinutes: String,
    @SerialName("in_app_storage_days") val inAppStorageDays: String,
    @SerialName("simulcasting_enabled") val simulcastingEnabled: Boolean,
    @SerialName("listing_available_days") val listingAvailableDays: String,
    @SerialName("overage_rate_dollars_per_minute") val overageRateDollarsPerMinute: String
)