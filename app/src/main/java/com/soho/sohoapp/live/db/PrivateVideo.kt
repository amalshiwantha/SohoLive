package com.soho.sohoapp.live.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.network.response.Agent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@Serializable
@Entity(tableName = "private_videos")
data class PrivateVideo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val filePath: String,
    val createdDate: String,
    var castFor: String,
    var title: String,
    var description: String? = null,
    var privacy: String = VideoPrivacy.PRIVATE.label,
    var agentProperty: AgentProperty? = null,
    var videoInfo: VideoInfo? = null
) {
    val dayLabel: String
        get() = getDayLabel(createdDate)
}

@Serializable
data class VideoInfo(
    @SerialName("stream_type") var streamType: String? = null,
    @SerialName("property_listing_id") val propertyListingId: Int = 0,
    @SerialName("title") val title: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("agent_profile_id") val agentProfileId: Int = 0,
    @SerialName("unlisted") val unlisted: Boolean = false,
    @SerialName("orientation") val orientation: String? = null
)

@Serializable
data class AgentProperty(
    val propertyId: Int = 0,
    val address: String? = null,
    val bedrooms: Double = 0.0,
    val bathrooms: Double = 0.0,
    val parking: Double = 0.0,
    val areaSize: Pair<String, Int>? = null,
    val agent: Agent? = null
)

fun getDayLabel(createdDate: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val created = dateFormat.parse(createdDate)
    val current = Date()

    // Calculate the difference in days
    val diffInMillis = abs(current.time - created.time)
    val diffInDays = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()

    return "${diffInDays}D"
}
