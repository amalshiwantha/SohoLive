package com.soho.sohoapp.live.di

import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.network.core.KtorHttpClient
import com.soho.sohoapp.live.view.screens.splash.SplashViewModel
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Json { isLenient = true; ignoreUnknownKeys = true } }
    single { KtorHttpClient.httpClient() }
    single { AppDataStoreManager(androidContext()) }
    viewModel { SplashViewModel(get()) }
}