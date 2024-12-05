package com.soho.sohoapp.live.ui.view.screens.subscription

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SubscriptionViewModel(
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager
) : ViewModel() {

    val mState: MutableState<SubscriptionState> = mutableStateOf(SubscriptionState())

    fun loadPlans() {
        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let { prof ->
                    getActivePlan(prof.authenticationToken)
                }
            }
        }
    }

    private fun getActivePlan(authToken: String) {
        apiRepo.getCurrentPlan(authToken).onEach { apiState ->
            when (apiState) {
                is ApiState.Data -> {
                    println("mySubs :  ActPlan :: ${apiState.data}")
                    getSubsPlans(authToken)
                }

                is ApiState.Alert -> {}
                is ApiState.Loading -> {
                    mState.value = mState.value.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getSubsPlans(authToken: String) {
        apiRepo.getSubscriptionPlans(authToken).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    apiState.data?.let { result ->
                        mState.value = mState.value.copy(
                            planListRes = result.data,
                            isSuccess = result.data.isNotEmpty()
                        )
                    }
                }

                is ApiState.Alert -> {}
                is ApiState.Loading -> {
                    mState.value =
                        mState.value.copy(isLoading = apiState.progressBarState == ProgressBarState.Loading)
                }
            }
        }.launchIn(viewModelScope)
    }

}