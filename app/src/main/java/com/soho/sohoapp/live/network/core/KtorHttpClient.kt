package com.soho.sohoapp.live.network.core

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object KtorHttpClient {

    @OptIn(ExperimentalSerializationApi::class)
    fun httpClient() = HttpClient {

        install(HttpTimeout) {
            val timeout = 60000L
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }

        install(ContentNegotiation) {
            json(Json {
                explicitNulls = false
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
                encodeDefaults = true
                classDiscriminator = "#class"
            })
        }

        install(ResponseObserver) {
            onResponse { response ->
                println("AppDebug ResObs : ${response.status.value}")
            }
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL

            logger = object : Logger {
                override fun log(message: String) {
                    println("AppDebug : $message")
                }
            }
        }

        HttpResponseValidator {
            validateResponse { response: HttpResponse ->
                val statusCode = response.status.value
                if (statusCode !in 200..299) {
                    throw Exception("AppDebug Error : $statusCode")
                }
            }
        }


    }

}