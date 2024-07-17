package com.soho.sohoapp.live.ui.view.screens.golive

import android.annotation.SuppressLint
import android.util.Size
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.enums.CategoryType
import com.soho.sohoapp.live.enums.CustomCoverOption
import com.soho.sohoapp.live.enums.FormFields
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.enums.StepInfo
import com.soho.sohoapp.live.model.AgencyItem
import com.soho.sohoapp.live.model.GoLivePlatform
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.PlatformToken
import com.soho.sohoapp.live.model.PropertyItem
import com.soho.sohoapp.live.model.ScheduleDateTime
import com.soho.sohoapp.live.model.ScheduleSlots
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.model.TextFiledConfig
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.AgentProfileGoLive
import com.soho.sohoapp.live.network.response.DataGoLive
import com.soho.sohoapp.live.network.response.Document
import com.soho.sohoapp.live.network.response.TsPropertyResponse
import com.soho.sohoapp.live.ui.components.AppAlertDialog
import com.soho.sohoapp.live.ui.components.ButtonColoredIcon
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.ButtonColouredWrap
import com.soho.sohoapp.live.ui.components.ButtonConnect
import com.soho.sohoapp.live.ui.components.ButtonGradientIcon
import com.soho.sohoapp.live.ui.components.ButtonOutLinedIcon
import com.soho.sohoapp.live.ui.components.CenterMessageProgress
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
import com.soho.sohoapp.live.ui.components.TextFieldOutlined
import com.soho.sohoapp.live.ui.components.TextIconSwipeSelection
import com.soho.sohoapp.live.ui.components.TextStarRating
import com.soho.sohoapp.live.ui.components.brushBottomGradientBg
import com.soho.sohoapp.live.ui.components.brushGradientLive
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.components.brushPlanBtnGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppPrimaryDark
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.AppWhiteGray
import com.soho.sohoapp.live.ui.theme.BorderGray
import com.soho.sohoapp.live.ui.theme.ErrorRed
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import com.soho.sohoapp.live.ui.theme.TextDark
import com.soho.sohoapp.live.ui.view.activity.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.schedule.DateTimePicker
import com.soho.sohoapp.live.ui.view.screens.schedule.ScheduleItemView
import com.soho.sohoapp.live.ui.view.screens.schedule.ShowDeleteAlert
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import com.soho.sohoapp.live.utility.NetworkUtils
import com.soho.sohoapp.live.utility.openLiveCaster
import com.soho.sohoapp.live.utility.toUppercaseFirst
import com.soho.sohoapp.live.utility.visibleValue
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@SuppressLint("MutableCollectionMutableState")
@Composable
fun GoLiveScreen(
    navController: NavController,
    viewMMain: MainViewModel,
    savedApiResults: DataGoLive? = null,
    savedTsResults: TsPropertyResponse? = null,
    savedState: GoLiveAssets? = null,
    mGoLiveSubmit: GoLiveSubmit,
    goLiveVm: GoLiveViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject(),
    onLoadApiResults: (DataGoLive) -> Unit,
    onLoadTSResults: (TsPropertyResponse) -> Unit,
    onUpdateState: (GoLiveAssets) -> Unit
) {

    val stateVm = goLiveVm.mState.value
    val assetsState = savedState ?: goLiveVm.assetsState.value
    val stepCount = 5
    var currentStepId by remember { mutableIntStateOf(assetsState.stepId.value) }
    val optionList = mutableListOf("Inspection", "Auction", "Other")
    var isNetConnected by remember { mutableStateOf(true) }
    var isNowSelected by remember { mutableStateOf(assetsState.isNowSelected.value) }
    var isNoSlots by rememberSaveable { mutableStateOf(false) }
    var isDontShowProfile by remember { mutableStateOf(mGoLiveSubmit.unlisted) }
    var isShowScheduleOkScreen by remember { mutableStateOf(false) }
    val stateSMConnected by goLiveVm.connectedProfileNames.collectAsStateWithLifecycle()
    val checkedSM = remember { mutableStateListOf<String>() }
    var mFieldsError by remember { mutableStateOf(mutableMapOf<FormFields, String>()) }
    val eventState =
        AppEventBus.events.collectAsState(initial = AppEvent.SMProfile(SocialMediaProfile()))
    val alertState = remember { mutableStateOf(Pair(false, null as AlertConfig?)) }

    /*
    * if goLiveApi got success response then want to open the LiveCast Screen
    * */
    LaunchedEffect(stateVm.goLiveResults) {
        stateVm.goLiveResults?.let {

            if (isNowSelected) {
                openLiveCaster(it)
            } else {
                isShowScheduleOkScreen = true
            }

            //Reset steps flow
            currentStepId = 0
            resetSteps(currentStepId, mGoLiveSubmit, assetsState)
        }
    }

    /*
    * Connect the SM need to update the mGoLiveSubmit
    * */
    LaunchedEffect(eventState.value) {
        saveSMProfileInGoLiveData(eventState, mGoLiveSubmit, checkedSM, onReloadDataStore = {
            goLiveVm.loadConnectedSMList()
            viewMMain.resetSendEvent()
        })
    }

    /*
    * Checking alert state updated or not
    * */
    LaunchedEffect(stateVm.alertState) {
        alertState.value = getAlertConfig(stateVm)
    }

    /*
    * Show alert when state change
    * */
    ShowAlert(alertState.value, onDismiss = {
        alertState.value = Pair(false, null)
        goLiveVm.onTriggerEvent(GoLiveEvent.DismissAlert)
    })

    /*
    * Load main data when api is connected and already connected need to get it from cache when reloading
    * */
    LaunchedEffect(Unit) {
        if (savedApiResults == null) {
            callLoadPropertyApi(goLiveVm, netUtil, onUpdateNetStatus = {
                isNetConnected = it
                stateVm.apiResults?.let { apiRes ->
                    onLoadApiResults.invoke(apiRes)
                }
            })
        } else {
            //fill saved data into localDataSet
            checkedSM.clear()
            checkedSM.addAll(mGoLiveSubmit.checkedPlatforms)
            mFieldsError = mGoLiveSubmit.errors
            goLiveVm.updateAssetsState(savedTsResults, savedApiResults, savedState)
        }

        goLiveVm.loadConnectedSMList()
    }

    /*
    * open Schedule Screen
    * */
    LaunchedEffect(isShowScheduleOkScreen) {
        if (isShowScheduleOkScreen) {
            navController.navigate(NavigationPath.GO_LIVE_SUCCESS.name)
            isShowScheduleOkScreen = false
        }
    }

    Box(
        modifier = Modifier
            .background(brushMainGradientBg)
            .fillMaxSize()
    ) {

        if (isNetConnected) {
            if (stateVm.loadingState == ProgressBarState.Loading) {
                CenterMessageProgress(message = stateVm.loadingMessage)
            } else {

                //save apiResults
                if (stateVm.isSuccess) {
                    stateVm.apiResults?.let { apiRes ->
                        onLoadApiResults.invoke(apiRes)
                    }

                    stateVm.tsResults?.let { tsRes ->
                        onLoadTSResults.invoke(tsRes)
                    }

                    onUpdateState(goLiveVm.assetsState.value)
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

                            //Step #1 property list
                            val propertyList = assetsState.propertyListState?.value

                            //Step #2 agency list. agency list load from step #1 selections
                            val agencyList = assetsState.agencyListState?.value

                            //All Steps
                            StepContents(
                                currentStepId = currentStepId,
                                savedResults = savedData,
                                tsResults = savedTsResults,
                                propertyList = propertyList,
                                mainAgencyList = agencyList,
                                optionList = optionList,
                                isNowSelected = isNowSelected,
                                isNoSlots = isNoSlots,
                                isNotShowProfile = isDontShowProfile,
                                mGoLiveSubmit = mGoLiveSubmit,
                                mFieldsError = mFieldsError,
                                stateSMConnected = stateSMConnected,
                                onSwipeIsNowSelected = {
                                    isNowSelected = it
                                    assetsState.isNowSelected.value = it
                                },
                                onNotShowProfileChange = {
                                    mGoLiveSubmit.apply { unlisted = it }
                                    isDontShowProfile = it
                                },
                                onPropertyItemClicked = { selectedProperty ->
                                    mGoLiveSubmit.apply {
                                        propertyId = if (selectedProperty.isChecked) {
                                            selectedProperty.propInfo.id?.toInt() ?: 0
                                        } else {
                                            0
                                        }
                                        title = if (selectedProperty.isChecked) {
                                            selectedProperty.propInfo.fullAddress()
                                        } else {
                                            ""
                                        }
                                    }
                                    goLiveVm.updatePropertyList(selectedProperty)
                                },
                                onAgentItemClicked = { selectedAgent ->
                                    mGoLiveSubmit.apply {
                                        agentId = if (selectedAgent.isChecked) {
                                            selectedAgent.agentProfile.id
                                        } else {
                                            0
                                        }
                                    }
                                    goLiveVm.updateAgentSelectionList(selectedAgent)
                                },
                                onSMItemClicked = { selectedSM ->

                                    /*
                                    * if SM Connected then open resultSM bottomSheet
                                    * or else open connectSM bottomSheet
                                    * */

                                    if (selectedSM.isConnect) {

                                        if (selectedSM.isItemChecked) {
                                            viewMMain.updateSMConnectedState(selectedSM)
                                        } else {
                                            val currentPT = mGoLiveSubmit.platformToken
                                            val platformName = selectedSM.name.lowercase()

                                            checkedSM.remove(platformName)
                                            currentPT.removeIf { it.platform == platformName }

                                            mGoLiveSubmit.apply {
                                                checkedPlatforms = checkedSM
                                                platformToken = currentPT
                                            }
                                        }

                                    } else {
                                        viewMMain.updateSocialMediaState(selectedSM)
                                    }
                                })
                        }
                    }
                }

                //bottom button
                NextBackButtons(modifier = Modifier.align(Alignment.BottomCenter),
                    currentStepId = currentStepId,
                    stepCount = stepCount,
                    isNowSelected = isNowSelected,
                    onClickedNext = {
                        val isAllowGo = isAllowGoNext(
                            currentStepId = currentStepId,
                            mGoLiveSubmit = mGoLiveSubmit,
                            goLiveVm = goLiveVm,
                            onValidateRes = {
                                mFieldsError = it.errors
                            }
                        )

                        if (currentStepId < stepCount - 1 && isAllowGo) {
                            currentStepId++
                            assetsState.stepId.value = currentStepId
                        }
                    },
                    onClickedBack = {
                        currentStepId = (currentStepId - 1) % stepCount
                        assetsState.stepId.value = currentStepId
                    },
                    onClickedFinalise = {
                        isNoSlots = mGoLiveSubmit.scheduleSlots.isEmpty()

                        if (!isNoSlots) {
                            //add utc dateFormat for schedules
                            convertScheduleUtc(mGoLiveSubmit)

                            callApi(
                                mGoLiveSubmit,
                                mFieldsError,
                                netUtil,
                                goLiveVm,
                                false,
                                onErrorsUpdate = {
                                    mFieldsError = it
                                })
                        }
                    },
                    onClickedLive = {
                        callApi(mGoLiveSubmit, mFieldsError, netUtil, goLiveVm, onErrorsUpdate = {
                            mFieldsError = it
                        })
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

/*
* mGoLiveSubmit.scheduleSlots convert to the utc timeFormat like "2024-07-13T06:30:00Z"
* */

private fun convertScheduleUtc(mGoLiveSubmit: GoLiveSubmit) {
    val updatedSchedulesAt: MutableList<ScheduleDateTime> = mutableListOf()

    mGoLiveSubmit.scheduleSlots.forEach { dateTimeString ->
        dateTimeString.display?.let { dateTime ->

            val formatter = SimpleDateFormat("d/M/yyyy, hh:mm a", Locale.US)
            formatter.timeZone = TimeZone.getDefault()
            val localDate = formatter.parse(dateTime)

            val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            utcFormatter.timeZone = TimeZone.getTimeZone("UTC")

            localDate?.let {
                updatedSchedulesAt.add(ScheduleDateTime(dateTime = utcFormatter.format(it)))
            }
        }
    }

    mGoLiveSubmit.apply {
        schedulesAt = updatedSchedulesAt
    }
}

fun callApi(
    mGoLiveSubmit: GoLiveSubmit,
    mFieldsError: MutableMap<FormFields, String>,
    netUtil: NetworkUtils,
    goLiveVm: GoLiveViewModel,
    isGoLive: Boolean = true,
    onErrorsUpdate: (MutableMap<FormFields, String>) -> Unit
) {
    mGoLiveSubmit.apply { errors = mGoLiveSubmit.validateData() }
    onErrorsUpdate.invoke(mGoLiveSubmit.errors)

    if (netUtil.isNetworkAvailable()) {
        if (mFieldsError.isEmpty()) {

            if (isGoLive) {
                goLiveVm.onTriggerEvent(GoLiveEvent.CallSubmitGoLive(mGoLiveSubmit))
            } else {
                goLiveVm.onTriggerEvent(GoLiveEvent.CallSubmitSchedule(mGoLiveSubmit))
            }

        } else {
            goLiveVm.showAlert(
                getAlertConfig(
                    context.getString(R.string.attention),
                    context.getString(R.string.attention_message)
                )
            )
        }
    } else {
        goLiveVm.showAlert(
            getAlertConfig(
                context.getString(R.string.connection_lost),
                context.getString(R.string.no_net_msg)
            )
        )
    }
}

fun resetSteps(currentStepId: Int, mGoLiveSubmit: GoLiveSubmit, assetsState: GoLiveAssets) {
    assetsState.stepId.value = currentStepId

    assetsState.agencyListState?.value =
        assetsState.agencyListState?.value?.map { item ->
            if (item.isChecked) item.copy(isChecked = false) else item
        } ?: listOf()

    assetsState.propertyListState?.value =
        assetsState.propertyListState?.value?.map { item ->
            if (item.isChecked) item.copy(isChecked = false) else item
        } ?: listOf()

    mGoLiveSubmit.apply {
        purpose = null
        propertyId = 0
        title = null
        description = null
        agentId = 0
        unlisted = false
        targets.clear()
        platformToken.clear()
        scheduleSlots.clear()
        errors.clear()
        checkedPlatforms.clear()
    }
}

fun isAllowGoNext(
    currentStepId: Int,
    mGoLiveSubmit: GoLiveSubmit,
    goLiveVm: GoLiveViewModel,
    onValidateRes: (GoLiveSubmit) -> Unit
): Boolean {
    return when (currentStepId) {
        0 -> {
            if (mGoLiveSubmit.propertyId == 0) {
                goLiveVm.showAlert(
                    getAlertConfig(
                        context.getString(R.string.selection_required),
                        context.getString(R.string.select_property)
                    )
                )
                false
            } else {
                true
            }
        }

        1 -> {
            if (mGoLiveSubmit.unlisted) {
                true
            } else {
                if (mGoLiveSubmit.agentId == 0) {
                    goLiveVm.showAlert(
                        getAlertConfig(
                            context.getString(R.string.selection_required),
                            context.getString(R.string.select_agent)
                        )
                    )
                    false
                } else {
                    true
                }
            }
        }

        2 -> {
            if (mGoLiveSubmit.platformToken.isNotEmpty()) {
                true
            } else {
                goLiveVm.showAlert(
                    getAlertConfig(
                        context.getString(R.string.selection_required),
                        context.getString(R.string.select_platform)
                    )
                )
                false
            }
        }

        3 -> {
            mGoLiveSubmit.apply { errors = mGoLiveSubmit.validateData() }
            val errorList = mGoLiveSubmit.errors
            onValidateRes.invoke(mGoLiveSubmit)
            errorList.isEmpty()
        }

        else -> {
            true
        }
    }
}

@Composable
fun ShowAlert(value: Pair<Boolean, AlertConfig?>, onDismiss: () -> Unit) {
    if (value.first) {
        value.second?.let {
            AppAlertDialog(alert = it, onConfirm = {
                onDismiss()
            }, onDismiss = {
                onDismiss()
            })
        }
    }
}

fun getAlertConfig(alertTitle: String?, msg: String): AlertConfig {
    return AlertConfig.COMMON_OK.apply {
        title = alertTitle ?: context.getString(R.string.problem)
        message = msg
    }
}

fun getAlertConfig(state: GoLiveState): Pair<Boolean, AlertConfig?> {
    return if (state.alertState is AlertState.Display) {
        val alertConfig = state.alertState.config
        Pair(true, alertConfig)
    } else {
        Pair(false, null)
    }
}

fun saveSMProfileInGoLiveData(
    eventState: State<Any>,
    mGoLiveSubmit: GoLiveSubmit,
    checkedSM: SnapshotStateList<String>,
    onReloadDataStore: () -> Unit
) {
    val smProfile = when (val event = eventState.value) {
        is AppEvent.SMProfile -> event.smProfile
        else -> SocialMediaProfile()
    }

    if (smProfile.smInfo != SocialMediaInfo.NONE) {
        onReloadDataStore()

        //get current platform list
        val currentPlatformToken = mGoLiveSubmit.platformToken.toMutableList()
        val liveTargets = mGoLiveSubmit.targets.toMutableList()

        val smSelection = smProfile.smInfo.selectionType
        val platformName = smProfile.smInfo.name.lowercase()
        val token = smProfile.profile.token

        /*
        * according to the selectedType need to get selected platform and token from the pages,groups and timeline arrayList
        * */
        smSelection?.let { selectedType ->
            if (smProfile.smInfo == SocialMediaInfo.FACEBOOK) {
                when (selectedType.type) {
                    CategoryType.TIMELINE -> {
                        val goPlatform = GoLivePlatform(
                            targetFeedId = "me",
                            platform = platformName,
                            accessToken = selectedType.accessToken
                        )
                        liveTargets.removeIf {
                            it.accessToken == goPlatform.accessToken
                        }
                        liveTargets.add(goPlatform)
                    }

                    CategoryType.PAGES -> {
                        val goPlatform = GoLivePlatform(
                            targetFeedId = selectedType.id,
                            platform = platformName,
                            accessToken = selectedType.accessToken
                        )
                        liveTargets.removeIf {
                            it.accessToken == goPlatform.accessToken
                        }
                        liveTargets.add(goPlatform)
                    }

                    CategoryType.GROUPS -> {
                        val goPlatform = GoLivePlatform(
                            targetFeedId = selectedType.id,
                            platform = platformName,
                            accessToken = selectedType.accessToken
                        )
                        liveTargets.removeIf {
                            it.accessToken == goPlatform.accessToken
                        }
                        liveTargets.add(goPlatform)
                    }
                }
            } else {
                val goPlatform = GoLivePlatform(
                    targetFeedId = "",
                    platform = platformName,
                    accessToken = selectedType.accessToken
                )
                liveTargets.removeIf {
                    it.accessToken == goPlatform.accessToken
                }
                liveTargets.add(goPlatform)
            }
        }


        //update new platform list
        if (smProfile.profile.isConnected) {
            currentPlatformToken.add(
                PlatformToken(
                    platform = platformName,
                    accessToken = token.toString()
                )
            )

        } else {
            currentPlatformToken.removeIf { it.platform == platformName }
        }

        //save default checked state
        checkedSM.add(platformName)

        //finally apply updated platform list to the mGoLiveSubmit
        mGoLiveSubmit.apply {
            platformToken = currentPlatformToken
            checkedPlatforms = checkedSM
            targets = liveTargets
        }
    }
}

fun GoLiveSubmit.validateData(): MutableMap<FormFields, String> {
    val errors = mutableMapOf<FormFields, String>()

    if (purpose.isNullOrEmpty()) {
        errors[FormFields.PURPOSE] = "Livestream cannot be null or empty"
    } else {
        errors.remove(FormFields.PURPOSE)
    }

    if (title.isNullOrEmpty()) {
        errors[FormFields.TITLE] = "Title cannot be null or empty"
    } else {
        errors.remove(FormFields.TITLE)
    }

    return errors
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
    mGoLiveSubmit: GoLiveSubmit,
    isNowSelected: Boolean,
    isNoSlots: Boolean,
    isNotShowProfile: Boolean,
    optionList: MutableList<String>,
    mFieldsError: MutableMap<FormFields, String>,
    stateSMConnected: MutableList<SocialMediaInfo>,
    onSwipeIsNowSelected: (Boolean) -> Unit,
    onNotShowProfileChange: (Boolean) -> Unit,
    onPropertyItemClicked: (PropertyItem) -> Unit,
    onAgentItemClicked: (AgencyItem) -> Unit,
    onSMItemClicked: (SocialMediaInfo) -> Unit
) {
    SpacerVertical(40.dp)

    when (currentStepId) {
        // step #1
        0 -> {
            propertyList?.let { propList ->
                if (propList.isEmpty()) {
                    DisplayNoData(message = "Not Found Properties")
                } else {
                    SearchBar()
                    SpacerVertical(16.dp)
                    PropertyListing(propList, onItemClicked = {
                        onPropertyItemClicked.invoke(it)
                    })
                    SpacerVertical(size = 70.dp)
                }
            } ?: run {
                DisplayNoData(message = "Property Information Serves Failed")
            }
        }

        // step #2
        1 -> {
            mainAgencyList?.let { agentList ->
                if (agentList.isEmpty()) {
                    DisplayNoData(message = "No Agency Information")
                } else {
                    ProfileHideItem(isNotShowProfile, onCheckedChange = {
                        onNotShowProfileChange.invoke(it)
                    })

                    AgentListing(mainAgencyList, onAgentItemClicked = { selected ->
                        onAgentItemClicked.invoke(selected)
                    })
                }
            } ?: run {
                DisplayNoData(message = "Agency Information Load Failed")
            }
        }

        // step #3
        2 -> {
            SocialMediaListing(
                stateSMConnected = stateSMConnected,
                onSMItemClicked = { selectedSM ->
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
                },
                onSMItemChecked = { smInfo ->
                    onSMItemClicked.invoke(smInfo)
                },
                onUpdateInitialState = { smAllList ->
                    val smList = smAllList.filterNot { it.name == SocialMediaInfo.SOHO.name }
                    val selectedSMList = smList.filter { it.isConnect && it.isItemChecked }

                    val updatedPlatformToken: MutableList<PlatformToken> =
                        selectedSMList.mapNotNull { sm ->
                            PlatformToken(
                                platform = sm.name.lowercase(),
                                accessToken = sm.accessToken.toString()
                            )
                        }.toMutableList()

                    mGoLiveSubmit.apply {
                        platformToken = updatedPlatformToken
                    }
                })
            SpacerVertical(size = 70.dp)
        }

        // step #4
        3 -> {
            Content4(
                optionList = optionList,
                mGoLiveSubmit = mGoLiveSubmit,
                mFieldsError = mFieldsError
            )
        }

        // step #5
        4 -> {
            Content5(
                goLiveData = mGoLiveSubmit,
                isNowSelected = isNowSelected,
                isNoSlots = isNoSlots,
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
    mGoLiveSubmit: GoLiveSubmit,
    mFieldsError: MutableMap<FormFields, String>
) {
    var txtCounter by rememberSaveable { mutableStateOf("0/3000") }
    var isOnCoverOption by remember { mutableStateOf(false) }
    val errPurpose = mFieldsError[FormFields.PURPOSE]
    val errTitle = mFieldsError[FormFields.TITLE]

    val configPurpose = TextFiledConfig(
        input = mGoLiveSubmit.purpose.orEmpty(),
        placeholder = "Enter Purpose",
        isError = !errPurpose.isNullOrEmpty()
    )

    val configTitle = TextFiledConfig(
        input = mGoLiveSubmit.title.orEmpty(),
        placeholder = "Address or title for your livecast",
        isError = !errTitle.isNullOrEmpty(),
    )

    val configDesc = TextFiledConfig(
        input = mGoLiveSubmit.description.orEmpty(),
        placeholder = "Let viewers know more about what you are streaming. E.g. Property description, address, etc.",
        imeAction = ImeAction.Done
    )

    //what for livestream
    Text700_14sp(step = "What is this livestream for?")
    DropDownWhatForLiveStream(
        options = optionList, placeHolder = "Select an option", onValueChangedEvent = {
            mGoLiveSubmit.apply { purpose = it }
        }, fieldConfig = configPurpose
    )
    errPurpose?.let {
        ShowError(message = it)
    }

    //title
    SpacerVertical(size = 24.dp)
    Text700_14sp(step = "Stream title")
    TextFieldOutlined(tfConfig = configTitle, onTextChange = {
        mGoLiveSubmit.apply { title = it }
    })
    errTitle?.let {
        ShowError(message = it)
    }

    //description
    SpacerVertical(size = 24.dp)
    Row {
        Text700_14sp(step = "Description", modifier = Modifier.weight(1f))
        Text700_14sp(step = txtCounter, isBold = false)
    }
    TextAreaWhite(fieldConfig = configDesc, onTextChange = {
        mGoLiveSubmit.apply { description = it.first }
        txtCounter = it.second
    })

    //cover image
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
private fun Content5(
    goLiveData: GoLiveSubmit,
    isNowSelected: Boolean,
    isNoSlots: Boolean,
    onSwipeIsNowSelected: (Boolean) -> Unit
) {
    var slotToDelete by remember { mutableStateOf<ScheduleSlots?>(null) }
    var isShowDialog by remember { mutableStateOf(false) }
    var isShowDateTimePicker by remember { mutableStateOf(false) }
    val slots by remember { mutableStateOf(goLiveData.scheduleSlots.toMutableStateList()) }

    //confirmation to delete time slot
    if (isShowDialog) {
        ShowDeleteAlert(
            isShowDialog = isShowDialog,
            slotToDelete = slotToDelete,
            onDismiss = { isShowDialog = it },
            onDelete = {
                slots.remove(it)
                goLiveData.apply {
                    this.scheduleSlots = slots
                }
            }
        )
    }

    //open DateTimePicker
    if (isShowDateTimePicker) {
        DateTimePicker(onDismissed = { isShowDateTimePicker = !it }, onDateTimeSelect = {
            if (!it.date.isNullOrEmpty() && !it.time.isNullOrEmpty()) {
                //isNoSchedules = false
                slots.add(it)
                goLiveData.apply {
                    this.scheduleSlots = slots
                }
            }
        })
    }

    //swipe selection
    Text700_14sp(step = "When do you want to go live?")
    SpacerVertical(size = 8.dp)
    SwipeSwitchWhenGo(isNowSelected, onSwipeChange = {
        onSwipeIsNowSelected(it)
    })

    //this is for Schedule section content
    if (!isNowSelected) {
        SpacerVertical(size = 40.dp)
        Text700_14sp(step = "Set date & time")

        SpacerVertical(size = 8.dp)
        Text700_14sp(step = "You can schedule multiple livecast for this listing", isBold = false)

        SpacerVertical(size = 16.dp)

        //Schedule List
        if (slots.isNotEmpty()) {
            slots.forEach { slot ->
                ScheduleItemView(slot = slot, onItemClick = { deleteSlot ->
                    isShowDialog = true
                    slotToDelete = deleteSlot
                })
            }

            SpacerVertical(size = 16.dp)
        }

        //Add Button
        val btnText = if (slots.isNotEmpty()) "Add Another" else "Add Scheduled Time(s)"
        ButtonColoredIcon(title = btnText,
            icon = R.drawable.ic_add,
            btnColor = AppGreen,
            onBtnClick = { isShowDateTimePicker = true })

        if (isNoSlots) {
            if (goLiveData.scheduleSlots.isEmpty()) {
                SpacerVertical(size = 8.dp)
                Text700_14sp(
                    step = "Please set at least 1 schedule",
                    isBold = false,
                    color = ErrorRed
                )
            }
        }
    }

    //add more bottom spaces
    SpacerVertical(size = 100.dp)
}

@Composable
private fun ShowError(message: String) {
    Text400_14sp(info = message, color = ErrorRed)
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
private fun SwipeSwitchWhenGo(isNowSelected: Boolean, onSwipeChange: (Boolean) -> Unit) {
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
            .height(88.dp)
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
            TextIconSwipeSelection(
                modifier = Modifier
                    .weight(1f)
                    .clickable { /*onSwipeChange(true)*/ },
                title = "Go Live Now",
                icon = R.drawable.livecast_color,
                textColor = if (isNowSelected) TextDark else AppWhite
            )

            TextIconSwipeSelection(
                modifier = Modifier
                    .weight(1f)
                    .clickable { /*onSwipeChange(false)*/ },
                title = "Schedule for Later",
                icon = if (!isNowSelected) R.drawable.schedule_on else R.drawable.schedule_off,
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
    stepCount: Int,
    isNowSelected: Boolean,
    onClickedNext: () -> Unit,
    onClickedBack: () -> Unit,
    onClickedLive: () -> Unit,
    onClickedFinalise: () -> Unit
) {
    val beforeLastStepId = stepCount - 1

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brushBottomGradientBg, shape = RoundedCornerShape(
                    topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp
                )
            )
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            val isEnableBack = currentStepId > 0

            //Back Button
            if (isEnableBack) {
                ButtonColouredWrap(
                    text = stringResource(R.string.back),
                    color = Color.Transparent,
                    isBackButton = true,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .height(48.dp)
                ) {
                    onClickedBack.invoke()
                }
            }

            //Next Button
            val widthAnimate by animateDpAsState(
                targetValue = if (isEnableBack) 170.dp else 570.dp,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                label = "animateNextBtn"
            )

            val rightAlign = if (isEnableBack) {
                Alignment.CenterEnd
            } else {
                Alignment.Center
            }

            val rightModify = if (isEnableBack) {
                if (currentStepId == beforeLastStepId) {
                    Modifier.height(48.dp)
                } else {
                    Modifier
                        .height(48.dp)
                        .width(widthAnimate)
                }
            } else {
                Modifier
                    .height(48.dp)
                    .fillMaxWidth()
            }

            Column(modifier = Modifier.align(rightAlign)) {
                if (currentStepId == beforeLastStepId) {
                    if (isNowSelected) {
                        //LivePreview Button
                        ButtonGradientIcon(text = "Preview Live",
                            icon = R.drawable.livecast_color,
                            gradientBrush = brushGradientLive,
                            modifier = rightModify,
                            onBtnClick = {
                                onClickedLive.invoke()
                            })
                    } else {
                        //FinaliseSchedule Button
                        ButtonColouredWrap(
                            text = "Finalise Schedule",
                            color = AppGreen,
                            modifier = rightModify,
                            onBtnClick = {
                                onClickedFinalise.invoke()
                            }
                        )
                    }
                } else {
                    //Next Button
                    ButtonColouredWrap(
                        text = stringResource(R.string.next),
                        color = AppGreen,
                        modifier = rightModify,
                        onBtnClick = {
                            onClickedNext.invoke()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SocialMediaListing(
    stateSMConnected: MutableList<SocialMediaInfo>,
    onSMItemClicked: (String) -> Unit,
    onSMItemChecked: (SocialMediaInfo) -> Unit,
    onUpdateInitialState: (smList: List<SocialMediaInfo>) -> Unit
) {

    val savedYT = stateSMConnected.find { it.name == SocialMediaInfo.YOUTUBE.name }
    val savedFB = stateSMConnected.find { it.name == SocialMediaInfo.FACEBOOK.name }
    val savedLI = stateSMConnected.find { it.name == SocialMediaInfo.LINKEDIN.name }

    val visibleSMList = SocialMediaInfo.entries.filter {
        it.name != SocialMediaInfo.NONE.name
    }

    val smList by rememberSaveable { mutableStateOf(visibleSMList) }

    smList.first { it.name == SocialMediaInfo.YOUTUBE.name }.apply {
        isConnect = savedYT?.isConnect ?: false
        isItemChecked = savedYT?.isItemChecked ?: false
        accessToken = savedYT?.accessToken
    }

    smList.first { it.name == SocialMediaInfo.FACEBOOK.name }.apply {
        isConnect = savedFB?.isConnect ?: false
        isItemChecked = savedFB?.isItemChecked ?: false
        accessToken = savedFB?.accessToken
    }

    smList.first { it.name == SocialMediaInfo.LINKEDIN.name }.apply {
        isConnect = savedLI?.isConnect ?: false
        isItemChecked = savedLI?.isItemChecked ?: false
        accessToken = savedLI?.accessToken
    }

    LaunchedEffect(onUpdateInitialState) {
        onUpdateInitialState(smList)
    }

    smList.forEach { item ->
        SocialMediaItemContent(item, onSMItemClicked = {
            onSMItemClicked.invoke(it)
        }, onSMItemChecked = { smInfo ->
            onSMItemChecked(smInfo)
        })
    }
}

@Composable
private fun AgentListing(
    agencyList: List<AgencyItem>, onAgentItemClicked: (AgencyItem) -> Unit
) {
    agencyList.forEach { agentItem ->
        AgencyItemContent(agentItem, onItemClicked = { selected ->
            onAgentItemClicked.invoke(selected)
        })
    }
}

@Composable
private fun PropertyListing(
    listings: List<PropertyItem>?, onItemClicked: (PropertyItem) -> Unit = {}
) {
    listings?.forEach { item ->
        PropertyItemContent(item, onItemClicked = { selected ->
            onItemClicked.invoke(selected)
        })
    }
}

@Composable
private fun ProfileHideItem(
    isHideProfile: Boolean, onCheckedChange: (Boolean) -> Unit = {}
) {

    val cardBgColor = if (isHideProfile) AppWhite else ItemCardBg
    val textColor = if (isHideProfile) ItemCardBg else AppWhite

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onCheckedChange(!isHideProfile) },
        shape = MaterialTheme.shapes.large,

        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text700_14spBold(step = "Do not show profile", txtColor = textColor)
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                //CheckBox BG
                Image(
                    painter = if (isHideProfile) {
                        painterResource(id = R.drawable.check_box_chcked)
                    } else {
                        painterResource(id = R.drawable.cehck_box_uncheck)
                    }, contentDescription = null, contentScale = ContentScale.FillBounds
                )

                //Tick Icon
                if (isHideProfile) {
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
    onSMItemClicked: (String) -> Unit,
    onSMItemChecked: (SocialMediaInfo) -> Unit
) {
    var isChecked by remember { mutableStateOf(info.isItemChecked) }

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
                SwitchCompo(isChecked, modifier = Modifier.height(35.dp), onCheckedChange = {
                    isChecked = it
                    onSMItemChecked.invoke(info.apply {
                        isItemChecked = isChecked
                    })
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
                    onItemClicked(item.apply { isChecked = !isChecked })
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
    profile: AgentProfileGoLive, isChecked: Boolean, textColor: Color
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
                modifier = Modifier.padding(horizontal = 8.dp), contentAlignment = Alignment.Center
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
fun SwitchCompo(
    isChecked: Boolean = false, modifier: Modifier = Modifier, onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = modifier
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

@Preview
@Composable
private fun PreviewGoLiveScreen() {
    Box(modifier = Modifier.background(brushMainGradientBg)) {
        val countSteps = 5
        val currentStep = 4

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
                    isNoSlots = false,
                    isNotShowProfile = true,
                    mGoLiveSubmit = GoLiveSubmit(),
                    mFieldsError = mutableMapOf(),
                    stateSMConnected = mutableListOf(),
                    onSwipeIsNowSelected = { },
                    onNotShowProfileChange = {},
                    onPropertyItemClicked = {},
                    onAgentItemClicked = {},
                    onSMItemClicked = {})
            }
        }

        NextBackButtons(modifier = Modifier.align(Alignment.BottomCenter),
            currentStepId = currentStep,
            stepCount = countSteps,
            isNowSelected = true,
            onClickedNext = {},
            onClickedBack = {},
            onClickedFinalise = {},
            onClickedLive = {})
    }
}
