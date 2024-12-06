package com.soho.sohoapp.live.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsageResponse(
    @SerialName("data") val data: UsageData? = null,
    @SerialName("response_type") val responseType: String? = null,
    @SerialName("response") val response: String? = null,
)

@Serializable
data class UsageData(
    @SerialName("current_usage") val currentUsage: CurrentUsage? = null,
)

@Serializable
data class CurrentUsage(
    @SerialName("start_time") val startTime: String? = null,
    @SerialName("end_time") val endTime: String? = null,
    @SerialName("plan_details") val planDetails: PlanDetails? = null,
    @SerialName("streaming_minutes") val streamingMinutes: Int = 0,
    @SerialName("viewing_minutes") val viewingMinutes: Int = 0,
    @SerialName("overage_streaming_minutes") val overageStreamingMinutes: Int = 0,
    @SerialName("overage_viewing_minutes") val overageViewingMinutes: Int = 0
)

@Serializable
data class PlanDetails(
    @SerialName("name") val name: String? = null,
    @SerialName("interval") val interval: String? = null,
    @SerialName("price") val price: String? = null,
    @SerialName("stripe_plan_id") val stripePlanId: String? = null,
    @SerialName("stripe_viewing_plan_id") val stripeViewingPlanId: String? = null,
    @SerialName("stripe_streaming_plan_id") val stripeStreamingPlanId: String? = null,
    @SerialName("plan_type") val planType: String? = null,
    @SerialName("viewing_minutes") val viewingMinutes: String? = null,
    @SerialName("streaming_minutes") val streamingMinutes: String? = null,
    @SerialName("in_app_storage_days") val inAppStorageDays: String? = null,
    @SerialName("simulcasting_enabled") val simulcastingEnabled: Boolean = false,
    @SerialName("listing_available_days") val listingAvailableDays: String? = null,
    @SerialName("overage_rate_dollars_per_minute") val overageRateDollarsPerMinute: String? = null
)