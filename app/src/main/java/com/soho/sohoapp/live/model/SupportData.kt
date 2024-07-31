package com.soho.sohoapp.live.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.soho.sohoapp.live.enums.CategoryType
import com.soho.sohoapp.live.enums.FormFields
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.network.response.AgentProfileGoLive
import com.soho.sohoapp.live.network.response.DataVidRes
import com.soho.sohoapp.live.network.response.Document
import com.soho.sohoapp.live.network.response.VideoItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveCastStatus(val statusCode: Int, val message: String)

@Serializable
data class VideoAnalytics(
    val fb: Int = 0,
    val yt: Int = 0,
    val li: Int = 0,
    val soho: Int = 0,
    val play_min: Int = 0, //seconds
) {
    // Function to get the total playtime count in seconds
    fun getTotalPlayTime(): Int {
        return fb + yt + li + soho
    }

    // Function to format the total playtime
    fun getFormattedPlayTime(): String {
        return formatPlayTime(getTotalPlayTime())
    }

    private fun formatPlayTime(seconds: Int): String {
        if (seconds <= 0) return "0 sec"

        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return when {
            hours > 0 -> "$hours H ${if (minutes > 0) "$minutes min" else ""}".trim()
            minutes > 0 -> "${if (minutes > 0) "$minutes min" else ""} ${if (secs > 0) "$secs sec" else ""}".trim()
            else -> "${secs} sec"
        }
    }
}

data class PropertyItem(val id: Int, val propInfo: Document, var isChecked: Boolean = false)

data class AgencyItem(
    val id: Int, val agentProfile: AgentProfileGoLive, var isChecked: Boolean = false
)

data class TextFiledConfig(
    var input: String = "",
    var placeholder: String = "",
    var isError: Boolean = false,
    var clickable: Boolean = false,
    var isSingleLine: Boolean = true,
    var trailingIcon: ImageVector? = null,
    var imeAction: ImeAction = ImeAction.Next,
    var keyboardType: KeyboardType = KeyboardType.Text,
)

@Serializable
data class GoLiveSubmit(
    @SerialName("stream_type") var purpose: String? = null,
    @SerialName("property_listing_id") var propertyId: Int = 0,
    @SerialName("title") var title: String? = null,
    @SerialName("description") var description: String? = null,
    @SerialName("agent_profile_id") var agentId: Int = 0,
    @SerialName("unlisted") var unlisted: Boolean = false,
    @SerialName("simulcast_targets") var targets: MutableList<GoLivePlatform> = mutableListOf(),
    @SerialName("schedules_at") var schedulesAt: MutableList<ScheduleDateTime> = mutableListOf(),
    var platformToken: MutableList<PlatformToken> = mutableListOf(),
    var scheduleSlots: MutableList<ScheduleSlots> = mutableListOf(),
    var errors: MutableMap<FormFields, String> = mutableMapOf(),
    var checkedPlatforms: MutableList<String> = mutableListOf(),
    var videoLibResState: MutableState<DataVidRes?> = mutableStateOf(null),
    var videoItemState: MutableState<VideoItem?> = mutableStateOf(null)
) {
    constructor() : this(
        purpose = null,
        title = null,
        description = null
    )
}

@Serializable
data class PlatformToken(
    var platform: String,
    var accessToken: String
)

@Serializable
data class GoLivePlatform(
    @SerialName("target_feed_id") var targetFeedId: String? = null,
    @SerialName("platform") var platform: String? = null,
    @SerialName("access_token") var accessToken: String? = null
)

@Serializable
data class ScheduleSlots(
    var date: String? = null,
    var time: String? = null,
    var display: String? = null
) {
    constructor() : this(
        date = null,
        time = null,
        display = null
    )
}

@Serializable
data class ScheduleDateTime(
    @SerialName("date_time") var dateTime: String? = null
)

@Serializable
data class ConnectedSocialMedia(val smList: MutableList<SocialMediaInfo>)

@Serializable
data class ConnectedSocialProfile(val smProfileList: MutableList<SocialMediaProfile>)

@Serializable
data class SocialMediaProfile(
    val smInfo: SocialMediaInfo,
    val profile: Profile,
    val timelines: MutableList<CategoryInfo> = mutableListOf(),
    val pages: MutableList<CategoryInfo> = mutableListOf(),
    val groups: MutableList<CategoryInfo> = mutableListOf()
) {
    constructor() : this(
        smInfo = SocialMediaInfo.NONE,
        profile = Profile()
    )
}

@Serializable
data class Profile(
    val fullName: String? = null,
    val imageUrl: String? = null,
    val email: String? = null,
    val token: String? = null,
    val type: SocialMediaInfo = SocialMediaInfo.NONE,
    var isConnected: Boolean = false
) {
    constructor() : this(
        fullName = "",
        imageUrl = "",
        email = "",
        token = ""
    )
}

@Serializable
data class CategoryInfo(
    var index: Int = 0,
    val id: String,
    val type: CategoryType,
    val title: String,
    val url: String,
    val imageUrl: String,
    val accessToken: String,
    val isSelect: Boolean = false
)
