package com.soho.sohoapp.live.ui.view.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.soho.sohoapp.live.SohoLiveApp
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.ui.view.screens.signin.gauth.GoogleUserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _isCallSMConnect = MutableStateFlow(SocialMediaInfo.NONE)
    val isCallSMConnect: StateFlow<SocialMediaInfo> = _isCallSMConnect.asStateFlow()

    private val _isSMConnected =
        MutableStateFlow(SocialMediaProfile(SocialMediaInfo.NONE, mutableListOf()))
    val isSMConnected: StateFlow<SocialMediaProfile> = _isSMConnected.asStateFlow()

    private var _userStateGoogle = MutableLiveData<GoogleUserModel>()
    val googleUser: LiveData<GoogleUserModel> = _userStateGoogle


    //update LiveData
    fun updateSocialMediaState(smInfo: SocialMediaInfo) {
        _isCallSMConnect.value = smInfo
    }

    fun saveSocialMediaProfile(smProfile: SocialMediaProfile) {
        _isSMConnected.value = smProfile
    }

    //Google
    fun fetchGoogleUser(email: String?, name: String?) {
        viewModelScope.launch {
            _userStateGoogle.value = GoogleUserModel(
                email = email,
                name = name,
            )
        }
    }

    private fun checkGoogleSigned() {
        val gsa = GoogleSignIn.getLastSignedInAccount(SohoLiveApp.context)
        if (gsa != null) {
            _userStateGoogle.value = GoogleUserModel(
                email = gsa.email,
                name = gsa.displayName,
            )
        }
    }

    fun googleSignOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("myGuser signed out successfully.")
            } else {
                println("myGuser Sign out failed.")
            }
        }
    }
    //Google End

}