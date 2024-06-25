package com.soho.sohoapp.live.ui.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.ui.navigation.AppNavHost
import com.soho.sohoapp.live.ui.theme.SohoLiveTheme
import com.ssw.linkedinmanager.dto.LinkedInAccessToken
import com.ssw.linkedinmanager.dto.LinkedInEmailAddress
import com.ssw.linkedinmanager.dto.LinkedInUserProfile
import com.ssw.linkedinmanager.events.LinkedInManagerResponse
import com.ssw.linkedinmanager.events.LinkedInUserLoginDetailsResponse
import com.ssw.linkedinmanager.events.LinkedInUserLoginValidationResponse
import com.ssw.linkedinmanager.ui.LinkedInRequestManager


class MainActivity : ComponentActivity(), LinkedInManagerResponse {

    companion object {

        const val RESULT_ERROR = 102
        const val EXTRA_DATA_FB = "extraDataFb"

        fun getInstance(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val fbCallbackManager: CallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewMMain: MainViewModel by viewModels()
        enableEdgeToEdge()

        setContent {
            SohoLiveTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val smInfoConnect by viewMMain.isCallSMConnect.collectAsState()

                    ChangeSystemTrayColor()
                    AppNavHost(viewMMain)

                    //PreSetup for Social Media
                    setupFBLogin()
                    openSmConnection(smInfoConnect, onReset = {
                        viewMMain.updateSocialMediaState(SocialMediaInfo.NONE)
                    })
                }
            }
        }
    }

    @Composable
    private fun ChangeSystemTrayColor() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = false

        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }

    private fun openSmConnection(smInfoConnect: SocialMediaInfo, onReset :()->Unit) {
        onReset()

        when (smInfoConnect) {
            SocialMediaInfo.SOHO -> {}
            SocialMediaInfo.FACEBOOK -> {
                facebookLogin()
            }

            SocialMediaInfo.YOUTUBE -> {}
            SocialMediaInfo.LINKEDIN -> {
                linkedInLogin()
            }

            else -> {}
        }
    }

    //SocialMedia callback actions
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fbCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    /*
    * All of social media connection action will fire on Here, called from step #3 GoLive Screen to here
    * */

    //FaceBook
    private fun setupFBLogin() {
        LoginManager.getInstance()
            .registerCallback(fbCallbackManager, callback = object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d("myFBData", "facebook login cancelled")
                    /*setResult(RESULT_CANCELED)
                    finish()*/
                }

                override fun onError(error: FacebookException) {
                    Log.d("myFBData", "facebook login error")
                    /*setResult(RESULT_ERROR)
                    finish()*/
                }

                override fun onSuccess(result: LoginResult) {
                    Log.d("myFBData", "facebook login success")
                    /*val intent = Intent().apply {
                        putExtra(EXTRA_DATA_FB, result.accessToken)
                    }
                    setResult(RESULT_OK, intent)
                    finish()*/
                }
            })
    }

    private fun facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            listOf("email", "public_profile")
        )
    }

    private fun facebookLogout() {
        LoginManager.getInstance().logOut()
    }

    //LinkedIn
    private fun linkedInLogin() {
        val linkedInRequestManager = LinkedInRequestManager(
            this,
            this,
            "8670uh4gz1gbbp",
            "9xaiOv3ibWwsqCXi",
            "https://lakveggie.ceylonapz.com",
            true
        )

        linkedInRequestManager.isLoggedIn(object : LinkedInUserLoginValidationResponse {
            override fun activeLogin() {
                //Session token is active. can use to get data from linkedin
            }

            override fun tokenExpired() {
                //token has been expired. need to obtain a new code
            }

            override fun notLogged() {
                //user is not logged into the application
            }
        })

        linkedInRequestManager.getLoggedRequestedMode(object : LinkedInUserLoginDetailsResponse {
            override fun loggedMode(mode: Int) {
                //user is already logged in. active token. mode is available
                when (mode) {
                    LinkedInRequestManager.MODE_LITE_PROFILE_ONLY -> {}
                    LinkedInRequestManager.MODE_EMAIL_ADDRESS_ONLY -> {}
                    LinkedInRequestManager.MODE_BOTH_OPTIONS -> {}
                }
            }

            override fun tokenExpired() {
                //token has been expired. need to obtain a new code
            }

            override fun notLogged() {
                //user is not logged into the application
            }
        })

        linkedInRequestManager.showAuthenticateView(LinkedInRequestManager.MODE_BOTH_OPTIONS)
    }

    override fun onGetAccessTokenFailed() {
        TODO("Not yet implemented")
    }

    override fun onGetAccessTokenSuccess(linkedInAccessToken: LinkedInAccessToken?) {
        val linkedinAccessToken = linkedInAccessToken?.access_token
    }

    override fun onGetCodeFailed() {
        TODO("Not yet implemented")
    }

    override fun onGetCodeSuccess(code: String?) {
        TODO("Not yet implemented")
    }

    override fun onGetProfileDataFailed() {
        TODO("Not yet implemented")
    }

    override fun onGetProfileDataSuccess(linkedInUserProfile: LinkedInUserProfile?) {
        TODO("Not yet implemented")
    }

    override fun onGetEmailAddressFailed() {
        TODO("Not yet implemented")
    }

    override fun onGetEmailAddressSuccess(linkedInEmailAddress: LinkedInEmailAddress?) {
        val adde = linkedInEmailAddress?.emailAddress
    }
    //LinkedIn End
}