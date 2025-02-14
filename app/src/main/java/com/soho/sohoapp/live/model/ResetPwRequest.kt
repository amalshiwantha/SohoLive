package com.soho.sohoapp.live.model

import kotlinx.serialization.Serializable

@Serializable
data class ResetPwRequest(
    var newPassword: String? = null,
    var confirmPassword: String? = null
)