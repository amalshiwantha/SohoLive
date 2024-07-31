package com.soho.sohoapp.live.ui.view.activity.live

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.LiveRequest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class StateLive(
    val isSuccess: Boolean = false,
    val isLoading: ProgressBarState = ProgressBarState.Idle,
    val alertState: AlertState = AlertState.Idle,
    val errorMsg: String? = null,
)

class LiveStreamViewModel(
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager,
) : ViewModel() {

    val mState = mutableStateOf(StateLive())

    fun callLiveStreamApi(muxKey: String) {
        /*mState.value = mState.value.copy(
            loadingState = ProgressBarState.Loading,
            loadingMessage = context.getString(R.string.requesting_golive)
        )*/

        val request =
            LiveRequest(
                platform = listOf("facebook"),
                streamKey = muxKey,
                url = "http://",
                liveStreamId = 12342
            )

        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {
                    onAirLiveStream(it.authenticationToken, request)
                }
            }
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
                            mState.value = mState.value.copy(isSuccess = true)
                        } else {
                            mState.value = mState.value.copy(errorMsg = errorMsg)
                        }
                    }
                }

                is ApiState.Loading -> {
                    mState.value = mState.value.copy(isLoading = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    mState.value = mState.value.copy(alertState = apiState.alertState)
                }
            }
        }.launchIn(viewModelScope)
    }

}