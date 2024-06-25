package com.soho.sohoapp.live.ui.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()
                ) {

                    val smInfoConnect by viewMMain.isCallSMConnect.collectAsState()

                    ChangeSystemTrayColor()
                    AppNavHost(viewMMain)

                    //PreSetup for Social Media
                    setupFBLogin()
                    OpenSMConnectModel(viewMMain, smInfoConnect)
                }
            }
        }
    }

    @Composable
    private fun OpenSMConnectModel(viewMMain: MainViewModel, smInfoConnect: SocialMediaInfo) {
        if (smInfoConnect.name != SocialMediaInfo.NONE.name) {
            SocialMediaConnectBottomSheet(smInfoConnect, onConnect = { askConnectInfo ->
                when (askConnectInfo) {
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
            }, onReset = {
                viewMMain.updateSocialMediaState(SocialMediaInfo.NONE)
            })
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SocialMediaConnectBottomSheet(
        smInfoConnect: SocialMediaInfo,
        onConnect: (SocialMediaInfo) -> Unit,
        onReset: () -> Unit
    ) {
        val bottomSheetState = rememberModalBottomSheetState()
        var showBottomSheet by remember { mutableStateOf(true) }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState
            ) {
                ContentBottomSheet(smInfoConnect, onConnect = {
                    onConnect.invoke(smInfoConnect)
                    showBottomSheet = false
                })
            }
        } else {
            onReset()
        }
    }

    @Composable
    private fun ContentBottomSheet(
        smInfoConnect: SocialMediaInfo,
        onConnect: (SocialMediaInfo) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "This is some text inside the bottom sheet.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(onClick = {
                onConnect(smInfoConnect)
            }) {
                Text("Connect")
            }
        }
    }

    @Composable
    private fun ChangeSystemTrayColor() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = false

        systemUiController.setSystemBarsColor(
            color = Color.Transparent, darkIcons = useDarkIcons
        )
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
                    Log.d("myFBData", "facebook login cancelled")/*setResult(RESULT_CANCELED)
                    finish()*/
                }

                override fun onError(error: FacebookException) {
                    Log.d("myFBData", "facebook login error")/*setResult(RESULT_ERROR)
                    finish()*/
                }

                override fun onSuccess(result: LoginResult) {
                    Log.d("myFBData", "facebook login success")/*val intent = Intent().apply {
                        putExtra(EXTRA_DATA_FB, result.accessToken)
                    }
                    setResult(RESULT_OK, intent)
                    finish()*/
                }
            })
    }

    private fun facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(
            this, listOf("email", "public_profile")
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

    @Preview
    @Composable
    private fun PreviewBottomSheetSMConnect() {
        ContentBottomSheet(
            smInfoConnect = SocialMediaInfo.FACEBOOK,
            onConnect = {})
    }
}