package com.soho.sohoapp.live

import android.app.Application
import com.soho.sohoapp.live.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SohoLiveApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SohoLiveApp)
            modules(appModule)
        }
    }
}
