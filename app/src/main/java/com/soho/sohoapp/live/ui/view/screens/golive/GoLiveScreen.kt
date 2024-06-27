package com.soho.sohoapp.live.ui.view.screens.golive

import android.util.Size
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.CustomCoverOption
import com.soho.sohoapp.live.enums.FBListType
import com.soho.sohoapp.live.enums.FieldConfig
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.enums.StepInfo
import com.soho.sohoapp.live.model.FbTypeView
import com.soho.sohoapp.live.model.SMProfile
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.AgentProfileGoLive
import com.soho.sohoapp.live.network.response.DataGoLive
import com.soho.sohoapp.live.network.response.Document
import com.soho.sohoapp.live.network.response.TsPropertyResponse
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.ButtonConnect
import com.soho.sohoapp.live.ui.components.ButtonGradientIcon
import com.soho.sohoapp.live.ui.components.ButtonOutLinedIcon
import com.soho.sohoapp.live.ui.components.CenterProgress
import com.soho.sohoapp.live.ui.components.DropDownWhatForLiveStream
import com.soho.sohoapp.live.ui.components.SearchBar
import com.soho.sohoapp.live.ui.components.SpacerHorizontal
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_12sp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_12sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text700_14spBold
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.TextAreaWhite
import com.soho.sohoapp.live.ui.components.TextFieldWhite
import com.soho.sohoapp.live.ui.components.TextStarRating
import com.soho.sohoapp.live.ui.components.TextSwipeSelection
import com.soho.sohoapp.live.ui.components.brushBottomGradientBg
import com.soho.sohoapp.live.ui.components.brushGradientLive
import com.soho.sohoapp.live.ui.components.brushGradientSetDateTime
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.components.brushPlanBtnGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppPrimaryDark
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.AppWhiteGray
import com.soho.sohoapp.live.ui.theme.BorderGray
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import com.soho.sohoapp.live.ui.theme.TextDark
import com.soho.sohoapp.live.ui.view.activity.MainViewModel
import com.soho.sohoapp.live.utility.NetworkUtils
import com.soho.sohoapp.live.utility.toUppercaseFirst
import com.soho.sohoapp.live.utility.visibleValue
import org.koin.compose.koinInject


@Composable
fun GoLiveScreen(
    navController: NavController,
    viewMMain: MainViewModel,
    savedApiResults: DataGoLive? = null,
    savedTsResults: TsPropertyResponse? = null,
    goLiveVm: GoLiveViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject(),
    onLoadApiResults: (DataGoLive) -> Unit,
    onLoadTSResults: (TsPropertyResponse) -> Unit
) {

    val stateVm = goLiveVm.mState.value
    val stepCount = 4
    var currentStepId by remember { mutableIntStateOf(0) }
    val optionList = mutableListOf("Option1", "Option 2", "Option 3")
    var selectedOption by remember { mutableStateOf("") }
    var isDateFixed by remember { mutableStateOf(false) }
    var isNetConnected by remember { mutableStateOf(true) }
    var isNowSelected by remember { mutableStateOf(false) }
    var isDontShowProfile by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        println("myFB Reloaded " + savedApiResults)
        if (savedApiResults == null) {
            callLoadPropertyApi(goLiveVm, netUtil, onUpdateNetStatus = {
                isNetConnected = it
                stateVm.apiResults?.let { apiRes -> onLoadApiResults.invoke(apiRes) }
            })
        }
    }

    //if SM connect success then open model
    val smFbProfile by goLiveVm.updatedFbProfile.collectAsState()
    if (smFbProfile.type == SocialMediaInfo.FACEBOOK) {
        viewMMain.saveSocialMediaProfile(getSampleFbProfile())
    }

    Box(
        modifier = Modifier
            .background(brushMainGradientBg)
            .fillMaxSize()
    ) {

        if (isNetConnected) {
            if (stateVm.loadingState == ProgressBarState.Loading) {
                CenterProgress(modifier = Modifier.align(Alignment.Center))
            } else {

                //save apiResults
                if (stateVm.isSuccess) {
                    stateVm.apiResults?.let { apiRes ->
                        onLoadApiResults.invoke(apiRes)
                    }

                    stateVm.tsResults?.let { tsRes ->
                        onLoadTSResults.invoke(tsRes)
                    }
                }

                //main steps content
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    item {
                        TopContent(stepCount, currentStepId)
                    }
                    item {
                        savedApiResults?.let { savedData ->

                            //Global ints for save click id and list

                            //Step #1 property list
                            val propertyList = stateVm.propertyListState?.value

                            //Step #2 agency list. agency list load from step #1 selections
                            val agencyList = stateVm.agencyListState?.value

                            StepContents(
                                currentStepId = currentStepId,
                                savedResults = savedData,
                                tsResults = savedTsResults,
                                propertyList = propertyList,
                                mainAgencyList = agencyList,
                                optionList = optionList,
                                isNowSelected = isNowSelected,
                                isNotShowProfile = isDontShowProfile,
                                selectedOption = selectedOption,
                                onSelectOption = { selectedOption = it },
                                onSwipeIsNowSelected = { isNowSelected = it },
                                onNotShowProfileChange = { isDontShowProfile = it },
                                onPropertyItemClicked = { selectedProperty ->
                                    goLiveVm.updatePropertyList(selectedProperty)
                                },
                                onAgentItemClicked = { selectedAgent ->
                                    goLiveVm.updateAgentSelectionList(selectedAgent)
                                },
                                onSMItemClicked = { selectedSM ->
                                    viewMMain.updateSocialMediaState(selectedSM)
                                }
                            )
                        }
                    }
                }

                //bottom button
                NextBackButtons(modifier = Modifier.align(Alignment.BottomCenter),
                    currentStepId = currentStepId,
                    isNowSelected = isNowSelected,
                    onClickedNext = {
                        if (currentStepId < stepCount - 1) {
                            currentStepId++
                        }
                    },
                    onClickedBack = {
                        currentStepId = (currentStepId - 1) % stepCount
                    },
                    onClickedDateTime = {
                        //POST /api/soho_live/live_stream
                    },
                    onClickedLive = {
                        //POST /api/soho_live/live_stream
                    })
            }
        } else {
            NoInternetScreen(onRetryClick = {
                callLoadPropertyApi(goLiveVm, netUtil, onUpdateNetStatus = {
                    isNetConnected = it
                    stateVm.apiResults?.let { apiRes -> onLoadApiResults.invoke(apiRes) }
                })
            })
        }
    }
}

fun getSampleFbProfile(): SocialMediaProfile {
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
        3,
        FBListType.PAGES,
        "MyPage",
        "http:www.facebook.com",
        "https://t4.ftcdn.net/jpg/06/08/55/73/360_F_608557356_ELcD2pwQO9pduTRL30umabzgJoQn5fnd.jpg"
    )

    val group1 = FbTypeView(
        4,
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

private fun callLoadPropertyApi(
    goLiveVm: GoLiveViewModel, netUtil: NetworkUtils, onUpdateNetStatus: (Boolean) -> Unit
) {
    if (netUtil.isNetworkAvailable()) {
        onUpdateNetStatus(true)
        goLiveVm.onTriggerEvent(GoLiveEvent.CallLoadProperties)
    } else {
        onUpdateNetStatus(false)
    }
}

@Composable
fun NoInternetScreen(onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text950_20sp(title = "No Internet Connection")
        SpacerVertical(size = 8.dp)
        Text700_14sp(step = "Please check your internet connection and try again.")
        SpacerVertical(size = 24.dp)
        ButtonColoured(text = "Retry", color = AppGreen, onBtnClick = {
            onRetryClick()
        })
    }
}

@Composable
fun TopContent(stepCount: Int, currentStepId: Int) {
    StepIndicator(totalSteps = stepCount, currentStep = currentStepId)
    SpacerVertical(16.dp)
    StepCountTitleInfo(currentStepId)
}

@Composable
fun StepContents(
    currentStepId: Int,
    savedResults: DataGoLive,
    tsResults: TsPropertyResponse? = null,
    propertyList: List<PropertyItem>? = null,
    mainAgencyList: List<AgencyItem>? = null,
    isNowSelected: Boolean,
    isNotShowProfile: Boolean,
    optionList: MutableList<String>,
    selectedOption: String,
    onSelectOption: (String) -> Unit,
    onSwipeIsNowSelected: (Boolean) -> Unit,
    onNotShowProfileChange: (Boolean) -> Unit,
    onPropertyItemClicked: (PropertyItem) -> Unit,
    onAgentItemClicked: (AgencyItem) -> Unit,
    onSMItemClicked: (SocialMediaInfo) -> Unit
) {
    SpacerVertical(40.dp)

    when (currentStepId) {
        // step#1
        0 -> {
            tsResults?.let { tsProp ->
                if (tsProp.propertyList.isEmpty()) {
                    DisplayNoData(message = "Not Found Properties")
                } else {
                    SearchBar()
                    SpacerVertical(16.dp)
                    PropertyListing(propertyList, onItemClicked = {
                        onPropertyItemClicked.invoke(it)
                    })
                    SpacerVertical(size = 70.dp)
                }
            } ?: run {
                DisplayNoData(message = "Property Information Serves Failed")
            }
        }

        // step#2
        1 -> {
            if (savedResults.agentProfiles.isEmpty()) {
                DisplayNoData(message = "No Agency Information")
            } else {

                ProfileHideItem(isNotShowProfile, onCheckedChange = {
                    onNotShowProfileChange.invoke(it)
                })

                mainAgencyList?.let { it1 ->
                    AgentListing(it1, onAgentItemClicked = { selected ->
                        onAgentItemClicked.invoke(selected)
                    })
                }
            }
        }

        // step#3
        2 -> {
            SocialMediaListing(onSMItemClicked = { selectedSM ->
                when (selectedSM) {
                    SocialMediaInfo.FACEBOOK.name -> {
                        onSMItemClicked.invoke(SocialMediaInfo.FACEBOOK)
                    }

                    SocialMediaInfo.YOUTUBE.name -> {
                        onSMItemClicked.invoke(SocialMediaInfo.YOUTUBE)
                    }

                    SocialMediaInfo.LINKEDIN.name -> {
                        onSMItemClicked.invoke(SocialMediaInfo.LINKEDIN)
                    }
                }
            })
            SpacerVertical(size = 70.dp)
        }

        // step#4
        3 -> {
            Content4(optionList = optionList,
                selectedOption = selectedOption,
                isNowSelected = isNowSelected,
                onSelectOption = { onSelectOption(it) },
                onSwipeIsNowSelected = { onSwipeIsNowSelected(it) })
        }
    }
}

@Composable
fun DisplayNoData(message: String) {
    SpacerVertical(size = 150.dp)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_no_data),
            contentDescription = "No Data",
            modifier = Modifier.size(64.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message, style = TextStyle(fontSize = 18.sp, color = Color.White)
        )
    }
}

@Composable
private fun Content4(
    optionList: MutableList<String>,
    selectedOption: String,
    isNowSelected: Boolean,
    onSelectOption: (String) -> Unit,
    onSwipeIsNowSelected: (Boolean) -> Unit
) {

    var isOnCoverOption by remember { mutableStateOf(false) }

    Text700_14sp(step = "When do you want to go live?")

    SpacerVertical(size = 8.dp)
    SwipeableSwitch(isNowSelected, onSwipeChange = {
        onSwipeIsNowSelected(it)
    })

    SpacerVertical(size = 24.dp)
    Text700_14sp(step = "What is this livestream for?")
    DropDownWhatForLiveStream(selectedValue = selectedOption,
        options = optionList,
        placeHolder = "Select an option",
        onValueChangedEvent = {
            onSelectOption(it)
        })

    SpacerVertical(size = 24.dp)
    Text700_14sp(step = "Stream title")
    TextFieldWhite(FieldConfig.NEXT.apply {
        placeholder = "Address or title for your livestream"
    }) {}

    SpacerVertical(size = 24.dp)
    Text700_14sp(step = "Description")
    TextAreaWhite(FieldConfig.NEXT.apply {
        placeholder =
            "Let viewers know more about what you are streaming. E.g. Property description, address, etc."
    }) {}


    if (false) {
        SpacerVertical(size = 40.dp)
        Text700_14sp(step = "Livestream cover image")
        Text400_14sp(info = "We’ve generated a cover image for your livestream. Cover image may be seen by viewers on connected social platforms and when you share your livestream link.")

        SpacerVertical(size = 16.dp)
        Image(
            painter = painterResource(id = R.drawable.sample_cover_image),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )

        SpacerVertical(size = 16.dp)
        ShareDownloadButtons()

        SpacerVertical(size = 24.dp)
        CustomizeCoverImageCard(isOnCoverOption, onCheckedChange = {
            isOnCoverOption = it
        })

        if (isOnCoverOption) {
            SpacerVertical(size = 16.dp)
            CustomizeCoverOptionCards()
        }
    }

    SpacerVertical(size = 140.dp)
}

@Composable
fun CustomizeCoverOptionCards() {
    CustomCoverOption.entries.forEach {
        CoverOptionItem(it)
    }
}

@Composable
fun CoverOptionItem(it: CustomCoverOption) {
    var isChecked by remember { mutableStateOf(it.isEnabled) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = ItemCardBg)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text700_14sp(step = it.title)
            Spacer(modifier = Modifier.weight(1f))
            SwitchCompo(isChecked, onCheckedChange = {
                isChecked = it
            })
        }
    }
}

@Composable
fun CustomizeCoverImageCard(isOnCoverOption: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = ItemCardBg)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text700_14sp(step = "Customize cover image")
                Spacer(modifier = Modifier.weight(1f))
                SwitchCompo(isOnCoverOption, onCheckedChange = {
                    onCheckedChange(it)
                })
            }

            //if true hide view plan button and show sub options
            if (!isOnCoverOption) {
                SpacerVertical(size = 20.dp)
                UpgradedPlansText()

                SpacerVertical(size = 16.dp)
                ButtonGradientIcon(text = "View Plans",
                    icon = R.drawable.ic_upgrade,
                    gradientBrush = brushPlanBtnGradientBg,
                    onBtnClick = {})
            }

        }
    }


}


@Composable
fun UpgradedPlansText() {
    val annotatedText = buildAnnotatedString {
        append("Upgrade your plans to ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Multicast 30 ")
        }
        append("and above to enjoy this feature")
    }

    Text(
        textAlign = TextAlign.Center,
        text = annotatedText,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
        fontWeight = FontWeight(700),
        color = HintGray,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun ShareDownloadButtons() {
    Row {
        ButtonOutLinedIcon(
            text = "Share", icon = R.drawable.ic_eye_vec, modifier = Modifier.weight(1f)
        ) {}
        SpacerHorizontal(size = 16.dp)
        ButtonOutLinedIcon(
            text = "Download", icon = R.drawable.ic_download, modifier = Modifier.weight(1f)
        ) {}
    }
}

@Composable
private fun SwipeableSwitch(isNowSelected: Boolean, onSwipeChange: (Boolean) -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = 16.dp
    val availableWidth = screenWidth - (horizontalPadding * 2)
    val indicatorWidth = availableWidth / 2
    val indicatorOffset by animateDpAsState(
        targetValue = if (isNowSelected) 0.dp else indicatorWidth, label = "animateToMove"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(width = 1.dp, color = BorderGray, shape = RoundedCornerShape(16.dp))
            .background(Color.Transparent)
    ) {
        //move selection
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(indicatorWidth)
                .padding(4.dp)
                .fillMaxHeight()
                .background(AppWhiteGray, shape = RoundedCornerShape(14.dp))
        )

        //two options
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextSwipeSelection(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSwipeChange(true) },
                title = "Now",
                textColor = if (isNowSelected) TextDark else AppWhite
            )

            TextSwipeSelection(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSwipeChange(false) },
                title = "Schedule for later",
                textColor = if (!isNowSelected) TextDark else AppWhite
            )
        }
    }
}

@Composable
private fun StepIndicator(
    totalSteps: Int,
    currentStep: Int,
    activeColor: Color = Color.White,
    inactiveColor: Color = Color.DarkGray
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0 until totalSteps) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(
                        color = if (i <= currentStep) activeColor else inactiveColor,
                        shape = CircleShape
                    )
            )

            if (i != totalSteps - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
private fun NextBackButtons(
    modifier: Modifier,
    currentStepId: Int,
    isNowSelected: Boolean,
    onClickedNext: () -> Unit,
    onClickedBack: () -> Unit,
    onClickedLive: () -> Unit,
    onClickedDateTime: () -> Unit
) {
    Box(
        modifier = modifier.background(
            brushBottomGradientBg, shape = RoundedCornerShape(
                topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp
            )
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (currentStepId > 0) {
                ButtonColoured(
                    text = stringResource(R.string.back),
                    color = Color.Transparent,
                    modifier = Modifier.weight(1f),
                    isBackButton = true
                ) {
                    onClickedBack.invoke()
                }
                Spacer(modifier = Modifier.width(16.dp))
            }

            if (currentStepId == 3) {
                if (isNowSelected) {
                    ButtonGradientIcon(text = "Preview Live",
                        icon = R.drawable.ic_livestream,
                        gradientBrush = brushGradientLive,
                        modifier = Modifier.weight(1f),
                        onBtnClick = {
                            onClickedLive.invoke()
                        })
                } else {
                    ButtonGradientIcon(text = "Set Date & Time",
                        icon = R.drawable.ic_calender,
                        gradientBrush = brushGradientSetDateTime,
                        modifier = Modifier.weight(1f),
                        onBtnClick = {
                            onClickedDateTime.invoke()
                        })
                }
            } else {
                ButtonColoured(
                    text = stringResource(R.string.next),
                    color = AppGreen,
                    modifier = Modifier.weight(1f)
                ) {
                    onClickedNext.invoke()
                }
            }
        }
    }
}

@Composable
private fun SocialMediaListing(onSMItemClicked: (String) -> Unit) {

    val visibleSMList = SocialMediaInfo.entries.filter {
        it.name != SocialMediaInfo.NONE.name
    }

    visibleSMList.forEach { item ->
        SocialMediaItemContent(item, onSMItemClicked = {
            onSMItemClicked.invoke(it)
        })
    }
}

@Composable
private fun AgentListing(
    agencyList: List<AgencyItem>,
    onAgentItemClicked: (AgencyItem) -> Unit
) {
    agencyList.forEach { agentItem ->
        AgencyItemContent(agentItem, onItemClicked = { selected ->
            onAgentItemClicked.invoke(selected)
        })
    }
}

@Composable
private fun PropertyListing(
    listings: List<PropertyItem>?,
    onItemClicked: (PropertyItem) -> Unit = {}
) {
    listings?.forEach { item ->
        PropertyItemContent(item, onItemClicked = { selected ->
            onItemClicked.invoke(selected)
        })
    }
}

@Composable
private fun ProfileHideItem(
    isDontShowProfile: Boolean,
    onCheckedChange: (Boolean) -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = ItemCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text700_14spBold(step = "Do not show profile")
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onCheckedChange(!isDontShowProfile) },
                contentAlignment = Alignment.Center
            ) {
                //CheckBox BG
                Image(
                    painter = if (isDontShowProfile) {
                        painterResource(id = R.drawable.check_box_chcked)
                    } else {
                        painterResource(id = R.drawable.cehck_box_uncheck)
                    }, contentDescription = null, contentScale = ContentScale.FillBounds
                )

                //Tick Icon
                if (isDontShowProfile) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_tick),
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}

@Composable
private fun SocialMediaItemContent(
    info: SocialMediaInfo,
    onSMItemClicked: (String) -> Unit
) {
    var isChecked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {

            //logo
            Column {

                if (info == SocialMediaInfo.SOHO) {
                    Text400_14sp(info = info.title, color = AppPrimaryDark)
                    SpacerVertical(size = 8.dp)
                }

                val imgSize = getImageWidth(info.icon)
                Image(
                    painter = painterResource(id = info.icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = if (imgSize.width == 0) {
                        Modifier
                    } else {
                        Modifier.size(imgSize.width.dp, imgSize.height.dp)
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (info.isConnect) {
                //switch
                SwitchCompo(isChecked, onCheckedChange = {
                    isChecked = it
                })
            } else {
                //button
                ButtonConnect(text = "Connect Now", color = AppGreen) {
                    onSMItemClicked.invoke(info.name)
                }
            }
        }
    }
}

@Composable
private fun getImageWidth(drawableResId: Int): Size {
    val context = LocalContext.current
    val drawable = ResourcesCompat.getDrawable(context.resources, drawableResId, null)
    val width = drawable?.intrinsicWidth ?: 0
    val height = drawable?.intrinsicHeight ?: 0

    if (width > 1000) {
        return Size(105, 24)
    } else {
        return Size(0, 0)
    }

}

@Composable
private fun AgencyItemContent(item: AgencyItem, onItemClicked: (AgencyItem) -> Unit = {}) {
    val cardBgColor = if (item.isChecked) AppWhite else ItemCardBg
    val textColor = if (item.isChecked) ItemCardBg else AppWhite
    val agent = item.agentProfile

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onItemClicked(item.apply { isChecked = !isChecked }) },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            //image
            val urlPainter = rememberAsyncImagePainter(
                model = agent.imageUrl,
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
                    .padding(start = 14.dp)
                    .fillMaxWidth()
            ) {
                ProfileNameCheckBox(agent, item.isChecked, textColor)
                SpacerVertical(size = 8.dp)
                agent.email?.let {
                    Text400_14sp(info = it, color = textColor)
                }
            }
        }
    }
}

@Composable
private fun PropertyItemContent(item: PropertyItem, onItemClicked: (PropertyItem) -> Unit = {}) {
    val cardBgColor = if (item.isChecked) AppWhite else ItemCardBg
    val textColor = if (item.isChecked) ItemCardBg else AppWhite
    val property = item.propInfo

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onItemClicked(item.apply { isChecked = !isChecked }) },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp)) {
            //image
            val urlPainter = rememberAsyncImagePainter(
                model = property.thumbnailUrl(),
                placeholder = painterResource(id = R.drawable.property_placeholder),
                error = painterResource(id = R.drawable.property_placeholder)
            )

            Image(
                painter = urlPainter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(width = 70.dp, height = 68.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            //info
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .fillMaxWidth()
            ) {

                TypeAndCheckBox(item.isChecked, property, txtColor = textColor, onCheckedChange = {
                    onItemClicked(item)
                })
                SpacerVertical(size = 8.dp)
                Text700_14sp(step = property.fullAddress(), color = textColor)
                SpacerVertical(size = 8.dp)
                if (false) Text400_14sp(info = "3 scheduled livestream", color = textColor)
                SpacerVertical(size = 8.dp)
                AmenitiesView(property, textColor)
            }
        }
    }
}

@Composable
private fun AmenitiesView(doc: Document, textColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        doc.bedroomCount.visibleValue()?.let {
            Text400_12sp(label = it, txtColor = textColor)
            AmenitiesIcon(icon = R.drawable.ic_bedroom, iconColor = textColor)
        }

        doc.bathroomCount.visibleValue()?.let {
            Text400_12sp(label = it, txtColor = textColor)
            AmenitiesIcon(icon = R.drawable.ic_bathroom, iconColor = textColor)
        }

        doc.carspotCount.visibleValue()?.let {
            Text400_12sp(label = it, txtColor = textColor)
            AmenitiesIcon(icon = R.drawable.ic_car_park, iconColor = textColor)
        }

        doc.areaSize()?.let {
            Text400_12sp(label = it.first, txtColor = textColor)
            AmenitiesIcon(icon = it.second, iconColor = textColor)
        }
    }
}

@Composable
private fun AmenitiesIcon(icon: Int, iconColor: Color = AppWhite) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = null,
        modifier = Modifier.padding(start = 4.dp, end = 8.dp),
        contentScale = ContentScale.FillBounds,
        colorFilter = ColorFilter.tint(iconColor)
    )
}

@Composable
private fun TypeAndCheckBox(
    isChecked: Boolean,
    doc: Document,
    txtColor: Color = AppWhite,
    onCheckedChange: (Boolean) -> Unit = {}
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            doc.apListingState?.toUppercaseFirst()?.let {
                Text700_12sp(label = it)
            }
            Image(
                painter = painterResource(id = R.drawable.space_dot),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 8.dp),
                contentScale = ContentScale.FillBounds
            )
            doc.propertyType?.toUppercaseFirst()?.let {
                Text400_12sp(label = it, txtColor = txtColor)
            }
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable { onCheckedChange(!isChecked) }, contentAlignment = Alignment.Center
        ) {
            //CheckBox BG
            Image(
                painter = if (isChecked) {
                    painterResource(id = R.drawable.check_box_chcked)
                } else {
                    painterResource(id = R.drawable.cehck_box_uncheck)
                }, contentDescription = null, contentScale = ContentScale.FillBounds
            )

            //Tick Icon
            if (isChecked) {
                Image(
                    painter = painterResource(id = R.drawable.ic_tick),
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }

    }
}

@Composable
private fun ProfileNameCheckBox(
    profile: AgentProfileGoLive,
    isChecked: Boolean,
    textColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        profile.name?.let { Text700_14spBold(step = it, txtColor = textColor) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Right
        ) {

            if (profile.rating != 0f) {
                TextStarRating(rate = "* ${profile.rating}")
            }

            SpacerHorizontal(size = 16.dp)

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                //CheckBox BG
                Image(
                    painter = if (isChecked) {
                        painterResource(id = R.drawable.check_box_chcked)
                    } else {
                        painterResource(id = R.drawable.cehck_box_uncheck)
                    }, contentDescription = null, contentScale = ContentScale.FillBounds
                )

                //Tick Icon
                if (isChecked) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_tick),
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}

private fun getStepInfo(currentStepId: Int): StepInfo {
    return StepInfo.entries[(currentStepId) % StepInfo.entries.size]
}

@Composable
private fun StepCountTitleInfo(currentStepId: Int) {
    val stepInfo = getStepInfo(currentStepId)

    Text700_14sp(step = stepInfo.counter)
    SpacerVertical(8.dp)
    Text950_20sp(title = stepInfo.title)
    SpacerVertical(8.dp)
    Text400_14sp(info = stepInfo.info)
}


@Composable
fun SwitchCompo(isChecked: Boolean = false, onCheckedChange: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable {
                onCheckedChange(!isChecked)
            }, contentAlignment = Alignment.Center
    ) {
        Image(
            painter = if (isChecked) {
                painterResource(id = R.drawable.switch_on)
            } else {
                painterResource(id = R.drawable.switch_off)
            }, contentDescription = null, contentScale = ContentScale.FillBounds
        )
    }
}

data class PropertyItem(val id: Int, val propInfo: Document, var isChecked: Boolean = false)

data class AgencyItem(
    val id: Int,
    val agentProfile: AgentProfileGoLive,
    var isChecked: Boolean = false
)

@Preview
@Composable
private fun PreviewGoLiveScreen() {
    Box(modifier = Modifier.background(brushMainGradientBg)) {
        val countSteps = 4
        val currentStep = 3

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            item {
                TopContent(countSteps, currentStep)
            }
            item {
                StepContents(currentStepId = currentStep,
                    savedResults = DataGoLive(emptyList(), emptyList()),
                    optionList = mutableListOf(),
                    isNowSelected = true,
                    isNotShowProfile = true,
                    selectedOption = "",
                    onSelectOption = { },
                    onSwipeIsNowSelected = { },
                    onNotShowProfileChange = {},
                    onPropertyItemClicked = {},
                    onAgentItemClicked = {},
                    onSMItemClicked = {})
            }
        }

        NextBackButtons(modifier = Modifier.align(Alignment.BottomCenter),
            currentStepId = currentStep,
            isNowSelected = true,
            onClickedNext = {},
            onClickedBack = {},
            onClickedDateTime = {},
            onClickedLive = {})
    }
}
