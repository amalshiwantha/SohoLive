package com.soho.sohoapp.live.ui.view.activity.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.model.AlertData
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.LiveRequest
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import com.soho.sohoapp.live.utility.Const.Companion.ERR_VAL
import com.soho.sohoapp.live.utility.getForceExitMessage
import com.soho.sohoapp.live.utility.toErrorCode
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

    private val _msEndCast = MutableStateFlow(false)
    val msEndCast: StateFlow<Boolean> = _msEndCast

    private val _msRollBackCast = MutableStateFlow(false)
    val msRollBackCast: StateFlow<Boolean> = _msRollBackCast

    /*
    * this will call end of the liveCast End button Pressed
    * */
    fun completeLiveStream(streamId: String) {
        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {

                    //Check ActivePlan and call onEndLiveStream
                    apiRepo.getCurrentPlan(it.authenticationToken)
                        .onEach { apiState ->
                            when (apiState) {
                                is ApiState.Data -> {
                                    apiState.data?.let { activeRes ->
                                        if (!activeRes.responseType.equals(ERR_VAL)) {
                                            //call EndLiveStream
                                            onEndLiveStream(it.authenticationToken, streamId)
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


                } ?: run {
                    _msAlert.value =
                        AlertData(isShow = true, title = "Error", message = "User not logged")
                }
            }
        }
    }

    private fun onEndLiveStream(authToken: String, streamId: String) {
        apiRepo.onEndLiveCast(authToken, streamId).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    apiState.data?.let { result ->

                        val isSuccess = !result.responseType.equals("error")
                        val errorMsg = result.response
                        //val responsePrivacy = result.data

                        if (isSuccess) {
                            _msEndCast.value = true
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

    /*
    * this api will call when close the live screen without start the streaming (cross & back button click)
    * */
    fun rollbackLiveStream(reqLive: LiveRequest) {
        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {

                    //Check ActivePlan and call uploadVideoMux
                    apiRepo.getCurrentPlan(it.authenticationToken)
                        .onEach { apiState ->
                            when (apiState) {
                                is ApiState.Data -> {
                                    apiState.data?.let { activeRes ->
                                        if (!activeRes.responseType.equals(ERR_VAL)) {
                                            //call RollBackLiveStream
                                            onRollBackLiveStream(it.authenticationToken, reqLive)
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

                } ?: run {
                    _msAlert.value =
                        AlertData(isShow = true, title = "Error", message = "User not logged")
                }
            }
        }
    }

    private fun onRollBackLiveStream(
        authToken: String,
        liveReq: LiveRequest
    ) {
        apiRepo.onRollBackLiveCast(authToken, liveReq).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    apiState.data?.let { result ->

                        val isSuccess = !result.responseType.equals("error")
                        val errorMsg = result.response
                        //val responsePrivacy = result.data

                        if (isSuccess) {
                            _msRollBackCast.value = true
                        } else {
                            _msAlert.value =
                                AlertData(isShow = true, title = "Error", message = errorMsg)

                            //todo this is for temp
                            _msRollBackCast.value = true
                            //todo this is for temp end
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
                        message = "Something went wrong on live cast cancel, Please try again."
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    /*
    * reset fun call state
    * */
    fun resetStates() {
        _msLoading.value = false
        _msAlert.value = AlertData()
    }
}