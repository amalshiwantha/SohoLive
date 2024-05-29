package com.soho.sohoapp.live.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val result: String? = null,
    val status: Boolean = false,
    val alert: Alert
)

@Serializable
data class Alert(
    val title: String,
    val message: String
)