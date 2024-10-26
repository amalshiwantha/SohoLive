package com.soho.sohoapp.live.ui.view.screens.pre_rec_library

import android.os.Environment
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.model.PrivateVideo
import com.soho.sohoapp.live.ui.view.screens.video_recorder.PvtRecFolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class PreRecLibraryViewModel() : ViewModel() {
    val mState: MutableState<PreRecLibState> = mutableStateOf(PreRecLibState())
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun loadPvtVideo() {
        viewModelScope.launch {
            mState.value = mState.value.copy(isLoading = mutableStateOf(true))
            mState.value = mState.value.copy(videoList = mutableStateOf(sortedPvtVidList()))
            mState.value = mState.value.copy(isLoading = mutableStateOf(false))
        }
    }

    private fun sortedPvtVidList(): MutableList<PrivateVideo> {
        val displayList: MutableList<PrivateVideo> = mutableListOf()
        //get all db saved data
        val dbSaveData = getAllStoreData()

        //get all raw video files
        val rawFiles = getAllRecordedVideos()

        /*
        * find dbSaved file locally avaliable or not.
        * if have then add to the displayList
        * */
        rawFiles.forEach { it ->
            val rawFileName = it.name
            val savedFile = dbSaveData.find { savedData ->
                savedData.filePath.endsWith(rawFileName)
            }

            println("fileName ${savedFile?.filePath}")

            savedFile?.let { avaliableFile ->
                displayList.add(avaliableFile)
            }
        }

        //sort last rec first
        sortVideosByDate(displayList)

        return displayList
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

    private fun getAllStoreData(): MutableList<PrivateVideo> {
        val tmpFile1 =
            "file:///storage/emulated/0/Movies/SohoPreRecord/SohoLive_20241026_004116.mp4"
        val tmpFile2 =
            "file:///storage/emulated/0/Movies/SohoPreRecord/SohoLive_20241022_085135.mp4"
        val pv1 = PrivateVideo(
            filePath = tmpFile1,
            createdDate = "2024-10-26 12:23:56",
            castFor = "Inspection",
            privacy = VideoPrivacy.PUBLIC,
            title = "Just Title"
        )

        val pv2 = PrivateVideo(
            filePath = tmpFile2,
            createdDate = "2024-10-22 10:13:56",
            castFor = "Auction",
            privacy = VideoPrivacy.UNLISTED,
            title = "Just Title Second",
            description = "Just more description to display"
        )

        return mutableListOf(pv1, pv2)
    }

    // Sort function
    private fun sortVideosByDate(videos: MutableList<PrivateVideo>) {
        videos.sortByDescending { video ->
            dateFormat.parse(video.createdDate)
        }
    }
}