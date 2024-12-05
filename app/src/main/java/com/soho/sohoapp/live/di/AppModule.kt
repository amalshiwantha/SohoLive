package com.soho.sohoapp.live.di

import androidx.room.Room
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.db.AppDatabase
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.api.soho.SohoApiServices
import com.soho.sohoapp.live.network.api.soho.SohoServicesImpl
import com.soho.sohoapp.live.network.core.KtorHttpClient
import com.soho.sohoapp.live.ui.view.activity.live.LiveStreamViewModel
import com.soho.sohoapp.live.ui.view.activity.main.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveViewModel
import com.soho.sohoapp.live.ui.view.screens.pre_rec_library.PreRecLibraryViewModel
import com.soho.sohoapp.live.ui.view.screens.profile.ProfileViewModel
import com.soho.sohoapp.live.ui.view.screens.review.ReviewViewModel
import com.soho.sohoapp.live.ui.view.screens.schedule.ScheduleViewModel
import com.soho.sohoapp.live.ui.view.screens.signin.SignInViewModel
import com.soho.sohoapp.live.ui.view.screens.splash.SplashViewModel
import com.soho.sohoapp.live.ui.view.screens.subscription.SubscriptionViewModel
import com.soho.sohoapp.live.ui.view.screens.video_edit_details.VidEditDetailsViewModel
import com.soho.sohoapp.live.ui.view.screens.video_library.VideoLibraryViewModel
import com.soho.sohoapp.live.ui.view.screens.video_manage.VideoManageViewModel
import com.soho.sohoapp.live.ui.view.screens.video_recorder.VideoRecorderViewModel
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
    // Room Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "soho_live_db"
        ).build()
        //.addMigrations(MIGRATION_1_2)
    }
    single { get<AppDatabase>().privateVideoDao() }

    //ViewModels
    viewModel { SplashViewModel(get()) }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { GoLiveViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get(), get()) }
    viewModel { VideoLibraryViewModel(get(), get()) }
    viewModel { VideoManageViewModel(get(), get()) }
    viewModel { LiveStreamViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { PreRecLibraryViewModel(get()) }
    viewModel { VidEditDetailsViewModel(get()) }
    viewModel { VideoRecorderViewModel(get()) }
    viewModel { ReviewViewModel(get(), get(), get()) }
    viewModel { SubscriptionViewModel(get(),get()) }
}