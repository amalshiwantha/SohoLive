package com.soho.sohoapp.live.network.api.soho

import com.soho.sohoapp.live.network.core.BASE_URL
import com.soho.sohoapp.live.model.LoginRequest
import com.soho.sohoapp.live.network.response.AuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom

class SohoServicesImpl(private val httpClient: HttpClient) : SohoApiServices {
    override suspend fun login(loginRequest: LoginRequest): AuthResponse {
        return httpClient.post {
            url {
                takeFrom(BASE_URL)
                encodedPath += SohoApiServices.LOGIN
            }
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }.body()
    }

    override suspend fun signup(signupModel: String): AuthResponse {
        TODO("Not yet implemented")
    }

    override suspend fun forgetPassword(email: String): AuthResponse {
        TODO("Not yet implemented")
    }
}