package com.soho.sohoapp.live.utility

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.mux.video.upload.api.MuxUpload
import kotlinx.coroutines.*
import java.io.File

class UploadService : Service() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    companion object {
        const val CHANNEL_ID = "upload_channel"
        const val CHANNEL_NAME = "Video Upload"
        const val NOTIFICATION_ID = 1

        const val EXTRA_FILE_PATH = "file_path"
        const val EXTRA_UPLOAD_URL = "upload_url"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filePath = intent?.getStringExtra(EXTRA_FILE_PATH)
        val uploadUrl = intent?.getStringExtra(EXTRA_UPLOAD_URL)

        if (filePath != null && uploadUrl != null) {
            startForeground(NOTIFICATION_ID, createNotification("Uploading video..."))
            startUpload(File(filePath), uploadUrl)
        } else {
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun startUpload(recFile: File, uploadUrl: String) {
        // Launch a coroutine on a background thread (IO dispatcher)
        serviceScope.launch(Dispatchers.IO) {
            val muxUpload = MuxUpload.Builder(uploadUrl, recFile).build()

            muxUpload.setProgressListener { progress ->
                // Update progress on the main thread
                val percentage = if (progress.totalBytes > 0) {
                    (progress.bytesUploaded.toFloat() / progress.totalBytes.toFloat()) * 100
                } else {
                    0f
                }
                // UI updates must happen on the main thread
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        updateNotification("Upload Progress: ${percentage.toInt()}%")
                    }
                }
            }

            muxUpload.setResultListener { result ->
                // Handle the result on the main thread
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        if (result.isSuccess) {
                            updateNotification("Upload Complete")
                        } else {
                            updateNotification("Upload Failed")
                        }
                    }
                }

                stopSelf()
            }

            // Start the upload on the main thread
            withContext(Dispatchers.Main) {
                muxUpload.start()
            }
        }
    }

    private fun createNotification(contentText: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Video Upload")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_menu_upload)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun updateNotification(contentText: String) {
        val notification = createNotification(contentText)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}