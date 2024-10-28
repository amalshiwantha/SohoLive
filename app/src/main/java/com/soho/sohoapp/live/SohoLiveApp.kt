package com.soho.sohoapp.live

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
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
    }
}
