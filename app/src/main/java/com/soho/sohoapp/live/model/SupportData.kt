package com.soho.sohoapp.live.model

import com.soho.sohoapp.live.enums.FBListType
import com.soho.sohoapp.live.enums.SocialMediaInfo
import kotlinx.serialization.Serializable

//this if for save in local
@Serializable
data class ConnectedSocialProfile(val smProfile: SocialMediaProfile)

@Serializable
data class SocialMediaProfile(
    val smInfo: SocialMediaInfo,
    val profiles: SMProfile,
    val timelines: MutableList<FbTypeView> = mutableListOf(),
    val pages: MutableList<FbTypeView> = mutableListOf(),
    val groups: MutableList<FbTypeView> = mutableListOf()
) {
    constructor() : this(
        smInfo = SocialMediaInfo.NONE,
        profiles = SMProfile()
    )
}

@Serializable
data class SMProfile(
    val fullName: String? = null,
    val imageUrl: String? = null,
    val email: String? = null,
    val token: String? = null,
    val type: SocialMediaInfo = SocialMediaInfo.NONE
) {
    constructor() : this(
        fullName = "",
        imageUrl = "",
        email = "",
        token = ""
    )
}

@Serializable
data class FbTypeView(
    val index: Int = 0,
    val type: FBListType,
    val title: String,
    val url: String,
    val imageUrl: String,
    val isSelect: Boolean = false
)
