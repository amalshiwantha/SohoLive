package com.soho.sohoapp.live.model

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(var email: String, var password: String)
