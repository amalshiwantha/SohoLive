package com.soho.sohoapp.live.network.api.soho

import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.model.TsPropertyRequest
import com.soho.sohoapp.live.network.core.BASE_URL
import com.soho.sohoapp.live.network.core.BASE_URL_TS
import com.soho.sohoapp.live.network.core.TS_API_KEY
import com.soho.sohoapp.live.network.response.AuthResponse
import com.soho.sohoapp.live.network.response.GoLiveResponse
import com.soho.sohoapp.live.network.response.TsPropertyResponse
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

    override suspend fun tsProperty(tsPropRequest: TsPropertyRequest): TsPropertyResponse {
        return httpClient.get {
            url {
                takeFrom(BASE_URL_TS)
                encodedPath += SohoApiServices.TS_PROPERTY
                parameters.apply {
                    tsPropRequest.query?.let { append("q", it) }
                    tsPropRequest.queryBy?.let { append("query_by", it) }
                    tsPropRequest.filterBy?.let { append("filter_by", it) }
                    tsPropRequest.perPage?.let { append("per_page", it) }
                    tsPropRequest.page?.let { append("page", it) }
                }
            }
            contentType(ContentType.Application.Json)
            header("X-TYPESENSE-API-KEY", TS_API_KEY)
        }.body()
    }
}