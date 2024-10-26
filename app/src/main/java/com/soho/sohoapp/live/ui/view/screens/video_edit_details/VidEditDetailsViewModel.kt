package com.soho.sohoapp.live.ui.view.screens.video_edit_details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.db.PrivateVideo
import com.soho.sohoapp.live.db.PrivateVideoDao
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.ui.view.screens.pre_rec_library.PreRecLibState
import kotlinx.coroutines.launch

class VidEditDetailsViewModel(private val vidDb: PrivateVideoDao) : ViewModel() {
    val mState: MutableState<PreRecLibState> = mutableStateOf(PreRecLibState())

    fun getLatestItem(pvtVidId: Long) {
        viewModelScope.launch {
            val pvtVideo = vidDb.getVideoById(pvtVidId.toInt())
            if (pvtVideo != null) {
                mState.value = mState.value.copy(
                    privateVideo = mutableStateOf(pvtVideo),
                )
            }
        }
    }

    fun updateDetails(selectedItem: PrivateVideo?) {
        viewModelScope.launch {
            selectedItem?.let { vidDb.updateVideo(it) }
            mState.value = mState.value.copy(isSuccess = true)
        }
    }

    private fun getAllStoreData(): MutableList<PrivateVideo> {
        val tmpFile1 =
            "file:///storage/emulated/0/Movies/SohoPreRecord/SohoLive_20241026_004116.mp4"
        val tmpFile2 =
            "file:///storage/emulated/0/Movies/SohoPreRecord/SohoLive_20241022_085135.mp4"
        val pv1 = PrivateVideo(
            filePath = tmpFile1,
            createdDate = "2024-10-26 12:23:56",
            castFor = "Inspection",
            privacy = VideoPrivacy.PUBLIC.label,
            title = "Just Title",
            description = "Just more description to display"
        )

        val pv2 = PrivateVideo(
            filePath = tmpFile2,
            createdDate = "2024-10-22 10:13:56",
            castFor = "Inspection",
            privacy = VideoPrivacy.PRIVATE.label,
            title = "Just Title Second"
        )

        return mutableListOf(pv1, pv2)
    }
}