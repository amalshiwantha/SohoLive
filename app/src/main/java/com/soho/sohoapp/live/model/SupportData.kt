package com.soho.sohoapp.live.model

import com.soho.sohoapp.live.enums.FBListType
import com.soho.sohoapp.live.enums.SocialMediaInfo

data class SocialMediaProfile(
    val smInfo: SocialMediaInfo,
    val profiles: MutableList<SMProfile>,
    val timelines: MutableList<FbTypeView>? = null,
    val pages: MutableList<FbTypeView>? = null,
    val groups: MutableList<FbTypeView>? = null
)

data class SMProfile(
    val fullName: String,
    val imageUrl: String,
    val email: String,
    val token: String
)

data class FbTypeView(
    val type: FBListType,
    val title: String,
    val url: String,
    val imageUrl: String,
    val isSelect: Boolean = false
)