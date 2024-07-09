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

/*
* {
   "unlisted":false,
   "stream_type":"auction",
   "title":"2\/102 Saddlers Drive, Gillieston Heights NSW 2321",
   "simulcast_targets":[
      {
         "target_feed_id":"me",
         "platform":"facebook",
         "privacy":"",
         "access_token":"EAAK8mp1i0v4BO0DNT7MdRNAy8uIp1olq73oZCv0cmUQSmvQXnUepsClSSYUxbMAsHQZAQrYDcVzCqrZCgCZA51tdaRXnh1aWVboFC9CFCEhh1yPlL7lErYqaEDAVHDNhIQ6GxxxpTjX2dzitIZB5MIYOdNSdO6jFCL6BqzPxGU9LHA0S5IKPh7TDqwtoLVnA90SARwvF3e6DXLPAk4YP6bZCutjgZDZD"
      },
      {
         "platform":"youtube",
         "target_feed_id":"",
         "access_token":"ya29.a0AXooCgtJHM-12Z-DdKoyaV6seysRWLW9Huh9ZiTFNjByKkfoLHJjetCvcXEjgkpo4ejZp8MBg4NuSLf2DWc126kFhL6NK6wRuFK4nXdmgFRVjWPjW5Rckjg_6okzKzjj4z0572dDViFQGxseJgon6yzzgTVWNpSFYsPbaCgYKATkSARASFQHGX2MiDMpaYDx1QgpW43MOx80rLA0171",
         "privacy":""
      }
   ],
   "property_listing_id":81063,
   "description":"Test Description",
   "agent_profile_id":9619
}
* */
@Serializable
data class GoLiveSubmit(
    @SerialName("stream_type") var purpose: String? = null,
    @SerialName("property_listing_id") var propertyId: Int = 0,
    @SerialName("title") var title: String? = null,
    @SerialName("description") var description: String? = null,
    @SerialName("agent_profile_id") var agentId: String? = null,
    @SerialName("unlisted") var unlisted: Boolean = false,
    @SerialName("simulcast_targets") var targets: MutableList<GoLivePlatform> = mutableListOf(),
    var platformToken: MutableList<PlatformToken> = mutableListOf(),
    var accessToken: MutableList<String> = mutableListOf(),
    var platform: MutableList<String> = mutableListOf(),
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

/*
* if timeline selected
* {
         "target_feed_id":"me",
         "platform":"facebook",
         "privacy":"",
         "access_token":"EAAK8mp1i0v4...." (login accessToken)
      }
      *
      *
      * if page or group
      * {
         "access_token":"EAAK...", (page or group accessToken)
         "platform":"facebook",
         "target_feed_id":"359629113892076",(page or group ID)
         "privacy":""
      }
* */

@Serializable
data class GoLivePlatform(
    @SerialName("target_feed_id") var targetFeedId: String? = null,
    @SerialName("platform") var platform: String? = null,
    @SerialName("access_token") var accessToken: String? = null,
    @SerialName("privacy") var privacy: String = "",
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
