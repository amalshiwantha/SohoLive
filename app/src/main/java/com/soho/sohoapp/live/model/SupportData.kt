package com.soho.sohoapp.live.model

import com.soho.sohoapp.live.enums.SocialMediaInfo

data class SocialMediaProfile(
    val smInfo: SocialMediaInfo,
    val fullName: String,
    val imageUrl: String,
    val email: String,
    val token: String
)
