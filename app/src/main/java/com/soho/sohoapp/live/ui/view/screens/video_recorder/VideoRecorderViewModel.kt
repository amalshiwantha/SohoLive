package com.soho.sohoapp.live.ui.view.screens.video_recorder

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.db.PrivateVideoDao
import com.soho.sohoapp.live.ui.view.screens.video_manage.VideoManageState
import kotlinx.coroutines.launch

class VideoRecorderViewModel(private val vidDb: PrivateVideoDao) : ViewModel() {

    val mState: MutableState<VideoManageState> = mutableStateOf(VideoManageState())

    fun saveVideoItem(pvtVid: Uri) {
        viewModelScope.launch {
            println("saveDB ${pvtVid.path}")
        }
    }
}