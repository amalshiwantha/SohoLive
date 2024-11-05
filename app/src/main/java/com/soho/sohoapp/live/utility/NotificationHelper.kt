package com.soho.sohoapp.live.utility

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context

class NotificationHelper {

    companion object {
        private const val CHANNEL_ID = "video_uploader_channel"
        private const val CHANNEL_NAME = "Video Upload Notifications"
        private const val CHANNEL_DESCRIPTION =
            "Notifications for file upload progress and completion"
    }

    init {
        createNotificationChannel()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, progress: Int) {
        val title = if (progress == 100) "Video Upload Complete" else "Uploading Video File"
        val content =
            if (progress != 100) "Completed $progress%" else "Your file has been uploaded."

        val notificationManager = NotificationManagerCompat.from(context)
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_upgrade)
            .setProgress(100, progress, false)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)  // Ensures higher visibility
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)  // Shows on lock screen

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(1, notificationBuilder.build())
    }
}
