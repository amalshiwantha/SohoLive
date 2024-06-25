package com.soho.sohoapp.live.enums

import com.soho.sohoapp.live.R

enum class SocialMediaInfo(
    var title: String,
    var info: String,
    var btnTitle: String,
    val icon: Int,
    var isConnect: Boolean
) {
    SOHO(
        title = "Your listing on",
        info = "Soho",
        btnTitle = "",
        icon = R.drawable.logo_soho,
        isConnect = true
    ),
    FACEBOOK(
        title = "Facebook",
        info = "By connecting, you will be able to broadcast your livestream to your Facebook friends and followers",
        btnTitle = "Connect to Facebook",
        icon = R.drawable.logo_facebook,
        isConnect = false
    ),
    YOUTUBE(
        title = "Youtube",
        info = "By connecting, you will be able to broadcast your livestream to your Youtube subscribers",
        btnTitle = "Connect to Youtube",
        icon = R.drawable.logo_youtube,
        isConnect = false
    ),
    LINKEDIN(
        title = "LinkedIn",
        info = "By connecting, you will be able to broadcast your livestream to your LinkedIn followers",
        btnTitle = "Connect to LinkedIn",
        icon = R.drawable.logo_linkedin,
        isConnect = false
    ),
    NONE(
        title = "",
        info = "",
        btnTitle = "",
        icon = R.drawable.logo_soho,
        isConnect = false
    )
}
