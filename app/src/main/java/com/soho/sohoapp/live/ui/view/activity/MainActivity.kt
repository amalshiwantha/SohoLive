package com.soho.sohoapp.live.ui.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.ui.components.ButtonColoredIcon
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhite
import com.soho.sohoapp.live.ui.components.SpacerHorizontal
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_16sp
import com.soho.sohoapp.live.ui.components.Text800_20sp
import com.soho.sohoapp.live.ui.navigation.AppNavHost
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.BottomBarBg
import com.soho.sohoapp.live.ui.theme.BottomSheetDrag
import com.soho.sohoapp.live.ui.theme.ItemCardBg
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
                    val isConnectedSM by viewMMain.isSMConnected.collectAsState()

                    ChangeSystemTrayColor()
                    AppNavHost(viewMMain)

                    //PreSetup for Social Media
                    setupFBLogin()
                    OpenSMConnectModel(viewMMain, smInfoConnect)
                    SocialMediaProfileBottomSheet(isConnectedSM)
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
                        //facebookLogin()

                        val profile = SocialMediaProfile(
                            SocialMediaInfo.FACEBOOK,
                            "Face Borker",
                            "https://design-assets.adobeprojectm.com/content/download/express/public/urn:aaid:sc:VA6C2:e88fad34-6940-5552-91ac-b45c41168d43/component?assetType=TEMPLATE&etag=f9b270fe49cf46a2b3686e8df866ee76&revision=613eebe7-dc06-4992-86c8-f031e98d09ea&component_id=44c6e3bc-f057-427c-a844-580c12fda50e",
                            "amalskr@facebook.com",
                            "fbask123"
                        )
                        viewMMain.saveSocialMediaProfile(profile)
                        //viewMMain.saveSocialMediaProfile(getProfileNone())
                    }

                    SocialMediaInfo.YOUTUBE -> {
                        val profile = SocialMediaProfile(
                            SocialMediaInfo.YOUTUBE,
                            "Yo Tube",
                            "https://png.pngtree.com/thumb_back/fh260/background/20230527/pngtree-in-the-style-of-bold-character-designs-image_2697064.jpg",
                            "amalskr@youtube.com",
                            "youask123"
                        )
                        viewMMain.saveSocialMediaProfile(profile)
                        //viewMMain.saveSocialMediaProfile(getProfileNone())
                    }

                    SocialMediaInfo.LINKEDIN -> {
                        //linkedInLogin()
                        val profile = SocialMediaProfile(
                            SocialMediaInfo.LINKEDIN,
                            "Jhon Smith",
                            "https://media.licdn.com/dms/image/D4D12AQGsWiQQo-hEew/article-cover_image-shrink_720_1280/0/1705940048112?e=2147483647&v=beta&t=Dm3TYa8aaImrrYHEksUYyCuPe0mRjKNlrKcNMnKjlXc",
                            "amalskr@live.com",
                            "liveask123"
                        )
                        viewMMain.saveSocialMediaProfile(profile)
                        //viewMMain.saveSocialMediaProfile(getProfileNone())
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
                containerColor = BottomBarBg,
                dragHandle = { DragHandle(color = BottomSheetDrag) },
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SocialMediaProfileBottomSheet(smProfile: SocialMediaProfile) {
        if (smProfile.smInfo.name != SocialMediaInfo.NONE.name) {
            val bottomSheetState = rememberModalBottomSheetState()
            var showBottomSheet by remember { mutableStateOf(true) }

            if (showBottomSheet) {
                ModalBottomSheet(
                    containerColor = BottomBarBg,
                    dragHandle = { DragHandle(color = BottomSheetDrag) },
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = bottomSheetState
                ) {
                    ProfileContentBottomSheet(smProfile, onDone = {
                        showBottomSheet = false
                    }, onDisconnect = {
                        showBottomSheet = false
                    })
                }
            }
        }
    }

    @Composable
    private fun ProfileContentBottomSheet(
        smProfile: SocialMediaProfile,
        onDisconnect: () -> Unit,
        onDone: () -> Unit
    ) {
        val smInfo = smProfile.smInfo

        Column(
            modifier = Modifier
                .background(BottomBarBg)
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {

            Text800_20sp(label = smInfo.title)
            SpacerVertical(size = 8.dp)

            //Connect Button
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text400_14sp(info = smInfo.info)
                SpacerVertical(size = 16.dp)

                //profile card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = ItemCardBg)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //image
                        val urlPainter = rememberAsyncImagePainter(
                            model = smProfile.imageUrl,
                            placeholder = painterResource(id = R.drawable.profile_placeholder),
                            error = painterResource(id = R.drawable.profile_placeholder)
                        )

                        Image(
                            painter = urlPainter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 70.dp, height = 68.dp)
                                .clip(CircleShape)
                        )
                        //info
                        Column(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Text400_14sp(info = "Connected to ${smInfo.title.lowercase()} as")
                            SpacerVertical(size = 8.dp)
                            Text700_16sp(title = smProfile.fullName)
                        }
                    }
                }

                //Button buttons
                SpacerVertical(size = 40.dp)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ButtonOutlineWhite(text = "Disconnect",
                        modifier = Modifier.weight(1f), onBtnClick = { onDisconnect() })
                    SpacerHorizontal(size = 8.dp)
                    ButtonColoured(
                        text = "Done",
                        color = AppGreen,
                        modifier = Modifier.weight(1f),
                        onBtnClick = { onDone() })
                }
            }
        }
    }

    @Composable
    private fun ContentBottomSheet(
        smInfoConnect: SocialMediaInfo,
        onConnect: (SocialMediaInfo) -> Unit
    ) {
        Column(
            modifier = Modifier
                .background(BottomBarBg)
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {

            Text800_20sp(label = smInfoConnect.title)
            SpacerVertical(size = 8.dp)

            //Connect Button
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text400_14sp(info = smInfoConnect.info)
                SpacerVertical(size = 40.dp)
                ButtonColoredIcon(title = smInfoConnect.btnTitle,
                    icon = smInfoConnect.btnIcon,
                    btnColor = smInfoConnect.btnColor,
                    onBtnClick = { onConnect(smInfoConnect) })
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

    private fun getProfileNone(): SocialMediaProfile {
        return SocialMediaProfile(
            SocialMediaInfo.NONE,
            "Jhone",
            "https",
            "gmail.com",
            "123"
        )
    }

    @Preview
    @Composable
    private fun PreviewBottomSheetSMConnect() {
        ContentBottomSheet(
            smInfoConnect = SocialMediaInfo.FACEBOOK,
            onConnect = {})
    }

    @Preview
    @Composable
    private fun PreviewBottomSheetSMProfile() {
        ProfileContentBottomSheet(
            smProfile = SocialMediaProfile(
                SocialMediaInfo.FACEBOOK,
                "Jhone Smith",
                "https://t4.ftcdn.net/jpg/06/08/55/73/360_F_608557356_ELcD2pwQO9pduTRL30umabzgJoQn5fnd.jpg",
                "amalskr@gmail.com",
                "ask123"
            ),
            onDisconnect = {}, onDone = {})
    }
}