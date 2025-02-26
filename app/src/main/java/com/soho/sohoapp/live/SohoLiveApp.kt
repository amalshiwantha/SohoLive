package com.soho.sohoapp.live

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mux.video.upload.MuxUploadSdk
import com.mux.video.upload.api.MuxUploadManager
import com.soho.sohoapp.live.di.appModule
import com.soho.sohoapp.live.utility.ZendeskClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SohoLiveApp : Application() {

    companion object {
        private lateinit var instance: SohoLiveApp
        lateinit var zendeskClient: ZendeskClient
        lateinit var analytics: FirebaseAnalytics

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

        FirebaseApp.initializeApp(context)
        analytics = Firebase.analytics

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

        zendeskClient = ZendeskClient.getInstance(context)
    }
}
