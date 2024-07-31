package com.soho.sohoapp.live.ui.view.activity.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.model.AlertData
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.LiveRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LiveStreamViewModel(
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager,
) : ViewModel() {

    private val _msLoading = MutableStateFlow(false)
    val msLoading: StateFlow<Boolean> = _msLoading

    private val _msAlert = MutableStateFlow(AlertData())
    val msAlert: StateFlow<AlertData> = _msAlert

    private val _msStartLiveSuccess = MutableStateFlow(false)
    val msStartLiveSuccess: StateFlow<Boolean> = _msStartLiveSuccess

    fun resetStates() {
        _msLoading.value = false
        _msAlert.value = AlertData()
    }

    fun callLiveStreamApi(liveReq: LiveRequest?) {
        liveReq?.let { liveReqData ->
            viewModelScope.launch {
                dataStore.userProfile.collect { profile ->
                    profile?.let {
                        onAirLiveStream(it.authenticationToken, liveReqData)
                    }
                }
            }
        } ?: run {
            _msAlert.value =
                AlertData(isShow = true, title = "Error", message = "Stream key is empty")
        }
    }

    private fun onAirLiveStream(authToken: String, liveReq: LiveRequest) {
        apiRepo.onAirLiveCast(authToken, liveReq).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    apiState.data?.let { result ->

                        val isSuccess = !result.responseType.equals("error")
                        val errorMsg = result.response
                        //val responsePrivacy = result.data

                        if (isSuccess) {
                            _msStartLiveSuccess.value = true
                        } else {
                            _msAlert.value =
                                AlertData(isShow = true, title = "Error", message = errorMsg)
                        }
                    }
                }

                is ApiState.Loading -> {
                    _msLoading.value = apiState.progressBarState == ProgressBarState.Loading
                }

                is ApiState.Alert -> {
                    _msAlert.value = AlertData(
                        isShow = true,
                        title = "Problem",
                        message = "Something went wrong, Please try again."
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}