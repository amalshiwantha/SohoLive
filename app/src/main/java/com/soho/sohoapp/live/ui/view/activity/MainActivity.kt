package com.soho.sohoapp.live.ui.view.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.CategoryType
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.CategoryInfo
import com.soho.sohoapp.live.model.ConnectedSocialProfile
import com.soho.sohoapp.live.model.Profile
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
import com.soho.sohoapp.live.ui.view.screens.golive.DoConnectFacebook
import com.soho.sohoapp.live.ui.view.screens.golive.doConnectGoogle
import com.soho.sohoapp.live.ui.view.screens.golive.doLogout
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import com.ssw.linkedinmanager.dto.LinkedInAccessToken
import com.ssw.linkedinmanager.dto.LinkedInEmailAddress
import com.ssw.linkedinmanager.dto.LinkedInUserProfile
import com.ssw.linkedinmanager.events.LinkedInManagerResponse
import com.ssw.linkedinmanager.events.LinkedInUserLoginDetailsResponse
import com.ssw.linkedinmanager.events.LinkedInUserLoginValidationResponse
import com.ssw.linkedinmanager.ui.LinkedInRequestManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)

    fun saveProfile(key: String, profile: ConnectedSocialProfile) {
        val jsonString = Json.encodeToString(profile)
        val editor = sharedPreferences.edit()
        editor.putString(key, jsonString)
        editor.apply()
    }

    fun getProfile(key: String): ConnectedSocialProfile? {
        val jsonString = sharedPreferences.getString(key, null) ?: return null
        return Json.decodeFromString(jsonString)
    }
}

class MainActivity : ComponentActivity(), LinkedInManagerResponse {

    //private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        //sharedPreferencesHelper = SharedPreferencesHelper(this)


        setContent {
            SohoLiveTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()
                ) {

                    val viewMMain: MainViewModel = koinInject()
                    val smInfoConnect by viewMMain.isCallSMConnect.collectAsState()
                    var openSmConnector by remember { mutableStateOf(SocialMediaInfo.NONE) }
                    val openSmConnectorState by rememberUpdatedState(openSmConnector)
                    //val stateSMProfile by viewMMain.stateConnectedProfile.collectAsStateWithLifecycle()
                    val stateSMConnected by viewMMain.stateIsSMConnected.collectAsStateWithLifecycle()
                    var isShowSMConnectedModel by remember { mutableStateOf(false) }
                    //val saveProf by viewMMain.saveProf.collectAsStateWithLifecycle()

                    ChangeSystemTrayColor()
                    AppNavHost(viewMMain)

                    /*
                    * open SM connect button bottomSheet and
                    * click action -> open connectApi
                    * */
                    OpenSMConnectModel(viewMMain, smInfoConnect, doConnectNow = { goConnect ->
                        when (goConnect) {
                            SocialMediaInfo.FACEBOOK -> {
                                openSmConnector = SocialMediaInfo.FACEBOOK
                            }

                            SocialMediaInfo.YOUTUBE -> {
                                openSmConnector = SocialMediaInfo.YOUTUBE
                            }

                            SocialMediaInfo.LINKEDIN -> {
                                openSmConnector = SocialMediaInfo.LINKEDIN
                            }

                            else -> {}
                        }
                    })

                    //Connect SM api
                    //state change for clickEvents
                    LaunchedEffect(openSmConnectorState) {
                        openSmConnector = SocialMediaInfo.NONE
                    }

                    //Google Sign In
                    val gAuth = doConnectGoogle(viewMMain)

                    /*LaunchedEffect(key1 = stateSMProfile) {
                        if (stateSMProfile.profile.isConnected) {
                            viewMMain.saveSocialMediaProfile(stateSMProfile)
                            viewMMain.resetState()
                        }
                    }*/

                    //Show Profile BottomSheet for SM connected
                    LaunchedEffect(stateSMConnected) {
                        if (stateSMConnected.smInfo.name != SocialMediaInfo.NONE.name) {
                            isShowSMConnectedModel = true
                        }
                    }

                    SocialMediaProfileBottomSheet(
                        stateSMConnected,
                        isShowSMConnectedModel,
                        onDoneClick = {
                            //send updated SMProfile to save in submitData
                            GlobalScope.launch {
                                AppEventBus.sendEvent(AppEvent.SMProfile(stateSMConnected))
                            }

                            viewMMain.resetSMConnectState()
                            isShowSMConnectedModel = false
                        },
                        onDisconnectClick = { smProfile ->
                            doLogout(smProfile)
                            viewMMain.removeSMProfile(smProfile)
                            viewMMain.resetSMConnectState()
                            isShowSMConnectedModel = false
                        })


                    //open social media connector
                    when (openSmConnector) {
                        SocialMediaInfo.SOHO -> {}
                        SocialMediaInfo.FACEBOOK -> {
                            DoConnectFacebook(viewMMain)
                        }

                        SocialMediaInfo.YOUTUBE -> {
                            LaunchedEffect(key1 = gAuth) {
                                gAuth.launch(1)
                            }
                        }

                        SocialMediaInfo.LINKEDIN -> {
                            LaunchedEffect(key1 = "linkedInLogin") {
                                linkedInLogin()
                            }

                            /*val smProfile = SMProfile(
                                "Jhon Smith",
                                "https://media.licdn.com/dms/image/D4D12AQGsWiQQo-hEew/article-cover_image-shrink_720_1280/0/1705940048112?e=2147483647&v=beta&t=Dm3TYa8aaImrrYHEksUYyCuPe0mRjKNlrKcNMnKjlXc",
                                "amalskr@live.com",
                                "liveask123"
                            )
                            val profile = SocialMediaProfile(
                                SocialMediaInfo.LINKEDIN,
                                mutableListOf(smProfile)
                            )
                            viewMMain.saveSocialMediaProfile(profile)*/
                        }

                        SocialMediaInfo.NONE -> {}
                    }
                }
            }
        }
    }

    @Composable
    private fun OpenSMConnectModel(
        viewMMain: MainViewModel,
        smInfoConnect: SocialMediaInfo,
        doConnectNow: (SocialMediaInfo) -> Unit
    ) {
        if (smInfoConnect.name != SocialMediaInfo.NONE.name) {
            SocialMediaConnectBottomSheet(smInfoConnect, onConnect = { askConnectInfo ->
                when (askConnectInfo) {
                    SocialMediaInfo.SOHO -> {}
                    SocialMediaInfo.FACEBOOK -> {
                        doConnectNow(SocialMediaInfo.FACEBOOK)
                    }

                    SocialMediaInfo.YOUTUBE -> {
                        doConnectNow(SocialMediaInfo.YOUTUBE)
                    }

                    SocialMediaInfo.LINKEDIN -> {
                        doConnectNow(SocialMediaInfo.LINKEDIN)
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
    private fun SocialMediaProfileBottomSheet(
        smProfile: SocialMediaProfile,
        isShow: Boolean,
        onDoneClick: () -> Unit,
        onDisconnectClick: (SocialMediaProfile) -> Unit
    ) {
        val bottomSheetState = rememberModalBottomSheetState()

        if (isShow) {
            ModalBottomSheet(
                containerColor = BottomBarBg,
                dragHandle = { DragHandle(color = BottomSheetDrag) },
                onDismissRequest = { onDoneClick() },
                sheetState = bottomSheetState
            ) {
                ProfileContentBottomSheet(smProfile, onDone = {
                    onDoneClick()
                }, onDisconnect = {
                    onDisconnectClick(smProfile)
                })
            }
        }
    }

    @Composable
    private fun SingleSelectionList(
        myList: SnapshotStateList<CategoryInfo>,
        onItemClick: (CategoryInfo) -> Unit
    ) {
        val selVal = myList.find { it.isSelect }
        var selectedIndex = selVal?.index ?: 0

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .selectableGroup()
        ) {
            items(myList) { item ->
                ListItemView(item, selectedIndex) { updatedItem ->
                    selectedIndex = updatedItem.index
                    onItemClick(updatedItem)
                }
            }
        }
    }

    @Composable
    fun ListItemView(
        fbView: CategoryInfo,
        selectedIndex: Int,
        onItemUpdated: (CategoryInfo) -> Unit
    ) {
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
        var fbViewType by remember { mutableStateOf(CategoryType.TIMELINE) }
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
                        CategoryType.TIMELINE -> {
                            val str = stringResource(R.string.not_admin_timeline)
                            Pair(mListTimelines, str)
                        }

                        CategoryType.PAGES -> {
                            val str = stringResource(R.string.not_admin_page)
                            Pair(mListPages, str)
                        }

                        CategoryType.GROUPS -> {
                            val str = stringResource(R.string.not_admin_group)
                            Pair(mListGroup, str)
                        }
                    }

                    if (fbSubList.first.isNotEmpty()) {
                        SingleSelectionList(fbSubList.first, onItemClick = {
                            when (it.type) {
                                CategoryType.TIMELINE -> updateState(mListTimelines, it)
                                CategoryType.PAGES -> updateState(mListPages, it)
                                CategoryType.GROUPS -> updateState(mListGroup, it)
                            }

                            smProfile.smInfo.apply {
                                selectionType = it
                            }
                        })
                    } else {
                        Text400_14sp(info = fbSubList.second)
                    }

                } else {
                    //profile card
                    ProfileCard(smProfile.profile, smInfo.title.lowercase())

                    smProfile.profile.let {
                        smProfile.smInfo.apply {
                            selectionType = CategoryInfo(
                                index = 0,
                                id = "",
                                type = CategoryType.TIMELINE,
                                title = "",
                                url = "",
                                imageUrl = "",
                                accessToken = it.token.orEmpty(),
                                isSelect = true
                            )
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

    private fun updateState(
        mListItems: SnapshotStateList<CategoryInfo>,
        updatedItem: CategoryInfo
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

    private fun getSelectedTab(selectedOption: Int): CategoryType {
        return when (selectedOption) {
            0 -> {
                CategoryType.TIMELINE
            }

            1 -> {
                CategoryType.PAGES
            }

            2 -> {
                CategoryType.GROUPS
            }

            else -> {
                CategoryType.TIMELINE
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
    private fun ProfileCard(profile: Profile, socialMediaName: String) {
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
                    model = profile.imageUrl,
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
                    profile.fullName?.let { Text700_16sp(title = it) }
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

    /*
    * All of social media connection action will fire on Here, called from step #3 GoLive Screen to here
    * */

    //Google

    //LinkedIn
    private fun linkedInLogin() {
        val linkedInRequestManager = LinkedInRequestManager(
            this,
            this,
            "8670uh4gz1gbbp",
            "9xaiOv3ibWwsqCXi",
            "https://www.linkedin.com/developers/tools/oauth/redirect",
            true
        )

        linkedInRequestManager.isLoggedIn(object : LinkedInUserLoginValidationResponse {
            override fun activeLogin() {
                println("myLinkedin activeLogin")
            }

            override fun tokenExpired() {
                println("myLinkedin tokenExpired")
            }

            override fun notLogged() {
                println("myLinkedin notLogged")
            }
        })

        linkedInRequestManager.getLoggedRequestedMode(object : LinkedInUserLoginDetailsResponse {
            override fun loggedMode(mode: Int) {
                println("myLinkedin getLoggedRequestedMode $mode")
                when (mode) {
                    LinkedInRequestManager.MODE_LITE_PROFILE_ONLY -> {}
                    LinkedInRequestManager.MODE_EMAIL_ADDRESS_ONLY -> {}
                    LinkedInRequestManager.MODE_BOTH_OPTIONS -> {}
                }
            }

            override fun tokenExpired() {
                println("myLinkedin tokenExpired")
            }

            override fun notLogged() {
                println("myLinkedin notLogged")
            }
        })

        linkedInRequestManager.showAuthenticateView(LinkedInRequestManager.MODE_BOTH_OPTIONS)
    }

    override fun onGetAccessTokenFailed() {
        println("myLinkedin onGetAccessTokenFailed")
    }

    override fun onGetAccessTokenSuccess(linkedInAccessToken: LinkedInAccessToken?) {
        val linkedinAccessToken = linkedInAccessToken?.access_token
        println("myLinkedin linkedinAccessToken $linkedinAccessToken")
    }

    override fun onGetCodeFailed() {
        println("myLinkedin onGetCodeFailed")
    }

    override fun onGetCodeSuccess(code: String?) {
        println("myLinkedin onGetCodeSuccess $code")
    }

    override fun onGetProfileDataFailed() {
        println("myLinkedin onGetProfileDataFailed ")
    }

    override fun onGetProfileDataSuccess(linkedInUserProfile: LinkedInUserProfile?) {
        println("myLinkedin onGetProfileDataSuccess $linkedInUserProfile")
    }

    override fun onGetEmailAddressFailed() {
        println("myLinkedin onGetEmailAddressFailed")
    }

    override fun onGetEmailAddressSuccess(linkedInEmailAddress: LinkedInEmailAddress?) {
        val inkedinEml = linkedInEmailAddress?.emailAddress
        println("myLinkedin onGetEmailAddressSuccess $inkedinEml")
    }
    //LinkedIn End

    @Preview
    @Composable
    private fun PreviewBottomSheetSMConnect() {
        ContentBottomSheet(
            smInfoConnect = SocialMediaInfo.FACEBOOK,
            onConnect = {})
    }


    fun getSampleFbProfile(): SocialMediaProfile {
        val profile = Profile(
            "Jhone Smith",
            "https://t4.ftcdn.net/jpg/06/08/55/73/360_F_608557356_ELcD2pwQO9pduTRL30umabzgJoQn5fnd.jpg",
            "amalskr@gmail.com",
            "ask123"
        )

        return SocialMediaProfile(
            smInfo = SocialMediaInfo.FACEBOOK,
            profile = profile,
            timelines = mutableListOf(),
            pages = mutableListOf(),
            groups = mutableListOf()
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