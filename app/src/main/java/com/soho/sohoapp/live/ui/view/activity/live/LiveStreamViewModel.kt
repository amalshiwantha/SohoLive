package com.soho.sohoapp.live.ui.view.activity.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository

class LiveStreamViewModel(
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager,
) : ViewModel() {

    fun appInBackground() {
        println("myStream appInBackground ")
        apiRepo.submitGoLive("", GoLiveSubmit())
    }

}