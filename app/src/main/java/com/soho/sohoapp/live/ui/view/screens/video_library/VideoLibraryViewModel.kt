package com.soho.sohoapp.live.ui.view.screens.video_library

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.db.PrivateVideo
import com.soho.sohoapp.live.db.PrivateVideoDao
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.model.VidLibRequest
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import com.soho.sohoapp.live.utility.Const.Companion.ERR_VAL
import com.soho.sohoapp.live.utility.deleteOldRecordedVideos
import com.soho.sohoapp.live.utility.getAllRecordedVideos
import com.soho.sohoapp.live.utility.getForceExitMessage
import com.soho.sohoapp.live.utility.toErrorCode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class VideoLibraryViewModel(
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager,
    private val vidDb: PrivateVideoDao
) : ViewModel() {

    val mState: MutableState<VideoLibraryState> = mutableStateOf(VideoLibraryState())
    var mReqData: VidLibRequest = VidLibRequest()

    init {
        getPvtVidList()
    }

    fun onTriggerEvent(event: VidLibEvent) {
        when (event) {
            VidLibEvent.DismissAlert -> {
                mState.value = mState.value.copy(alertState = AlertState.Idle)
            }

            is VidLibEvent.CallLoadVideo -> {
                getPvtVidList()
                mReqData = event.request
                loadVideoList(mReqData)
            }

            else -> {}
        }
    }

    fun reLoadData() {
        getPvtVidList()
        loadVideoList(mReqData)
    }

    private fun loadVideoList(request: VidLibRequest) {
        mState.value = mState.value.copy(
            loadingState = ProgressBarState.Loading,
            loadingMessage = "Loading Video Library..."
        )

        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {
                    //Check ActivePlan and call callVideoLibrary
                    apiRepo.getCurrentPlan(it.authenticationToken)
                        .onEach { apiState ->
                            when (apiState) {
                                is ApiState.Data -> {
                                    apiState.data?.let { activeRes ->
                                        if (!activeRes.responseType.equals(ERR_VAL)) {
                                            //call callVideoLibrary
                                            callVideoLibrary(it.authenticationToken, request)
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

    private fun getPvtVidList() {
        viewModelScope.launch {
            val displayList: MutableList<PrivateVideo> = mutableListOf()

            //Remove 30 days old records
            vidDb.deleteOldVideos()
            deleteOldRecordedVideos()

            //get all db saved data
            val dbSaveData = vidDb.getAllVideos()

            //get all raw video files
            val rawFiles = getAllRecordedVideos()

            /*
            * find dbSaved file locally avaliable or not.
            * if have then add to the displayList
            * */
            rawFiles.forEach {
                val rawFileName = it.name
                val savedFile = dbSaveData.find { savedData ->
                    savedData.filePath.endsWith(rawFileName)
                }

                savedFile?.let { avaliableFile ->
                    displayList.add(avaliableFile)
                }
            }

            mState.value = mState.value.copy(isHasPvtVid = mutableStateOf(displayList.isNotEmpty()))
        }
    }
}