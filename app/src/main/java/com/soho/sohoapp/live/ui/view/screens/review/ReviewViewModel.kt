package com.soho.sohoapp.live.ui.view.screens.review

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.db.PrivateVideo
import com.soho.sohoapp.live.db.PrivateVideoDao
import com.soho.sohoapp.live.db.VideoInfo
import com.soho.sohoapp.live.enums.VideoPrivacy
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
                    fileUrl = pvtVideo.filePath,
                    isLoadedItem = true
                )
            }
        }
    }

    fun updateUpload(updatedVideoItem: PrivateVideo?, mGoLiveSubmit: GoLiveSubmit?) {
        viewModelScope.launch {
            mState.value = mState.value.copy(isUploading = mutableStateOf(true))

            //update local DB
            updatedVideoItem?.let { vidItem ->
                //if having mGoLiveSubmit have to save
                mGoLiveSubmit?.let {
                    vidItem.videoInfo = mGoLiveSubmit.toVideoInfo()
                }

                //update unlisted state according to the vidItem.privacy
                vidItem.videoInfo?.apply {
                    unlisted = when (vidItem.privacy) {
                        VideoPrivacy.UNLISTED.label -> true
                        VideoPrivacy.PRIVATE.label -> false
                        VideoPrivacy.PUBLIC.label -> false
                        else -> false
                    }
                }

                //save video info
                vidDb.updateVideo(vidItem)

                //get upload url
                if (vidItem.privacy != VideoPrivacy.PRIVATE.label) {
                    dataStore.userProfile.collect { profile ->
                        profile?.let { prof ->
                            vidItem.videoInfo?.let { vidInfo ->
                                uploadVideoMux(prof.authenticationToken, vidInfo)
                            }
                        }
                    }
                } else {
                    mState.value =
                        mState.value.copy(
                            isUploading = mutableStateOf(false),
                            isDone = true
                        )
                }
            }
        }
    }

    private fun uploadVideoMux(authToken: String, videoInfo: VideoInfo) {

        apiRepo.uploadMuxVideo(authToken, videoInfo).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    apiState.data?.let { result ->
                        val uploadUrl = result.data?.uploadUrl

                        mState.value =
                            mState.value.copy(
                                uploadUrl = uploadUrl,
                                isUploading = mutableStateOf(false),
                                isSuccess = true
                            )
                    }
                }

                is ApiState.Loading -> {}

                is ApiState.Alert -> {}
            }
        }.launchIn(viewModelScope)
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
}