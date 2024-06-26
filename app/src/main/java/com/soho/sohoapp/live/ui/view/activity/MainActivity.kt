package com.soho.sohoapp.live.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.FBListType
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.FbTypeView
import com.soho.sohoapp.live.model.SMProfile
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.ui.components.ButtonColoredIcon
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhite
import com.soho.sohoapp.live.ui.components.SpacerHorizontal
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_16sp
import com.soho.sohoapp.live.ui.components.Text800_20sp
import com.soho.sohoapp.live.ui.components.TextSwipeSelection
import com.soho.sohoapp.live.ui.navigation.AppNavHost
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.AppWhiteGray
import com.soho.sohoapp.live.ui.theme.BorderGray
import com.soho.sohoapp.live.ui.theme.BottomBarBg
import com.soho.sohoapp.live.ui.theme.BottomSheetDrag
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import com.soho.sohoapp.live.ui.theme.SohoLiveTheme
import com.soho.sohoapp.live.ui.theme.TextDark
import com.soho.sohoapp.live.utility.printHashKey
import com.ssw.linkedinmanager.dto.LinkedInAccessToken
import com.ssw.linkedinmanager.dto.LinkedInEmailAddress
import com.ssw.linkedinmanager.dto.LinkedInUserProfile
import com.ssw.linkedinmanager.events.LinkedInManagerResponse
import com.ssw.linkedinmanager.events.LinkedInUserLoginDetailsResponse
import com.ssw.linkedinmanager.events.LinkedInUserLoginValidationResponse
import com.ssw.linkedinmanager.ui.LinkedInRequestManager
import org.json.JSONException
import java.net.MalformedURLException

class MainActivity : ComponentActivity(), LinkedInManagerResponse {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginInstant: LoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewMMain: MainViewModel by viewModels()
        enableEdgeToEdge()

        printHashKey()

        setupFirebaseFB()

        setContent {
            SohoLiveTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()
                ) {

                    var showProfileBottomSheet by remember { mutableStateOf(true) }
                    val smInfoConnect by viewMMain.isCallSMConnect.collectAsState()
                    val isConnectedSM by viewMMain.isSMConnected.collectAsState()

                    ChangeSystemTrayColor()
                    AppNavHost(viewMMain)

                    //PreSetup for Social Media
                    OpenSMConnectModel(viewMMain, smInfoConnect, onShowProfile = {
                        showProfileBottomSheet = true
                        openPro()
                    })

                    if (showProfileBottomSheet) {
                        SocialMediaProfileBottomSheet(isConnectedSM)
                    }
                }
            }
        }
    }

    private fun openPro() {

    }

    private fun setupFirebaseFB() {
        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()
        loginInstant = LoginManager.getInstance()

        loginInstant
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("myFb", "facebook:onSuccess:${loginResult}")
                    Log.v("myFb", "Token::" + loginResult.accessToken.token)
                    getFBProfileData(loginResult)
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d("myFb", "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("myFb", "facebook:onError", error)
                }
            })

        facebookLogin()
    }

    private fun getFBProfileData(loginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(loginResult.accessToken) { info, response ->
            Log.e("myFb info", info.toString())

            try {
                info?.let {
                    val userId = it.getString("id")
                    val profilePicture =
                        "https://graph.facebook.com/$userId/picture?width=500&height=500"
                    val firstName = if (it.has("first_name")) it.getString("first_name") else null
                    val lastName = if (it.has("last_name")) it.getString("last_name") else null

                    println("myFb bio : " + firstName)
                    println("myFb bio : " + lastName)
                    println("myFb bio : " + profilePicture)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
        }

// Here we put the requested fields to be returned from the JSONObject
        val parameters = Bundle().apply {
            putString("fields", "id, first_name, last_name, email, birthday, gender")
        }
        request.parameters = parameters
        request.executeAsync()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)

        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("myFb", "signInWithCredential:success")
                val user = auth.currentUser
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w("myFb", "signInWithCredential:failure", task.exception)
                updateUI(null)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        Log.d("myFb", "FirebaseUser:$user")
    }

    @Composable
    private fun OpenSMConnectModel(
        viewMMain: MainViewModel,
        smInfoConnect: SocialMediaInfo,
        onShowProfile: () -> Unit
    ) {
        if (smInfoConnect.name != SocialMediaInfo.NONE.name) {
            SocialMediaConnectBottomSheet(smInfoConnect, onConnect = { askConnectInfo ->
                when (askConnectInfo) {
                    SocialMediaInfo.SOHO -> {}
                    SocialMediaInfo.FACEBOOK -> {
                        //facebookLogin()
                        val smProfile = getSampleFbProfile()
                        viewMMain.saveSocialMediaProfile(smProfile)
                        onShowProfile()
                    }

                    SocialMediaInfo.YOUTUBE -> {
                        val smProfile = SMProfile(
                            "Yo Tube",
                            "https://png.pngtree.com/thumb_back/fh260/background/20230527/pngtree-in-the-style-of-bold-character-designs-image_2697064.jpg",
                            "amalskr@youtube.com",
                            "youask123"
                        )
                        val profile = SocialMediaProfile(
                            SocialMediaInfo.YOUTUBE,
                            mutableListOf(smProfile)
                        )
                        viewMMain.saveSocialMediaProfile(profile)
                        onShowProfile()
                    }

                    SocialMediaInfo.LINKEDIN -> {
                        //linkedInLogin()
                        val smProfile = SMProfile(
                            "Jhon Smith",
                            "https://media.licdn.com/dms/image/D4D12AQGsWiQQo-hEew/article-cover_image-shrink_720_1280/0/1705940048112?e=2147483647&v=beta&t=Dm3TYa8aaImrrYHEksUYyCuPe0mRjKNlrKcNMnKjlXc",
                            "amalskr@live.com",
                            "liveask123"
                        )
                        val profile = SocialMediaProfile(
                            SocialMediaInfo.LINKEDIN,
                            mutableListOf(smProfile)
                        )
                        viewMMain.saveSocialMediaProfile(profile)
                        onShowProfile()
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
    private fun SingleSelectionList(myList: SnapshotStateList<FbTypeView>) {
        val selectedId = myList.indexOfFirst { it.isSelect }
        var selectedIndex by remember { mutableIntStateOf(selectedId) }

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .selectableGroup()
        ) {
            items(myList) { item ->
                ListItemView(item, selectedIndex) { updatedItem ->
                    selectedIndex = updatedItem.index
                    updateState(myList, updatedItem)
                }
            }
        }
    }

    private fun updateState(
        mListItems: SnapshotStateList<FbTypeView>,
        updatedItem: FbTypeView
    ) {
        // Update all items, isSelect as false
        mListItems.forEachIndexed { index, fbTypeView ->
            mListItems[index] = fbTypeView.copy(isSelect = false)
        }

        //update selected item
        val selectedIndex =
            mListItems.indexOfFirst { it.title == updatedItem.title }
        if (selectedIndex != -1) {
            mListItems[selectedIndex] = updatedItem.copy(isSelect = true)
        }
    }

    @Composable
    fun ListItemView(fbView: FbTypeView, selectedIndex: Int, onItemUpdated: (FbTypeView) -> Unit) {
        val index = fbView.index

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = selectedIndex == index,
                    onClick = {
                        if (!fbView.isSelect) {
                            onItemUpdated(fbView.copy(isSelect = true))
                        }
                    }
                )
                .padding(bottom = 8.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = ItemCardBg)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //image
                val urlPainter = rememberAsyncImagePainter(
                    model = fbView.imageUrl,
                    placeholder = painterResource(id = R.drawable.profile_placeholder),
                    error = painterResource(id = R.drawable.profile_placeholder)
                )

                Image(
                    painter = urlPainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 48.dp, height = 48.dp)
                        .clip(CircleShape)
                )
                //info
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text700_16sp(title = fbView.title, modifier = Modifier.weight(1f))
                    SpacerHorizontal(size = 8.dp)
                    Image(
                        painter = painterResource(id = if (selectedIndex == index) R.drawable.fb_item_active else R.drawable.fb_item_inactive),
                        contentDescription = ""
                    )
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
        var fbViewType by remember { mutableStateOf(FBListType.TIMELINE) }
        var selectedOption by remember { mutableIntStateOf(0) }
        val mListTimelines = remember { smProfile.timelines.toMutableStateList() }
        val mListPages = remember { smProfile.pages.toMutableStateList() }
        val mListGroup = remember { smProfile.groups.toMutableStateList() }

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

                //if fb add custom tab view for pages,groups etc...
                if (smInfo.name == SocialMediaInfo.FACEBOOK.name) {
                    //FB tab view
                    SwipeSwitchFb(selectedOption = selectedOption, onSwipeChange = {
                        selectedOption = it
                        fbViewType = getSelectedTab(selectedOption)
                    })
                    SpacerVertical(size = 16.dp)

                    //Display Content for each tab
                    val fbSubList = when (fbViewType) {
                        FBListType.TIMELINE -> {
                            val str = stringResource(R.string.not_admin)
                            Pair(mListTimelines, str)
                        }

                        FBListType.PAGES -> {
                            val str = stringResource(R.string.not_admin)
                            Pair(mListPages, str)
                        }

                        FBListType.GROUPS -> {
                            val str = stringResource(R.string.not_admin)
                            Pair(mListGroup, str)
                        }
                    }

                    if (fbSubList.first.isNotEmpty()) {
                        SingleSelectionList(fbSubList.first)
                    } else {
                        Text400_14sp(info = fbSubList.second)
                    }

                } else {
                    //profile card
                    smProfile.profiles.forEach { profile ->
                        ProfileCard(profile, smInfo.title.lowercase())
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

    private fun getSelectedTab(selectedOption: Int): FBListType {
        return when (selectedOption) {
            0 -> {
                FBListType.TIMELINE
            }

            1 -> {
                FBListType.PAGES
            }

            2 -> {
                FBListType.GROUPS
            }

            else -> {
                FBListType.TIMELINE
            }
        }
    }

    @Composable
    private fun SwipeSwitchFb(selectedOption: Int, onSwipeChange: (Int) -> Unit) {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val horizontalPadding = 16.dp
        val availableWidth = screenWidth - (horizontalPadding * 2)
        val indicatorWidth = availableWidth / 3
        val indicatorOffset by animateDpAsState(
            targetValue = when (selectedOption) {
                0 -> 0.dp
                1 -> indicatorWidth
                else -> indicatorWidth * 2
            }, label = "animateToMove"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(width = 1.dp, color = BorderGray, shape = RoundedCornerShape(16.dp))
                .background(Color.Transparent)
        ) {
            // Move selection
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(indicatorWidth)
                    .padding(4.dp)
                    .fillMaxHeight()
                    .background(AppWhiteGray, shape = RoundedCornerShape(14.dp))
            )

            // Three options
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextSwipeSelection(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSwipeChange(0) },
                    title = "Timeline",
                    textColor = if (selectedOption == 0) TextDark else AppWhite
                )

                TextSwipeSelection(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSwipeChange(1) },
                    title = "Pages",
                    textColor = if (selectedOption == 1) TextDark else AppWhite
                )

                TextSwipeSelection(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSwipeChange(2) },
                    title = "Group",
                    textColor = if (selectedOption == 2) TextDark else AppWhite
                )
            }
        }
    }

    @Composable
    private fun ProfileCard(smProfile: SMProfile, socialMediaName: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
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
                    Text400_14sp(info = "Connected to $socialMediaName as")
                    SpacerVertical(size = 8.dp)
                    Text700_16sp(title = smProfile.fullName)
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
    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fbCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }*/

    /*
    * All of social media connection action will fire on Here, called from step #3 GoLive Screen to here
    * */

    //FaceBook
    /*private fun setupFBLogin() {
        LoginManager.getInstance()
            .registerCallback(fbCallbackManager, callback = object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d("myFBData", "facebook login cancelled")*//*setResult(RESULT_CANCELED)
                    finish()*//*
                }

                override fun onError(error: FacebookException) {
                    Log.d("myFBData", "facebook login error")*//*setResult(RESULT_ERROR)
                    finish()*//*
                }

                override fun onSuccess(result: LoginResult) {
                    Log.d("myFBData", "facebook login success")*//*val intent = Intent().apply {
                        putExtra(EXTRA_DATA_FB, result.accessToken)
                    }
                    setResult(RESULT_OK, intent)
                    finish()*//*
                }
            })
    }



    private fun facebookLogout() {
        LoginManager.getInstance().logOut()
    }*/

    private fun facebookLogin() {
        loginInstant.logInWithReadPermissions(this, listOf("email", "public_profile"))
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


    private fun getSampleFbProfile(): SocialMediaProfile {
        val profile = SMProfile(
            "Jhone Smith",
            "https://t4.ftcdn.net/jpg/06/08/55/73/360_F_608557356_ELcD2pwQO9pduTRL30umabzgJoQn5fnd.jpg",
            "amalskr@gmail.com",
            "ask123"
        )

        val timeline1 = FbTypeView(
            0,
            FBListType.TIMELINE,
            "TimeLine 1",
            "http:www.facebook.com",
            "https://t4.ftcdn.net/jpg/06/08/55/73/360_F_608557356_ELcD2pwQO9pduTRL30umabzgJoQn5fnd.jpg"
        )
        val timeline2 = FbTypeView(
            1,
            FBListType.TIMELINE,
            "TimeLine 2",
            "http:www.facebook.com",
            "https://t4.ftcdn.net/jpg/06/08/55/73/360_F_608557356_ELcD2pwQO9pduTRL30umabzgJoQn5fnd.jpg"
        )

        val page1 = FbTypeView(
            0,
            FBListType.PAGES,
            "MyPage",
            "http:www.facebook.com",
            "https://t4.ftcdn.net/jpg/06/08/55/73/360_F_608557356_ELcD2pwQO9pduTRL30umabzgJoQn5fnd.jpg"
        )

        val group1 = FbTypeView(
            0,
            FBListType.GROUPS,
            "My Group",
            "http:www.facebook.com",
            "https://t4.ftcdn.net/jpg/06/08/55/73/360_F_608557356_ELcD2pwQO9pduTRL30umabzgJoQn5fnd.jpg"
        )

        return SocialMediaProfile(
            smInfo = SocialMediaInfo.FACEBOOK,
            profiles = mutableListOf(profile),
            timelines = mutableListOf(timeline1, timeline2),
            pages = mutableListOf(),
            groups = mutableListOf(group1)
        )
    }


    @Preview
    @Composable
    private fun PreviewBottomSheetSMProfile() {
        ProfileContentBottomSheet(
            smProfile = getSampleFbProfile(),
            onDisconnect = {}, onDone = {})
    }
}