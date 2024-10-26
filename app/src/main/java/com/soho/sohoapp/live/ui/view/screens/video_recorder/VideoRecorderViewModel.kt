package com.soho.sohoapp.live.ui.view.screens.video_recorder

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.PrimaryKey
import com.soho.sohoapp.live.db.PrivateVideo
import com.soho.sohoapp.live.db.PrivateVideoDao
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.ui.view.screens.video_manage.VideoManageState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class VideoRecorderViewModel(private val vidDb: PrivateVideoDao) : ViewModel() {

    val mState: MutableState<VideoManageState> = mutableStateOf(VideoManageState())

    private val createdAt: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return sdf.format(Date())
        }

    fun saveVideoItem(goLiveData: GoLiveSubmit, file: Uri) {
        viewModelScope.launch {
            val pvtVid = PrivateVideo(
                filePath = file.path.orEmpty(),
                createdDate = createdAt,
                castFor = goLiveData.purpose.orEmpty(),
                title = goLiveData.title.orEmpty(),
                description = goLiveData.description.orEmpty(),
                propertyId = goLiveData.propertyId
            )
            vidDb.insertVideo(pvtVid)
            val getDa = vidDb.getVideoByPath(file.path.orEmpty())
            println("myVidRec saveDB $getDa")
            mState.value = mState.value.copy(isSuccess = true)
        }
    }

    fun reset() {
        mState.value = mState.value.copy(isSuccess = false)
    }
}