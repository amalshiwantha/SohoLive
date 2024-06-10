package com.soho.sohoapp.live.enums

import com.soho.sohoapp.live.R

enum class SocialMediaInfo(
    var title: String,
    val icon: Int,
    var isConnect: Boolean
) {
    SOHO(
        title = "Soho",
        icon = R.drawable.logo_soho,
        isConnect = false
    ),
    FACEBOOK(
        title = "FB",
        icon = R.drawable.logo_facebook,
        isConnect = true
    ),
    YOUTUBE(
        title = "YouTube",
        icon = R.drawable.logo_youtube,
        isConnect = false
    ),
    LINKEDIN(
        title = "Linkedin",
        icon = R.drawable.logo_linkedin,
        isConnect = true
    )
}
