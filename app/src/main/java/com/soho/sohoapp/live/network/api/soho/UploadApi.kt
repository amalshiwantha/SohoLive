package com.soho.sohoapp.live.network.api.soho

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import java.io.File

class UploadApi {
    suspend fun uploadVideo(authToken: String, videoFile: File, onProgress: (Int) -> Unit): String {
        return HttpClient(CIO).submitFormWithBinaryData(
            url = "http://intbuy.ceylonapz.com/dev/upload.php",
            formData = formData {
                append("videoFile", videoFile.readBytes(), Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=\"${videoFile.name}\"")
                })
            }
        ) {
            headers {
                append(HttpHeaders.Authorization, authToken)
                append(HttpHeaders.ContentType, ContentType.MultiPart.FormData.toString())
            }
            onUpload { bytesSentTotal, contentLength ->
                if (contentLength != 0L) {
                    val progress = (bytesSentTotal * 100 / contentLength).toInt()
                    onProgress(progress)
                }
            }
        }.bodyAsText()
    }
}