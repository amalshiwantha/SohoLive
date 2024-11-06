package com.soho.sohoapp.live.ui.view.activity.main

import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mux.video.upload.api.MuxUpload
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.db.PrivateVideoDao
import com.soho.sohoapp.live.enums.CastEnd
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.ConnectedSocialProfile
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.model.UploadData
import com.soho.sohoapp.live.ui.view.screens.player.deleteFileFromUri
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import com.soho.sohoapp.live.utility.NotificationHelper
import com.soho.sohoapp.live.utility.UploadService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(
    private val dataStore: AppDataStoreManager,
    private val vidDb: PrivateVideoDao
) : ViewModel() {
    private val _isCallSMConnect = MutableStateFlow(SocialMediaInfo.NONE)
    val isCallSMConnect: StateFlow<SocialMediaInfo> = _isCallSMConnect.asStateFlow()

    private val _stateIsSMConnected = MutableStateFlow(SocialMediaProfile())
    val stateIsSMConnected = _stateIsSMConnected.asStateFlow()

    private val _stateRecentLoggedSM = MutableStateFlow(mutableListOf<String>())
    val stateRecentLoggedSM = _stateRecentLoggedSM.asStateFlow()

    private val _stateOpenLiveCast = MutableStateFlow("")
    val stateOpenLiveCast: StateFlow<String> = _stateOpenLiveCast.asStateFlow()

    private val _uploadProgress = MutableStateFlow(0)
    val uploadProgress: StateFlow<Int> = _uploadProgress.asStateFlow()

    private val _stateUploadLevel = MutableStateFlow("")
    val stateUploadLevel: StateFlow<String> = _stateUploadLevel.asStateFlow()

    val uploadNotification = NotificationHelper()

    fun uploadNow(data: UploadData) {
        println("myUpload uploadData $data")

        val filePath = data.path ?: return
        val uploadUrl = data.url ?: return
        uploadVideo(File(filePath), uploadUrl)

        //startUploadService(filePath, uploadUrl)
    }

    private fun startUploadService(filePath: String, uploadUrl: String) {
        val intent = Intent(context, UploadService::class.java).apply {
            putExtra(UploadService.EXTRA_FILE_PATH, filePath)
            putExtra(UploadService.EXTRA_UPLOAD_URL, uploadUrl)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    //Upload Video
    private fun uploadVideo(recFile: File, uploadUrl: String) {
        viewModelScope.launch {
            _stateUploadLevel.value = "uploading"
            val muxUpload = MuxUpload.Builder(uploadUrl, recFile).build()

            muxUpload.setProgressListener { progress ->
                val percentage = if (progress.totalBytes > 0) {
                    (progress.bytesUploaded.toFloat() / progress.totalBytes.toFloat()) * 100
                } else {
                    0f
                }

                _uploadProgress.value = percentage.toInt()
                println("myUpload Progress Now: $percentage%")

                viewModelScope.launch {
                    uploadNotification.showNotification(
                        context,
                        percentage.toInt()
                    )
                }
            }

            muxUpload.setResultListener { result ->
                if (result.isSuccess) {
                    println("myUpload Done")
                    deleteFileAndRecord(recFile)
                    _stateUploadLevel.value = "done"
                } else {
                    _stateUploadLevel.value = "failed"
                    println("myUpload Failed")
                }
            }

            muxUpload.start()
        }
    }

    private fun deleteFileAndRecord(recFile: File) {
        //remove from storage
        deleteFileFromUri(vidFile = recFile)

        //remove form db
        viewModelScope.launch {
            vidDb.deleteVideoByPath(recFile.path)
        }
    }

    //update LiveData
    fun updateSocialMediaState(smInfo: SocialMediaInfo) {
        _isCallSMConnect.value = smInfo
    }

    fun updateSMConnectedState(smInfo: SocialMediaInfo) {
        getConnectedSMProfile(smInfo.name)
    }

    //save logged SM profile
    fun saveSMProfile(smProfile: SocialMediaProfile) {
        viewModelScope.launch {
            saveConnectedSMProfileList(smProfile)

            val newSM = smProfile.smInfo.name
            _stateRecentLoggedSM.value =
                _stateRecentLoggedSM.value.toMutableList().apply { add(newSM) }
        }
    }

    //remove logout SM profile
    fun removeSMProfile(smProfile: SocialMediaProfile) {
        viewModelScope.launch {
            val currentList =
                dataStore.getSMProfileList() ?: ConnectedSocialProfile(mutableListOf())

            currentList.smProfileList.removeIf { it.smInfo.name == smProfile.smInfo.name }

            dataStore.saveSMProfileList(currentList)
            removeRecentSMConnectState(smProfile.smInfo.name)
        }
    }

    fun resetSMConnectState() {
        _stateIsSMConnected.update { SocialMediaProfile() }
    }

    fun resetRecentSMConnectState() {
        _stateRecentLoggedSM.update { mutableListOf() }
    }

    fun removeRecentSMConnectState(name: String) {
        _stateRecentLoggedSM.value =
            _stateRecentLoggedSM.value.toMutableList().apply { remove(name) }
    }

    //add or replace a connected social media profile
    private fun saveConnectedSMProfileList(newProfile: SocialMediaProfile) {
        viewModelScope.launch {
            val currentList =
                dataStore.getSMProfileList() ?: ConnectedSocialProfile(mutableListOf())

            currentList.smProfileList.removeAll { it.smInfo == newProfile.smInfo }
            currentList.smProfileList.add(newProfile.apply {
                smInfo.isItemChecked = true
                smInfo.isConnect = true
            })

            // Save the updated profile list
            dataStore.saveSMProfileList(currentList)
            getSavedSMProfile(newProfile, currentList)
        }
    }

    private fun getSavedSMProfile(smProfile: SocialMediaProfile, profList: ConnectedSocialProfile) {
        viewModelScope.launch {
            val list = profList.smProfileList
            val foundProfile = list.find { it.smInfo == smProfile.smInfo }
            if (foundProfile != null) {
                _stateIsSMConnected.update { foundProfile }
            }
        }
    }

    private fun getConnectedSMProfile(name: String) {
        viewModelScope.launch {
            val currentList =
                dataStore.getSMProfileList() ?: ConnectedSocialProfile(mutableListOf())
            val foundSMprofile = currentList.smProfileList.find { it.smInfo.name == name }

            if (foundSMprofile != null) {
                _stateIsSMConnected.update { foundSMprofile }
            }
        }
    }

    fun resetSendEvent() {
        viewModelScope.launch {
            AppEventBus.sendEvent(AppEvent.SMProfile(SocialMediaProfile()))
        }
    }

    fun openLiveCastScreen(jsonStr: String) {
        _stateOpenLiveCast.value = jsonStr
    }

    fun updateLiveCastState(castEnd: CastEnd) {
        viewModelScope.launch {
            AppEventBus.sendEvent(AppEvent.LiveEndStatus(castEnd))
        }
    }
}