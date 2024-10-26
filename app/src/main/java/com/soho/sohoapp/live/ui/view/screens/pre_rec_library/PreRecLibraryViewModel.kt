package com.soho.sohoapp.live.ui.view.screens.pre_rec_library

import android.os.Environment
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.soho.sohoapp.live.network.common.ProgressBarState
import java.io.File

class PreRecLibraryViewModel() : ViewModel() {
    val mState: MutableState<PreRecLibState> = mutableStateOf(PreRecLibState())

    fun loadPvtVideo() {
        mState.value = mState.value.copy(loadingState = ProgressBarState.Loading)
        getAllRecordedVideos()
    }

    fun getAllRecordedVideos(): List<File> {
        println("myVidList getAllRecordedVideos")
        val movieDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        val customDir = File(movieDir, "SohoPreRecord")
        return if (customDir.exists()) {
            customDir.listFiles()?.filter { it.extension == "mp4" } ?: emptyList()
        } else {
            emptyList()
        }
    }
}