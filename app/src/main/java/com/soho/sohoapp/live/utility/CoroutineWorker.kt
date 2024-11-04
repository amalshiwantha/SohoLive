package com.soho.sohoapp.live.utility

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import kotlinx.coroutines.delay
import java.io.File
import kotlin.random.Random

class VideoUploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = runCatching {

        val filePath = inputData.getString(FILE_URI_TO_UPLOAD) ?: return Result.failure()
        val file = File(filePath)
        println("myUpload file ${file.exists()}")

        if (!file.exists()) return Result.failure()
        println("myUpload file $filePath")

        uploadFile(file)
        Result.success()

    }.getOrElse {
        return@getOrElse when (it) {
            is UploadingFailedException.Failed -> {
                sendNotification("Error: ${it.message}")
                Result.retry()
            }

            is UploadingFailedException.FileNotFound -> {
                sendNotification("Error: ${it.message}")
                Result.failure()
            }

            else -> {
                sendNotification("Upload Failed! Unknown Error")
                Result.failure()
            }
        }
    }

    private suspend fun uploadFile(uri: File?) {

        if (uri == null) throw UploadingFailedException.FileNotFound

        sendNotification("Uploading File")
        for (i in 0..100) {
            delay(1000)
            println("myUpload prog $i")
        }
        sendNotification("Uploaded Successfully")
    }

    private fun sendNotification(message: String) {
        NotificationHelper().createNotification(
            title = "File Uploader",
            message = message
        )
    }

    companion object {
        const val FILE_URI_TO_UPLOAD = "imageUriToUpload"
    }
}

sealed class UploadingFailedException(override val message: String) : Exception(message) {
    object FileNotFound : UploadingFailedException(message = "File not found! try again.")
    object Failed :
        UploadingFailedException(message = "Upload File Failed! will try again in 10 seconds")
}
