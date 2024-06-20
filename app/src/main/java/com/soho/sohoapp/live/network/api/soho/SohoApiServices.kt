package com.soho.sohoapp.live.network.api.soho

import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.network.response.AuthResponse
import com.soho.sohoapp.live.network.response.GoLiveResponse


interface SohoApiServices {

    companion object {
        const val LOGIN = "sessions/login"
        const val PROPERTY_LISTING = "property_listing/data"
    }

    suspend fun login(signInRequest: SignInRequest): AuthResponse
    suspend fun propertyListing(authToken : String): GoLiveResponse
}