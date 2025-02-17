package com.soho.sohoapp.live.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteConfigModel(
    val versionCode: Int,
    val message: String,
    val level: String
)