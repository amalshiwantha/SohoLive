package com.soho.sohoapp.live.ui.view.screens.review

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.db.PrivateVideo
import com.soho.sohoapp.live.db.PrivateVideoDao
import com.soho.sohoapp.live.db.VideoInfo
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.ui.view.screens.pre_rec_library.PreRecLibState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val vidDb: PrivateVideoDao,
    private val apiRepo: SohoApiRepository,
    private val dataStore: AppDataStoreManager
) : ViewModel() {
    val mState: MutableState<PreRecLibState> = mutableStateOf(PreRecLibState())

    fun getLatestItem(pvtVidId: Long) {
        viewModelScope.launch {
            val pvtVideo = vidDb.getVideoById(pvtVidId.toInt())
            if (pvtVideo != null) {
                mState.value = mState.value.copy(
                    privateVideo = mutableStateOf(pvtVideo),
                    isLoadedItem = true
                )
            }
        }
    }

    fun updateUpload(updatedVideoItem: PrivateVideo?, mGoLiveSubmit: GoLiveSubmit) {
        viewModelScope.launch {
            mState.value = mState.value.copy(isUploading = mutableStateOf(true))

            updatedVideoItem?.let {
                it.videoInfo = mGoLiveSubmit.toVideoInfo()
                vidDb.updateVideo(it)
            }

            dataStore.userProfile.collect { profile ->
                profile?.let { prof ->
                    updatedVideoItem?.videoInfo?.let { vidInfo ->
                        uploadVideoMux(prof.authenticationToken, vidInfo)
                    }
                }
            }
        }
    }

    private fun GoLiveSubmit.toVideoInfo(): VideoInfo {
        return VideoInfo(
            streamType = this.purpose,
            propertyListingId = this.propertyId,
            title = this.title,
            description = this.description,
            agentProfileId = this.agentId ?: 0,
            unlisted = this.isSohoPublic,
            orientation = this.orientation
        )
    }


    private fun uploadVideoMux(authToken: String, videoInfo: VideoInfo) {

        apiRepo.uploadMuxVideo(authToken, videoInfo).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    apiState.data?.let { result ->
                        println("myUplaod Res ${result.data?.uploadUrl}")
                        mState.value =
                            mState.value.copy(isUploading = mutableStateOf(false), isSuccess = true)
                    }
                }

                is ApiState.Loading -> {}

                is ApiState.Alert -> {}
            }
        }.launchIn(viewModelScope)
    }
}