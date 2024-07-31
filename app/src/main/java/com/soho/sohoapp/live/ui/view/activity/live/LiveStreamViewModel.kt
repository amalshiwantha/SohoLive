package com.soho.sohoapp.live.ui.view.activity.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.LiveRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class StateLive(
    val isSuccess: Boolean = false,
    val loadingMessage: String? = null,
    val loadingState: ProgressBarState = ProgressBarState.Idle,
    val alertState: AlertState = AlertState.Idle,
    val errorMsg: String? = null,
)

class LiveStreamViewModel(
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager,
) : ViewModel() {

    private val _mState = MutableStateFlow(StateLive())
    val mState: StateFlow<StateLive> = _mState

    private fun setLoading(message: String, state: ProgressBarState) {
        _mState.value = _mState.value.copy(
            loadingState = state,
            loadingMessage = if (state == ProgressBarState.Loading) message else null
        )
    }

    fun callLiveStreamApi(muxKey: String) {
        viewModelScope.launch {

            val request =
                LiveRequest(
                    platform = listOf("facebook"),
                    streamKey = muxKey,
                    url = "http://",
                    liveStreamId = 12342
                )

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

                        /*if (isSuccess) {
                            mState = mState.copy(isSuccess = true)
                        } else {
                            mState = mState.copy(errorMsg = errorMsg)
                        }*/
                    }
                }

                is ApiState.Loading -> {
                    //mState = mState.copy(loadingState = apiState.progressBarState)
                    setLoading("Connecting to livecast", apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    //mState = mState.copy(alertState = apiState.alertState)
                }
            }
        }.launchIn(viewModelScope)
    }

}