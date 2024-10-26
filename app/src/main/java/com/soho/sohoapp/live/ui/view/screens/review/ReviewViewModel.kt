package com.soho.sohoapp.live.ui.view.screens.review

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.db.PrivateVideo
import com.soho.sohoapp.live.db.PrivateVideoDao
import com.soho.sohoapp.live.ui.view.screens.pre_rec_library.PreRecLibState
import kotlinx.coroutines.launch

class ReviewViewModel(private val vidDb: PrivateVideoDao) : ViewModel() {
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

    fun updateDetails(selectedItem: PrivateVideo?) {
        viewModelScope.launch {
            selectedItem?.let { vidDb.updateVideo(it) }
            mState.value = mState.value.copy(isSuccess = true)
        }
    }
}