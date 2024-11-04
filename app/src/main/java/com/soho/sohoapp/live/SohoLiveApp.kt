package com.soho.sohoapp.live

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import com.mux.video.upload.MuxUploadSdk
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
    }
}
