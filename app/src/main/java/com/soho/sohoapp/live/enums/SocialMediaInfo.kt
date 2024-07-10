package com.soho.sohoapp.live.enums

import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.model.CategoryInfo
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.FacebookBlue
import com.soho.sohoapp.live.ui.theme.LinkedInBlue
import com.soho.sohoapp.live.ui.theme.YoutubeRed
import kotlinx.serialization.Serializable

@Serializable
enum class SocialMediaInfo(
    var title: String,
    var info: String,
    var btnTitle: String,
    var btnColor: Color,
    var btnIcon: Int,
    val icon: Int,
    var isConnect: Boolean,
    var isItemChecked: Boolean = true,
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
        info = "By connecting, you will be able to broadcast your livestream to your Facebook friends and followers",
        btnTitle = "Connect to Facebook",
        btnColor = FacebookBlue,
        btnIcon = R.drawable.ic_fb_round,
        icon = R.drawable.logo_facebook,
        isConnect = false
    ),
    YOUTUBE(
        title = "Youtube",
        info = "By connecting, you will be able to broadcast your livestream to your Youtube subscribers",
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
