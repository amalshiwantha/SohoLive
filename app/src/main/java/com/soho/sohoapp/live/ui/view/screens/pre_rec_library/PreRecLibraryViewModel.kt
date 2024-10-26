package com.soho.sohoapp.live.ui.view.screens.pre_rec_library

import android.os.Environment
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.ui.view.screens.video_recorder.PvtRecFolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class PreRecLibraryViewModel() : ViewModel() {
    val mState: MutableState<PreRecLibState> = mutableStateOf(PreRecLibState())

    fun loadPvtVideo() {
        viewModelScope.launch {
            mState.value = mState.value.copy(isLoading = mutableStateOf(true))
            delay(1000)
            mState.value = mState.value.copy(videoList = mutableStateOf(getAllRecordedVideos()))
            mState.value = mState.value.copy(isLoading = mutableStateOf(false))
        }
    }

    private fun getAllRecordedVideos(): List<File> {
        val movieDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        val customDir = File(movieDir, PvtRecFolder)
        return if (customDir.exists()) {
            customDir.listFiles()?.filter { it.extension == "mp4" } ?: emptyList()
        } else {
            emptyList()
        }
    }
}