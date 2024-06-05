package com.soho.sohoapp.live.model

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(var email: String? = null, var password: String? = null)