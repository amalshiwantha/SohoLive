package com.soho.sohoapp.live.ui.view.activity.main

import android.content.Intent
import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mux.video.upload.api.MuxUpload
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.db.PrivateVideoDao
import com.soho.sohoapp.live.enums.CastEnd
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.ConnectedSocialProfile
import com.soho.sohoapp.live.model.MainStateHolder
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.model.UploadData
import com.soho.sohoapp.live.model.User
import com.soho.sohoapp.live.ui.view.screens.golive.doLogout
import com.soho.sohoapp.live.ui.view.screens.player.deleteFileFromUri
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import com.soho.sohoapp.live.utility.NotificationHelper
import com.soho.sohoapp.live.utility.UploadService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    private val _stateOpenSupport = MutableStateFlow(false)
    val stateOpenSupport: StateFlow<Boolean> = _stateOpenSupport.asStateFlow()

    private val _stateAskSupport = MutableStateFlow(false)
    val stateAskSupport: StateFlow<Boolean> = _stateAskSupport.asStateFlow()

    private val _uploadProgress = MutableStateFlow(0)
    val uploadProgress: StateFlow<Int> = _uploadProgress.asStateFlow()

    private val _stateUploadLevel = MutableStateFlow("hide")
    val stateUploadLevel: StateFlow<String> = _stateUploadLevel.asStateFlow()

    val _isOpenResetPw = MutableStateFlow(false)
    val isOpenResetPw: StateFlow<Boolean> = _isOpenResetPw.asStateFlow()

    var deepLinkToken: MutableState<String?> = mutableStateOf(null)

    val msUser: MutableState<User> = mutableStateOf(User())

    private val uploadNotification = NotificationHelper()

    fun uploadNow(data: UploadData) {
        val filePath = data.path ?: return
        val uploadUrl = data.url ?: return
        uploadVideo(File(filePath), uploadUrl)

        //startUploadService(filePath, uploadUrl)
    }

    fun dataStore() : AppDataStoreManager {
        return dataStore
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

                viewModelScope.launch {
                    uploadNotification.showNotification(
                        context,
                        percentage.toInt()
                    )
                }
            }

            muxUpload.setResultListener { result ->
                if (result.isSuccess) {

                    viewModelScope.launch {
                        //this delay called to until get vidListApi retrun pre_process
                        _stateUploadLevel.value = "completed" //display done message
                        delay(5000)

                        deleteFileAndRecord(recFile)
                        _stateUploadLevel.value = "done" // reload call vidList api

                        delay(500)
                        _stateUploadLevel.value = "hide" //hide upload progress
                    }

                } else {
                    _stateUploadLevel.value = "failed" //display error message
                }
            }

            muxUpload.start()
        }
    }

    //delete file after upload
    private fun deleteFileAndRecord(recFile: File) {
        //remove from storage
        deleteFileFromUri(vidFile = recFile)

        //remove form db
        viewModelScope.launch {
            vidDb.deleteVideoByPath(recFile.path)
        }
    }

    //delete all files
    fun deleteOldFiles() {
        viewModelScope.launch {
            val videos = vidDb.getAllVideos()
            val thresholdDate = LocalDate.now().minusDays(30)

            videos.forEach { video ->
                val videoDate =
                    LocalDate.parse(
                        video.createdDate,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    )
                if (videoDate.isBefore(thresholdDate)) {
                    vidDb.deleteVideoByPath(video.filePath)
                    deleteFileFromUri(vidFile = File(video.filePath))
                }
            }
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

    fun clearLogout() {
        viewModelScope.launch {
            val smFb = SocialMediaProfile().apply {
                smInfo = SocialMediaInfo.FACEBOOK
                profile.isConnected = false
                smInfo.isConnect = false
                smInfo.isItemChecked = false
            }

            val smYT = SocialMediaProfile().apply {
                smInfo = SocialMediaInfo.YOUTUBE
                profile.isConnected = false
                smInfo.isConnect = false
                smInfo.isItemChecked = false
            }
            doLogout(smFb)
            doLogout(smYT)
            MainStateHolder.mState.reset()
            dataStore.clearAllData()
        }
    }

    fun openSupport() {
        _stateAskSupport.value = true
        loadProfileData()
    }

    fun closeSupport() {
        _stateOpenSupport.value = false
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {
                    msUser.value = msUser.value.copy(
                        name = it.name,
                        email = it.email
                    )

                    if (stateAskSupport.value) {
                        _stateOpenSupport.value = true
                        _stateAskSupport.value = false
                    }
                }
            }
        }
    }
}