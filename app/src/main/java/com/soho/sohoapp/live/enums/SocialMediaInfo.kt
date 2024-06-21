package com.soho.sohoapp.live.enums

import com.soho.sohoapp.live.R

enum class SocialMediaInfo(
    var title: String,
    val icon: Int,
    var isConnect: Boolean
) {
    SOHO(
        title = "Your listing on",
        icon = R.drawable.logo_soho,
        isConnect = true
    ),
    FACEBOOK(
        title = "",
        icon = R.drawable.logo_facebook,
        isConnect = false
    ),
    YOUTUBE(
        title = "",
        icon = R.drawable.logo_youtube,
        isConnect = false
    ),
    LINKEDIN(
        title = "",
        icon = R.drawable.logo_linkedin,
        isConnect = false
    )
}
