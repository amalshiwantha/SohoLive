package com.soho.sohoapp.live.network.api.soho

import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.network.core.BASE_URL
import com.soho.sohoapp.live.network.response.AuthResponse
import com.soho.sohoapp.live.network.response.GoLiveResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom

class SohoServicesImpl(private val httpClient: HttpClient) : SohoApiServices {
    override suspend fun login(signInRequest: SignInRequest): AuthResponse {
        return httpClient.post {
            url {
                takeFrom(BASE_URL)
                encodedPath += SohoApiServices.LOGIN
            }
            contentType(ContentType.Application.Json)
            setBody(signInRequest)
        }.body()
    }

    override suspend fun propertyListing(authToken: String): GoLiveResponse {
        return httpClient.get {
            url {
                takeFrom(BASE_URL)
                encodedPath += SohoApiServices.PROPERTY_LISTING
            }
            contentType(ContentType.Application.Json)
            header("Authorization", authToken)
        }.body()
    }
}