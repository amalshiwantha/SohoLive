package com.soho.sohoapp.live.network.api.soho

import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.model.TsPropertyRequest
import com.soho.sohoapp.live.model.VidLibRequest
import com.soho.sohoapp.live.network.response.AuthResponse
import com.soho.sohoapp.live.network.response.GoLiveResponse
import com.soho.sohoapp.live.network.response.GoLiveSubmitResponse
import com.soho.sohoapp.live.network.response.TsPropertyResponse
import com.soho.sohoapp.live.network.response.VidLibResponse


interface SohoApiServices {

    companion object {
        const val LOGIN = "sessions/login"
        const val PROPERTY_LISTING = "property_listing/data"
        const val PROPERTY_SCHEDULE = "property_listing/{propertyId}/schedules"
        const val GO_LIVE = "live_stream"
        const val TS_PROPERTY = "collections/property_listings/documents/search"
        const val VIDEO_LIBRARY = "asset"
    }

    suspend fun login(signInRequest: SignInRequest): AuthResponse
    suspend fun propertyListing(authToken: String): GoLiveResponse
    suspend fun tsProperty(tsPropRequest: TsPropertyRequest): TsPropertyResponse
    suspend fun goLive(authToken: String, goLiveData: GoLiveSubmit): GoLiveSubmitResponse
    suspend fun goLiveSchedule(authToken: String, goLiveData: GoLiveSubmit, propertyId : String): GoLiveSubmitResponse
    suspend fun videoLibrary(authToken: String, request: VidLibRequest): VidLibResponse
}