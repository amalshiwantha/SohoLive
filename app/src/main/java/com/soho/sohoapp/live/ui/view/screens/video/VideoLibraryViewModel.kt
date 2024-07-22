package com.soho.sohoapp.live.ui.view.screens.video

import androidx.lifecycle.ViewModel
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository

class VideoLibraryViewModel(
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager,
) : ViewModel() {

}