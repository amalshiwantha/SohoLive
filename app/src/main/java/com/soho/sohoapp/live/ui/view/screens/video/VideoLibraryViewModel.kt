package com.soho.sohoapp.live.ui.view.screens.video

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.model.VidLibRequest
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class VideoLibraryViewModel(
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager,
) : ViewModel() {

    val mState: MutableState<VideoLibraryState> = mutableStateOf(VideoLibraryState())

    fun onTriggerEvent(event: VidLibEvent) {
        when (event) {
            VidLibEvent.DismissAlert -> {
                mState.value = mState.value.copy(alertState = AlertState.Idle)
            }

            is VidLibEvent.CallLoadVideo -> {
                loadVideoList(event.request)
            }

            else -> {}
        }
    }

    private fun loadVideoList(request: VidLibRequest) {
        mState.value = mState.value.copy(
            loadingState = ProgressBarState.Loading,
            loadingMessage = "Loading Video Library..."
        )

        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {
                    callVideoLibrary(it.authenticationToken, request)
                }
            }
        }
    }

    private fun callVideoLibrary(authToken: String, request: VidLibRequest) {
        apiRepo.getVideoLibrary(authToken, request).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {

                    apiState.data?.let { result ->

                        val isSuccess = !result.responseType.equals("error")
                        val errorMsg = result.response
                        val res = result.data

                        if (isSuccess) {
                            mState.value = mState.value.copy(sApiResponse = mutableStateOf(res))
                            mState.value = mState.value.copy(isSuccess = true)
                        } else {
                            mState.value =
                                mState.value.copy(alertState = AlertState.Display(AlertConfig.VID_LIB_ERROR.apply {
                                    message = errorMsg.orEmpty()
                                }))
                        }
                    }
                }

                is ApiState.Loading -> {
                    mState.value = mState.value.copy(loadingState = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    mState.value = mState.value.copy(alertState = apiState.alertState)
                }
            }
        }.launchIn(viewModelScope)
    }

}