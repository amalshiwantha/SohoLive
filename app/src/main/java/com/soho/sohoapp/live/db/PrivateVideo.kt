package com.soho.sohoapp.live.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soho.sohoapp.live.enums.VideoPrivacy
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
) {
    val dayLabel: String
        get() = getDayLabel(createdDate)
}

@Serializable
data class AgentProperty(
    val propertyId: Int = 0,
    val address: String? = null,
    val bedrooms: Int = 0,
    val bathrooms: Int = 0,
    val parking: Int = 0,
    val sizeSm: Float = 0.0f,
    val agentName: String? = null,
    val agencyColor: String? = null,
    val agentProfileUrl: String? = null,
    val agencyLogoUrl: String? = null,
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
