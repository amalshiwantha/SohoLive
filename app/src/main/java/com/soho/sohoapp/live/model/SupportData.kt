package com.soho.sohoapp.live.model

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.soho.sohoapp.live.enums.CategoryType
import com.soho.sohoapp.live.enums.FormFields
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.network.response.AgentProfileGoLive
import com.soho.sohoapp.live.network.response.Document
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class VideoItem(
    var propertyType: String? = null, //action inspection etc
    var visibility: Int = 0, //public 1, private 0
    val duration: String? = null, //22:31
    val date: String? = null, //22 may 2024
    val title: String? = null,
    val info: String? = null,
    var imageUrl: String? = null,
    val analytics: VideoAnalytics? = null,
    val shareableLink: String? = null,
    val downloadLink: String? = null
)

data class VideoAnalytics(
    val fb: Int = 0,
    val yt: Int = 0,
    val li: Int = 0,
    val soho: Int = 0,
    val play_min: Int = 0,
)

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
    var checkedPlatforms: MutableList<String> = mutableListOf()
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
