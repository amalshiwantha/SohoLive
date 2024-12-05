package com.soho.sohoapp.live.ui.view.screens.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.ApiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SubscriptionViewModel(
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager
) : ViewModel() {

    fun loadPlans() {
        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let { prof ->
                    getSubsPlans(prof.authenticationToken)
                }
            }
        }
    }

    private fun getSubsPlans(authToken: String) {

        apiRepo.getSubscriptionPlans(authToken).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    println("mySubs :  Data :: ${apiState.data}")
                    apiState.data?.let { result ->
                        val res = result.data
                    }
                }

                is ApiState.Alert -> {}
                is ApiState.Loading -> {
                    //liveState.value = liveState.value.copy(loadingState = apiState.progressBarState)
                    println("mySubs :  Loading :: ${apiState.progressBarState}")
                }
            }
        }.launchIn(viewModelScope)
    }

}