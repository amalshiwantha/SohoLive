package com.soho.sohoapp.live.model

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.soho.sohoapp.live.enums.CategoryType
import com.soho.sohoapp.live.enums.FormFields
import com.soho.sohoapp.live.enums.SocialMediaInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class FBGroupPage(
    val id: String,
    val name: String,
    val accessToken: String? = null,
    val pictureUrl: String
)

data class FBProfile(
    val id: String,
    val name: String,
    val email: String,
    val profilePictureUrl: String
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
    @SerialName("live_stream_at") var liveStreamAt: String? = null,
    @SerialName("description") var description: String? = null,
    @SerialName("agent_profile_id") var agentId: String? = null,
    @SerialName("unlisted") var unlisted: Boolean = false,
    @SerialName("platform") var platform: MutableList<String> = mutableListOf(),
    @SerialName("access_token") var accessToken: MutableList<String> = mutableListOf(),
    @SerialName("target_feed_id") var feedId: MutableList<String> = mutableListOf(),
    @SerialName("privacy") var privacy: MutableList<String> = mutableListOf(),
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
    val isConnected: Boolean = false
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
