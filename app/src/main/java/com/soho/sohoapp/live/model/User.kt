package com.soho.sohoapp.live.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("name") var name: String = "",
    @SerialName("email") var email: String = ""
)