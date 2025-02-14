package com.soho.sohoapp.live.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPwRequest(
    @SerialName("login_token") var loginToken: String? = null,
    @SerialName("new_password") var newPassword: String? = null,
    var confirmPassword: String? = null
)