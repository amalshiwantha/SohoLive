package com.soho.sohoapp.live.ui.view.screens.video_manage

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.VidPrivacyRequest
import com.soho.sohoapp.live.ui.view.screens.video.VidLibEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class VideoManageViewModel(
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager
) : ViewModel() {

    val mState: MutableState<VideoManageState> = mutableStateOf(VideoManageState())

    fun onTriggerEvent(event: VidLibEvent) {
        when (event) {
            VidLibEvent.DismissAlert -> {
                mState.value = mState.value.copy(alertState = AlertState.Idle)
            }

            is VidLibEvent.CallUpdateVideo -> {
                updateVideoPrivacy(event.request)
            }

            else -> {}
        }
    }

    private fun updateVideoPrivacy(privacyReq: VidPrivacyRequest) {

        mState.value = mState.value.copy(
            loadingState = ProgressBarState.Loading,
            loadingMessage = "Update Video Privacy"
        )

        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {
                    callVideoLibrary(it.authenticationToken, privacyReq)
                }
            }
        }
    }

    private fun callVideoLibrary(authToken: String, privacyReq: VidPrivacyRequest) {
        apiRepo.setVideoPrivacy(authToken, privacyReq).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {

                    apiState.data?.let { result ->

                        val isSuccess = !result.responseType.equals("error")
                        val errorMsg = result.response
                        val responsePrivacy = result.data

                        if (isSuccess) {
                            mState.value =
                                mState.value.copy(updatedPrivacy = mutableStateOf(responsePrivacy.unlisted))
                            mState.value = mState.value.copy(isSuccess = true)
                        } else {
                            mState.value =
                                mState.value.copy(alertState = AlertState.Display(AlertConfig.COMMON_OK.apply {
                                    title = "Privacy Change Problem"
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