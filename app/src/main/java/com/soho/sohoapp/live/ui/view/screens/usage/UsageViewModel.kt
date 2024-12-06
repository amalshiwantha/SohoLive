package com.soho.sohoapp.live.ui.view.screens.usage

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import com.soho.sohoapp.live.utility.Const.Companion.ERR_VAL
import com.soho.sohoapp.live.utility.getForceExitMessage
import com.soho.sohoapp.live.utility.toErrorCode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UsageViewModel(
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager
) : ViewModel() {

    val mState: MutableState<UsageState> = mutableStateOf(UsageState())

    fun loadUsage() {
        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let { prof ->

                    //Check ActivePlan and call getStorageUsage
                    apiRepo.getCurrentPlan(prof.authenticationToken)
                        .onEach { apiState ->
                            when (apiState) {
                                is ApiState.Data -> {
                                    apiState.data?.let { activeRes ->
                                        if (!activeRes.responseType.equals(ERR_VAL)) {
                                            //call getStorageUsage
                                            getStorageUsage(prof.authenticationToken)
                                        } else {
                                            //ForceLogout Now
                                            val forceExit =
                                                getForceExitMessage(activeRes.response?.toErrorCode())
                                            AppEventBus.sendEvent(AppEvent.ForceLogout(forceExit))
                                        }
                                    }
                                }

                                else -> {}
                            }
                        }.launchIn(viewModelScope)


                }
            }
        }
    }

    private fun getStorageUsage(authToken: String) {
        apiRepo.getUsage(authToken).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    apiState.data?.let { result ->
                        /*mState.value = mState.value.copy(
                            planListRes = result.data,
                            isSuccess = result.data.isNotEmpty()
                        )*/
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