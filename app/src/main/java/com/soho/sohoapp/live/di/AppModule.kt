package com.soho.sohoapp.live.di

import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.api.soho.SohoApiServices
import com.soho.sohoapp.live.network.api.soho.SohoServicesImpl
import com.soho.sohoapp.live.network.core.KtorHttpClient
import com.soho.sohoapp.live.ui.view.activity.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveViewModel
import com.soho.sohoapp.live.ui.view.screens.schedule.ScheduleViewModel
import com.soho.sohoapp.live.ui.view.screens.signin.SignInViewModel
import com.soho.sohoapp.live.ui.view.screens.splash.SplashViewModel
import com.soho.sohoapp.live.utility.NetworkUtils
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Serialization
    single { Json { isLenient = true; ignoreUnknownKeys = true } }
    // Network
    single { KtorHttpClient.httpClient() }
    single { NetworkUtils(get()) }
    //DataStore
    single { AppDataStoreManager(androidContext()) }
    //Services
    single<SohoApiServices> { SohoServicesImpl(get()) }
    //Repositories
    single { SohoApiRepository(get()) }

    //ViewModels
    viewModel { SplashViewModel(get()) }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { GoLiveViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ScheduleViewModel(get(), get()) }
}