package com.soho.sohoapp.live.ui.view.screens.pre_rec_library

import android.os.Environment
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.db.PrivateVideo
import com.soho.sohoapp.live.db.PrivateVideoDao
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.ui.view.screens.video_recorder.PvtRecFolder
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class PreRecLibraryViewModel(
    private val vidDb: PrivateVideoDao
) : ViewModel() {

    val mState: MutableState<PreRecLibState> = mutableStateOf(PreRecLibState())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun loadPvtVideo() {
        mState.value = mState.value.copy(isLoading = mutableStateOf(true))
        sortedPvtVidList()
    }

    private fun sortedPvtVidList() {
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
            rawFiles.forEach { it ->
                val rawFileName = it.name
                val savedFile = dbSaveData.find { savedData ->
                    savedData.filePath.endsWith(rawFileName)
                }

                savedFile?.let { avaliableFile ->
                    displayList.add(avaliableFile)
                }
            }

            //sort last rec first
            sortVideosByDate(displayList)

            mState.value = mState.value.copy(videoList = mutableStateOf(displayList))
            mState.value = mState.value.copy(isLoading = mutableStateOf(false))
        }
    }

    private fun deleteOldRecordedVideos() {
        val movieDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        val customDir = File(movieDir, PvtRecFolder)

        if (customDir.exists()) {
            val thirtyDaysInMillis = TimeUnit.DAYS.toMillis(30)
            val currentTime = System.currentTimeMillis()

            customDir.listFiles()?.filter {
                it.extension == "mp4" && (currentTime - it.lastModified()) > thirtyDaysInMillis
            }?.forEach { oldFile ->
                if (oldFile.delete()) {
                    println("Deleted old video: ${oldFile.name}")
                } else {
                    println("Failed to delete: ${oldFile.name}")
                }
            }
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

    // Sort function
    private fun sortVideosByDate(videos: MutableList<PrivateVideo>) {
        videos.sortByDescending { video ->
            dateFormat.parse(video.createdDate)
        }
    }
}