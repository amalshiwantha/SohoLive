package com.soho.sohoapp.live.network.api.soho

import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.model.TsPropertyRequest
import com.soho.sohoapp.live.model.VidLibRequest
import com.soho.sohoapp.live.network.core.BASE_URL
import com.soho.sohoapp.live.network.core.BASE_URL_TS
import com.soho.sohoapp.live.network.core.TS_API_KEY
import com.soho.sohoapp.live.network.response.AuthResponse
import com.soho.sohoapp.live.network.response.GoLiveResponse
import com.soho.sohoapp.live.network.response.GoLiveSubmitResponse
import com.soho.sohoapp.live.network.response.LiveEndRequest
import com.soho.sohoapp.live.network.response.LiveRequest
import com.soho.sohoapp.live.network.response.LiveResponse
import com.soho.sohoapp.live.network.response.TsPropertyResponse
import com.soho.sohoapp.live.network.response.VidLibResponse
import com.soho.sohoapp.live.network.response.VidPrivacyRequest
import com.soho.sohoapp.live.network.response.VidPrivacyResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
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

    override suspend fun goLive(authToken: String, goLiveData: GoLiveSubmit): GoLiveSubmitResponse {
        return httpClient.post {
            url {
                takeFrom(BASE_URL)
                encodedPath += SohoApiServices.GO_LIVE
            }
            contentType(ContentType.Application.Json)
            header("Authorization", authToken)
            setBody(goLiveData)
        }.body()
    }

    override suspend fun goLiveSchedule(
        authToken: String,
        goLiveData: GoLiveSubmit,
        propertyId: String
    ): GoLiveSubmitResponse {
        return httpClient.post {
            url {
                takeFrom(BASE_URL)
                encodedPath += SohoApiServices.PROPERTY_SCHEDULE.replace("{propertyId}", propertyId)
            }
            contentType(ContentType.Application.Json)
            header("Authorization", authToken)
            setBody(goLiveData)
        }.body()
    }

    override suspend fun videoLibrary(authToken: String, request: VidLibRequest): VidLibResponse {
        return httpClient.get {
            url {
                takeFrom(BASE_URL)
                encodedPath += SohoApiServices.VIDEO_LIBRARY
                parameters.apply {
                    append("page", request.page.toString())
                    append("per_page", request.perPage.toString())
                    append("sort_by", request.sortBy)
                    append("sort_order", request.sortOrder)
                }
            }
            contentType(ContentType.Application.Json)
            header("Authorization", authToken)
        }.body()
    }

    override suspend fun videoPrivacyUpdate(
        authToken: String,
        privacyReq: VidPrivacyRequest
    ): VidPrivacyResponse {

        return httpClient.put {
            url {
                takeFrom(BASE_URL)
                encodedPath += SohoApiServices.VIDEO_PRIVACY_UPDATE.replace(
                    "{propertyId}",
                    privacyReq.videoId.toString()
                )
            }
            contentType(ContentType.Application.Json)
            header("Authorization", authToken)
            setBody(privacyReq)
        }.body()
    }

    override suspend fun endStream(authToken: String, streamId: String): LiveResponse {
        val liveEndReq = LiveEndRequest(streamId)
        return httpClient.put {
            url {
                takeFrom(BASE_URL)
                encodedPath += SohoApiServices.END_STREAM.replace(
                    "{live_stream_id}",
                    streamId
                )
            }
            contentType(ContentType.Application.Json)
            header("Authorization", authToken)
            setBody(liveEndReq)
        }.body()
    }

    override suspend fun rollBackStream(authToken: String, liveReq: LiveRequest): LiveResponse {
        return httpClient.delete {
            url {
                takeFrom(BASE_URL)
                encodedPath += SohoApiServices.ROLLBACK_STREAM.replace(
                    "{live_stream_id}",
                    liveReq.liveStreamId
                )
            }
            contentType(ContentType.Application.Json)
            header("Authorization", authToken)
            setBody(liveReq)
        }.body()
    }
}