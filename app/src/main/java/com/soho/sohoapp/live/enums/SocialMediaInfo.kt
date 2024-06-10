package com.soho.sohoapp.live.enums

import com.soho.sohoapp.live.R

enum class SocialMediaInfo(
    var title: String,
    val icon: Int
) {
    SOHO(
        title = "Soho",
        icon = R.drawable.logo_soho
    ),
    FACEBOOK(
        title = "FB",
        icon = R.drawable.logo_facebook
    ),
    YOUTUBE(
        title = "YouTube",
        icon = R.drawable.logo_youtube
    ),
    LINKEDIN(
        title = "Linkedin",
        icon = R.drawable.logo_linkedin
    )
}
