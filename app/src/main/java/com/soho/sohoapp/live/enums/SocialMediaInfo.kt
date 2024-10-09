package com.soho.sohoapp.live.enums

import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.model.CategoryInfo
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.FacebookBlue
import com.soho.sohoapp.live.ui.theme.LinkedInBlue
import com.soho.sohoapp.live.ui.theme.YoutubeRed
import kotlinx.serialization.Serializable

enum class SocialMedia(smName: String) {
    FACEBOOK("facebook"),
    YOUTUBE("youtube"),
    LINKEDIN("linkedin")
}

@Serializable
enum class SocialMediaInfo(
    var title: String,
    var info: String,
    var infoSub: String? = null,
    var infoItems: MutableList<String> = mutableListOf(),
    var infoItemBtn: MutableList<String> = mutableListOf(),
    var btnTitle: String,
    var btnColor: Color,
    var btnIcon: Int,
    val icon: Int,
    var isConnect: Boolean = false,
    var isItemChecked: Boolean = false,
    var accessToken: String? = null,
    var selectionType: CategoryInfo? = null
) {
    SOHO(
        title = "Your listing on",
        info = "Soho",
        btnTitle = "",
        btnColor = AppWhite,
        btnIcon = R.drawable.logo_soho,
        icon = R.drawable.logo_soho,
        isConnect = true
    ),
    FACEBOOK(
        title = "Facebook",
        info = "By connecting, you will be able to broadcast your livecast to your Facebook friends and followers",
        infoSub = "Ensure your Facebook account meet these requirements before you go live:",
        infoItems = mutableListOf(
            "Your account must be at least 60 days old.",
            "Your Page or professional profile must have at least 100 followers.",
            "To go live from a Page, you need to have Facebook access or task access to create content"
        ),
        btnTitle = "Connect to Facebook",
        btnColor = FacebookBlue,
        btnIcon = R.drawable.ic_fb_round,
        icon = R.drawable.logo_facebook,
        isConnect = false
    ),
    YOUTUBE(
        title = "Youtube",
        info = "By connecting, you will be able to broadcast your livecast to your Youtube subscribers.",
        infoSub = "Make sure your YouTube account is ready for live streaming by following these two steps:",
        infoItems = mutableListOf(
            "Make sure your YouTube account is verified",
            "Enable live streaming at least 24 hours in advance"
        ),
        infoItemBtn = mutableListOf("verify", "enable"),
        btnTitle = "Connect to Youtube",
        btnColor = YoutubeRed,
        btnIcon = R.drawable.ic_youtube_round,
        icon = R.drawable.logo_youtube,
        isConnect = false
    ),
    LINKEDIN(
        title = "LinkedIn",
        info = "By connecting, you will be able to broadcast your livestream to your LinkedIn followers",
        btnTitle = "Connect to LinkedIn",
        btnColor = LinkedInBlue,
        btnIcon = R.drawable.ic_linkedin_round,
        icon = R.drawable.logo_linkedin,
        isConnect = false
    ),
    NONE(
        title = "",
        info = "",
        btnTitle = "",
        btnColor = AppWhite,
        btnIcon = R.drawable.logo_soho,
        icon = R.drawable.logo_soho,
        isConnect = false
    )
}
