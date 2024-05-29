package com.soho.sohoapp.live.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val candidates: List<Candidate>
)

@Serializable
data class Candidate(
    @SerialName("content") val content: Contents,
)

@Serializable
data class Contents(
    @SerialName("parts") val parts: List<Parts>
)

@Serializable
data class Parts(
    val text: String
)
