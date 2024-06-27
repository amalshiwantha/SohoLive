package com.soho.sohoapp.live.model

import com.soho.sohoapp.live.enums.FBListType
import com.soho.sohoapp.live.enums.SocialMediaInfo
import kotlinx.serialization.Serializable

data class SocialMediaProfile(
    val smInfo: SocialMediaInfo,
    val profiles: MutableList<SMProfile>,
    val timelines: MutableList<FbTypeView> = mutableListOf(),
    val pages: MutableList<FbTypeView> = mutableListOf(),
    val groups: MutableList<FbTypeView> = mutableListOf()
)

@Serializable
data class SMProfile(
    val fullName: String,
    val imageUrl: String,
    val email: String,
    val token: String,
    val type: SocialMediaInfo = SocialMediaInfo.NONE
) {
    constructor() : this(
        fullName = "",
        imageUrl = "",
        email = "",
        token = ""
    )
}

data class FbTypeView(
    val index: Int = 0,
    val type: FBListType,
    val title: String,
    val url: String,
    val imageUrl: String,
    val isSelect: Boolean = false
)
