package com.soho.sohoapp.live.utility

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.soho.sohoapp.live.network.api.soho.UploadApi
import java.io.File

class VideoUploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = runCatching {

        val filePath = inputData.getString(FILE_URI_TO_UPLOAD) ?: return Result.failure()
        val file = File(filePath)

        if (!file.exists()) return Result.failure()

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

    private suspend fun uploadFile(file: File?) {

        if (file == null) throw UploadingFailedException.FileNotFound

        sendNotification("Uploading File")

        UploadApi().uploadVideo("au", file, onProgress = {
            println("myUpload prog $it")
        })

        sendNotification("Uploaded Successfully")
    }

    private fun sendNotification(message: String) {
        NotificationHelper().createNotification(
            title = "Private Video Uploader",
            message = message
        )
    }

    companion object {
        const val FILE_URI_TO_UPLOAD = "imageUriToUpload"
    }
}

sealed class UploadingFailedException(override val message: String) : Exception(message) {
    data object FileNotFound : UploadingFailedException(message = "File not found! try again.")
    data object Failed :
        UploadingFailedException(message = "Upload File Failed! will try again in 10 seconds")
}
