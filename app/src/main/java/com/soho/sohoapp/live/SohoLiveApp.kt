package com.soho.sohoapp.live

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mux.video.upload.MuxUploadSdk
import com.mux.video.upload.api.MuxUploadManager
import com.soho.sohoapp.live.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SohoLiveApp : Application() {

    companion object {
        private lateinit var instance: SohoLiveApp

        val context: Context
            get() = instance.applicationContext

        fun getStringApp(@StringRes stringRes: Int): String {
            return context.getString(stringRes)
        }

        fun Context.getActivity(): ComponentActivity? = when (this) {
            is ComponentActivity -> this
            is ContextWrapper -> baseContext.getActivity()
            else -> null
        }

        private const val CHANNEL_ID = "upload_channel"
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Video Upload Notifications",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Channel for video upload notifications"
                }
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun showDownloadNotification(context: Context, progress: Int, maxProgress: Int) {
            val notiTitle = if (progress == 100) "Video Upload Complete" else "Uploading Video File"
            val notiContent =
                if (progress != 100) "Completed $progress%" else "Your file has been uploaded."

            val notificationManager = NotificationManagerCompat.from(context)
            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(notiTitle)
                .setContentText(notiContent)
                .setSmallIcon(R.drawable.ic_upgrade)
                .setProgress(maxProgress, progress, false)
                .setOnlyAlertOnce(true)

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

    override fun onCreate() {
        super.onCreate()

        instance = this

        startKoin {
            androidContext(this@SohoLiveApp)
            modules(appModule)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "download_channel",
                "File download",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        MuxUploadSdk.initialize(appContext = this)
        MuxUploadManager.resumeAllCachedJobs()
    }
}
