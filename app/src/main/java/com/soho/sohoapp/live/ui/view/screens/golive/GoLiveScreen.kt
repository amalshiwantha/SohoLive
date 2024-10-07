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
import com.soho.sohoapp.live.enums.CastEnd
import com.soho.sohoapp.live.enums.CategoryType
import com.soho.sohoapp.live.enums.CustomCoverOption
import com.soho.sohoapp.live.enums.FormFields
import com.soho.sohoapp.live.enums.PropertyState
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.enums.StepInfo
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.model.AgencyItem
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.model.GoLivePlatform
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.MainStateHolder
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
import com.soho.sohoapp.live.network.response.LiveRequest
import com.soho.sohoapp.live.network.response.LiveTarget
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
import com.soho.sohoapp.live.ui.components.InitialProfileImage
import com.soho.sohoapp.live.ui.components.SearchBar
import com.soho.sohoapp.live.ui.components.SelectOrientationBottomSheet
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text400_10sp
import com.soho.sohoapp.live.ui.components.Text400_12sp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text400_14spSingleLine
import com.soho.sohoapp.live.ui.components.Text700_12sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text700_14spBold
import com.soho.sohoapp.live.ui.components.Text700_14spProperty
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
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.AppWhiteGray
import com.soho.sohoapp.live.ui.theme.BorderGray
import com.soho.sohoapp.live.ui.theme.ErrorRed
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import com.soho.sohoapp.live.ui.theme.RentTxtColor
import com.soho.sohoapp.live.ui.theme.TextDark
import com.soho.sohoapp.live.ui.theme.lowGreen
import com.soho.sohoapp.live.ui.view.activity.main.MainActivity.Companion.maxSteps
import com.soho.sohoapp.live.ui.view.activity.main.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.schedule.DateTimePicker
import com.soho.sohoapp.live.ui.view.screens.schedule.ScheduleItemView
import com.soho.sohoapp.live.ui.view.screens.schedule.ShowDeleteAlert
import com.soho.sohoapp.live.ui.view.screens.video_manage.PrivacySettings
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import com.soho.sohoapp.live.utility.NetworkUtils
import com.soho.sohoapp.live.utility.toUppercaseFirst
import com.soho.sohoapp.live.utility.visibleValue
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
    mGState: GlobalState,
    goLiveVm: GoLiveViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject(),
    onLoadApiResults: (DataGoLive) -> Unit,
    onLoadTSResults: (TsPropertyResponse) -> Unit,
    onUpdateState: (GoLiveAssets) -> Unit
) {
    val mState = goLiveVm.mState
    val stateVm = goLiveVm.liveState.value
    val stateRecentSM = viewMMain.stateRecentLoggedSM
    val assetsState = savedState ?: goLiveVm.assetsState.value
    val stepCount = maxSteps
    var currentStepId by remember { mutableIntStateOf(mState.stepId.value) }
    val optionList = mutableListOf("Inspection", "Auction", "Other")
    var isNetConnected by remember { mutableStateOf(true) }
    var isNowSelected by remember { mutableStateOf(assetsState.isNowSelected.value) }
    var isNoSlots by rememberSaveable { mutableStateOf(false) }
    var isDontShowProfile by remember { mutableStateOf(mGoLiveSubmit.isHideAgent) }
    var isShowScheduleOkScreen by remember { mutableStateOf(false) }
    val stateSMConnected by goLiveVm.connectedProfileNames.collectAsStateWithLifecycle()
    val checkedSM = remember { mutableStateListOf<String>() }
    var mFieldsError by remember { mutableStateOf(mutableMapOf<FormFields, String>()) }
    val eventState =
        AppEventBus.events.collectAsState(initial = AppEvent.SMProfile(SocialMediaProfile()))
    val eventStateLiveEnd =
        AppEventBus.events.collectAsState(initial = AppEvent.LiveEndStatus(CastEnd.NONE))
    val alertState = remember { mutableStateOf(Pair(false, null as AlertConfig?)) }
    var recentLoggedSM by remember { mutableStateOf(mutableListOf<String>()) }
    var rSelPropItem by remember { mutableStateOf(PropertyItem(0, Document(), false)) }
    var isShowOrientationModel by remember { mutableStateOf(false) }

    /*
    * show select orientation view
    * */
    if (isShowOrientationModel) {
        SelectOrientationBottomSheet(onGoLive = {
            isShowOrientationModel = false

            callApi(mGoLiveSubmit,
                mFieldsError,
                netUtil,
                goLiveVm,
                onErrorsUpdate = {
                    mFieldsError = it
                })
        }, onCancel = {
            isShowOrientationModel = false
        })
    }

    /*
    * check liveCast how to end, if completed then clean all of savedData or else keepIt as
    * */
    LaunchedEffect(eventStateLiveEnd.value) {
        val str = eventStateLiveEnd.value.toString()

        if (str == "LiveEndStatus(castEnd=COMPLETE)") {
            //Reset steps flow
            resetSteps(mGoLiveSubmit, assetsState)

            //Reset orientation
            MainStateHolder.resetLive()

            //Open end screen
            navController.navigate(NavigationPath.LIVE_CAST_END.name)
        }
    }

    /*
    * get updated SM list
    * */
    LaunchedEffect(stateRecentSM) {
        stateRecentSM.collect { list ->
            recentLoggedSM = list
        }
    }

    /*
    * if goLiveApi got success response then want to open the LiveCast Screen
    * */
    LaunchedEffect(stateVm.goLiveResults) {
        stateVm.goLiveResults?.let {

            val platformList = it.simulcastTargets.map { target ->
                target.platform
            }

            val targetLive = LiveTarget()
            targetLive.apply {
                platform = platformList
            }

            val requestLive = LiveRequest(
                simulcastTargets = it.simulcastTargets,
                streamKey = it.streamKey,
                liveStreamId = it.id,
                shareableLink = it.shareableLink
            )
            val jsonStr = Json.encodeToString(requestLive)

            if (isNowSelected) {
                viewMMain.openLiveCastScreen(jsonStr)
            } else {
                isShowScheduleOkScreen = true
            }
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
            goLiveVm.updateAssetsState(savedTsResults, savedState)
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

    /*
    * Globally saved selected property item
    * */
    LaunchedEffect(rSelPropItem) {
        if (rSelPropItem.id != 0) {

            mGState.apply {
                propertyItemState.value = if (rSelPropItem.isChecked) rSelPropItem else null
            }

            //this is for reset rSelPropItem value to accept new save value
            rSelPropItem = PropertyItem(0, Document(), false)
        }
    }


    //MAIN CONTENT
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
                            StepContents(currentStepId = currentStepId,
                                mGState = mGState,
                                propertyList = propertyList,
                                mainAgencyList = agencyList,
                                optionList = optionList,
                                isNowSelected = isNowSelected,
                                isNoSlots = isNoSlots,
                                isNotShowProfile = isDontShowProfile,
                                mGoLiveSubmit = mGoLiveSubmit,
                                mFieldsError = mFieldsError,
                                stateSMConnected = stateSMConnected,
                                recentLoggedSM = recentLoggedSM,
                                onSwipeIsNowSelected = {
                                    isNowSelected = it
                                    assetsState.isNowSelected.value = it
                                },
                                onNotShowProfileChange = {
                                    mGoLiveSubmit.apply {
                                        agentId = null
                                        isHideAgent = it
                                    }
                                    isDontShowProfile = it

                                    /*
                                    * if checked don't Show then removed selections all of items
                                    * */
                                    if (it) {
                                        goLiveVm.unSelectAgentSelectionList()
                                    }
                                },
                                onPropertyItemClicked = { selectedProperty ->

                                    //Global state update for selected property
                                    rSelPropItem = selectedProperty

                                    //submit data update
                                    mGoLiveSubmit.apply {
                                        if (selectedProperty.isChecked) {
                                            propertyId = selectedProperty.propInfo.id?.toInt() ?: 0
                                            title = selectedProperty.propInfo.fullAddress()
                                            propertyType =
                                                selectedProperty.propInfo.getPropertyState()
                                        } else {
                                            propertyId = 0
                                            title = null
                                            propertyType = null
                                        }
                                    }

                                    //pre-load agent list from selection
                                    goLiveVm.updatePropertyList(selectedProperty)
                                },
                                onAgentItemClicked = { selectedAgent ->
                                    mGoLiveSubmit.apply {
                                        agentId = if (selectedAgent.isChecked) {
                                            isHideAgent = false
                                            isDontShowProfile = false
                                            selectedAgent.agentProfile.id
                                        } else {
                                            null
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
                                            val updatedTargets: MutableList<GoLivePlatform> =
                                                mutableListOf()
                                            val liveTargets = mGoLiveSubmit.targets.toMutableList()
                                            val currentPT = mGoLiveSubmit.platformToken
                                            val platformName = selectedSM.name.lowercase()

                                            checkedSM.remove(platformName)
                                            currentPT.removeIf { it.platform == platformName }

                                            checkedSM.forEach { smName ->
                                                updatedTargets.add(liveTargets.first { it.platform == smName })
                                            }

                                            mGoLiveSubmit.apply {
                                                checkedPlatforms = checkedSM
                                                platformToken = currentPT
                                                targets = updatedTargets
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
                        val isAllowGo = isAllowGoNext(currentStepId = currentStepId,
                            mGoLiveSubmit = mGoLiveSubmit,
                            goLiveVm = goLiveVm,
                            onValidateRes = {
                                mFieldsError = it.errors
                            })

                        if (currentStepId < stepCount - 1 && isAllowGo) {
                            currentStepId++
                            mState.stepId.value = currentStepId
                        }
                    },
                    onClickedBack = {
                        currentStepId = (currentStepId - 1) % stepCount
                        mState.stepId.value = currentStepId
                    },
                    onClickedFinalise = {
                        isNoSlots = mGoLiveSubmit.scheduleSlots.isEmpty()

                        if (!isNoSlots) {
                            //add utc dateFormat for schedules
                            convertScheduleUtc(mGoLiveSubmit)

                            callApi(mGoLiveSubmit,
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
                        val isAllowGo = isAllowGoNext(currentStepId = currentStepId,
                            mGoLiveSubmit = mGoLiveSubmit,
                            goLiveVm = goLiveVm,
                            onValidateRes = {
                                mFieldsError = it.errors
                            })

                        if (isAllowGo) {
                            isShowOrientationModel = true
                        }
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
                context.getString(R.string.connection_lost), context.getString(R.string.no_net_msg)
            )
        )
    }
}

fun resetSteps(mGoLiveSubmit: GoLiveSubmit, assetsState: GoLiveAssets) {
    //unchecked all agents
    assetsState.agencyListState?.value = assetsState.agencyListState?.value?.map { item ->
        item.copy(isChecked = false)
    } ?: emptyList()

    //unchecked all properties
    assetsState.propertyListState?.value = assetsState.propertyListState?.value?.map { item ->
        item.copy(isChecked = false)
    } ?: emptyList()

    //set default step #4 form data and other
    mGoLiveSubmit.apply {
        purpose = null
        propertyId = 0
        title = null
        description = null
        agentId = null
        targets.clear()
        platformToken.clear()
        scheduleSlots.clear()
        errors.clear()
        checkedPlatforms.clear()
        isHideAgent = false
        isSohoPublic = true
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
            if (mGoLiveSubmit.isHideAgent) {
                mGoLiveSubmit.apply { agentId = null }
                true
            } else {
                if (mGoLiveSubmit.agentId == null) {
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

        2 -> {/*
            * recently added soho pub and priv. so its default public, so no need to check other SM checked or not
            * */
            true

            /*if (mGoLiveSubmit.platformToken.isNotEmpty()) {
                true
            } else {
                goLiveVm.showAlert(
                    getAlertConfig(
                        context.getString(R.string.selection_required),
                        context.getString(R.string.select_platform)
                    )
                )
                false
            }*/
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
            currentPlatformToken.removeIf { it.platform == platformName }
            currentPlatformToken.add(
                PlatformToken(
                    platform = platformName, accessToken = token.toString()
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
        SpacerUp(size = 8.dp)
        Text700_14sp(step = "Please check your internet connection and try again.")
        SpacerUp(size = 24.dp)
        ButtonColoured(text = "Retry", color = AppGreen, onBtnClick = {
            onRetryClick()
        })
    }
}

@Composable
fun TopContent(stepCount: Int, currentStepId: Int) {
    StepIndicator(totalSteps = stepCount, currentStep = currentStepId)
    SpacerUp(16.dp)
    StepCountTitleInfo(currentStepId)
}

@Composable
fun PropertyItemRow(
    item: PropertyItem, isSelected: Boolean, onSelect: (PropertyItem) -> Unit
) {
    val cardBgColor = if (isSelected) AppWhite else ItemCardBg
    val textColor = if (isSelected) ItemCardBg else AppWhite
    val property = item.propInfo

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onSelect(item) },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = cardBgColor)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
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

                TypeAndCheckBox(isSelected,
                    true,
                    property,
                    txtColor = textColor,
                    onCheckedChange = {
                        onSelect(item)
                    })

                SpacerUp(size = 8.dp)
                Text700_14spProperty(step = property.fullAddress(), color = textColor)

                /*SpacerUp(size = 8.dp)
                Text400_14sp(info = "3 scheduled livestream", color = textColor)*/

                SpacerUp(size = 10.dp)
                AmenitiesView(property, textColor)
            }
        }
    }

    /*Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onSelect(propertyItem) }
        .background(if (isSelected) Color.LightGray else Color.Transparent)
        .padding(16.dp)) {
        // Displaying Property Name
        Text700_14sp(step = propertyItem.propInfo.title ?: "", modifier = Modifier.weight(1f))
        Text400_10sp(label = propertyItem.propInfo.fullAddress())
    }*/
}


@Composable
fun StepContents(
    currentStepId: Int,
    mGState: GlobalState,
    propertyList: List<PropertyItem>? = null,
    mainAgencyList: List<AgencyItem>? = null,
    mGoLiveSubmit: GoLiveSubmit,
    isNowSelected: Boolean,
    isNoSlots: Boolean,
    isNotShowProfile: Boolean,
    optionList: MutableList<String>,
    mFieldsError: MutableMap<FormFields, String>,
    stateSMConnected: MutableList<SocialMediaInfo>,
    recentLoggedSM: MutableList<String>,
    onSwipeIsNowSelected: (Boolean) -> Unit,
    onNotShowProfileChange: (Boolean) -> Unit,
    onPropertyItemClicked: (PropertyItem) -> Unit,
    onAgentItemClicked: (AgencyItem) -> Unit,
    onSMItemClicked: (SocialMediaInfo) -> Unit
) {
    SpacerUp(24.dp)

    when (currentStepId) {
        // step #1
        0 -> {
            propertyList?.let { propList ->
                if (propList.isEmpty()) {
                    DisplayNoData(message = "Not Found Properties")
                } else {
                    SearchBar()
                    SpacerUp(16.dp)
                    PropertyListing(mGState, propList, onItemClicked = {
                        onPropertyItemClicked.invoke(it)
                    })
                    SpacerUp(size = 70.dp)
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
            SocialMediaListing(recentLoggedSM = recentLoggedSM,
                stateSMConnected = stateSMConnected,
                isSohoPublic = mGoLiveSubmit.isSohoPublic,
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
                onSohoItemChecked = {
                    mGoLiveSubmit.isSohoPublic = it
                    MainStateHolder.mState.isPublic.value = it
                })
            SpacerUp(size = 70.dp)
        }

        // step #4
        3 -> {
            Content4(
                optionList = optionList, mGoLiveSubmit = mGoLiveSubmit, mFieldsError = mFieldsError
            )
        }

        // step #5
        4 -> {
            Content5(goLiveData = mGoLiveSubmit,
                isNowSelected = isNowSelected,
                isNoSlots = isNoSlots,
                onSwipeIsNowSelected = { onSwipeIsNowSelected(it) })
        }
    }
}

@Composable
fun DisplayNoData(message: String) {
    SpacerUp(size = 150.dp)
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

    //save default value
    val propType = mGoLiveSubmit.propertyType
    val defaultSelection = getStateSelection(optionList, propType)
    mGoLiveSubmit.apply { purpose = defaultSelection }
    configPurpose.input = defaultSelection

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
    SpacerUp(size = 24.dp)
    Text700_14sp(step = "Stream title")
    TextFieldOutlined(tfConfig = configTitle, onTextChange = {
        mGoLiveSubmit.apply { title = it }
    })
    errTitle?.let {
        ShowError(message = it)
    }

    //description
    SpacerUp(size = 24.dp)
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
        SpacerUp(size = 40.dp)
        Text700_14sp(step = "Livestream cover image")
        Text400_14sp(info = "We’ve generated a cover image for your livestream. Cover image may be seen by viewers on connected social platforms and when you share your livestream link.")

        SpacerUp(size = 16.dp)
        Image(
            painter = painterResource(id = R.drawable.sample_cover_image),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )

        SpacerUp(size = 16.dp)
        ShareDownloadButtons()

        SpacerUp(size = 24.dp)
        CustomizeCoverImageCard(isOnCoverOption, onCheckedChange = {
            isOnCoverOption = it
        })

        if (isOnCoverOption) {
            SpacerUp(size = 16.dp)
            CustomizeCoverOptionCards()
        }
    }

    SpacerUp(size = 140.dp)
}

fun getStateSelection(optionList: MutableList<String>, propType: String?): String {
    return propType?.let {
        return when (it) {
            PropertyState.RENT.value -> optionList[0]
            PropertyState.SALE.value -> optionList[0]
            PropertyState.AUCTION.value -> optionList[1]
            else -> optionList.last()
        }
    } ?: run {
        optionList.last()
    }
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
        ShowDeleteAlert(isShowDialog = isShowDialog,
            slotToDelete = slotToDelete,
            onDismiss = { isShowDialog = it },
            onDelete = {
                slots.remove(it)
                goLiveData.apply {
                    this.scheduleSlots = slots
                }
            })
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
    SpacerUp(size = 8.dp)
    SwipeSwitchWhenGo(isNowSelected, onSwipeChange = {
        onSwipeIsNowSelected(it)
    })

    //this is for Schedule section content
    if (!isNowSelected) {
        SpacerUp(size = 40.dp)
        Text700_14sp(step = "Set date & time")

        SpacerUp(size = 8.dp)
        Text700_14sp(step = "You can schedule multiple livecast for this listing", isBold = false)

        SpacerUp(size = 16.dp)

        //Schedule List
        if (slots.isNotEmpty()) {
            slots.forEach { slot ->
                ScheduleItemView(slot = slot, onItemClick = { deleteSlot ->
                    isShowDialog = true
                    slotToDelete = deleteSlot
                })
            }

            SpacerUp(size = 16.dp)
        }

        //Add Button
        val btnText = if (slots.isNotEmpty()) "Add Another" else "Add Scheduled Time(s)"
        ButtonColoredIcon(title = btnText,
            icon = R.drawable.ic_add,
            btnColor = AppGreen,
            onBtnClick = { isShowDateTimePicker = true })

        if (isNoSlots) {
            if (goLiveData.scheduleSlots.isEmpty()) {
                SpacerUp(size = 8.dp)
                Text700_14sp(
                    step = "Please set at least 1 schedule", isBold = false, color = ErrorRed
                )
            }
        }
    }

    //add more bottom spaces
    SpacerUp(size = 100.dp)
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
                SpacerUp(size = 20.dp)
                UpgradedPlansText()

                SpacerUp(size = 16.dp)
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
        SpacerSide(size = 16.dp)
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
    inactiveColor: Color = AppWhite.copy(alpha = 0.2f),
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
                        ButtonGradientIcon(text = "Preview",
                            icon = R.drawable.livecast_color,
                            gradientBrush = brushGradientLive,
                            modifier = rightModify,
                            onBtnClick = {
                                onClickedLive.invoke()
                            })
                    } else {
                        //FinaliseSchedule Button
                        ButtonColouredWrap(text = "Finalise Schedule",
                            color = AppGreen,
                            modifier = rightModify,
                            onBtnClick = {
                                onClickedFinalise.invoke()
                            })
                    }
                } else {
                    //Next Button
                    ButtonColouredWrap(text = stringResource(R.string.next),
                        color = AppGreen,
                        modifier = rightModify,
                        onBtnClick = {
                            onClickedNext.invoke()
                        })
                }
            }
        }
    }
}

@Composable
private fun SocialMediaListing(
    recentLoggedSM: MutableList<String>,
    stateSMConnected: MutableList<SocialMediaInfo>,
    isSohoPublic: Boolean,
    onSMItemClicked: (String) -> Unit,
    onSMItemChecked: (SocialMediaInfo) -> Unit,
    onSohoItemChecked: (Boolean) -> Unit
) {

    /*this is for each SM checkBox*/
    var isCheckedYT by rememberSaveable { mutableStateOf(false) }
    var isCheckedFB by rememberSaveable { mutableStateOf(false) }
    var isCheckedLI by rememberSaveable { mutableStateOf(false) }

    /*check Sm connected or Not*/
    val savedYT = stateSMConnected.find { it.name == SocialMediaInfo.YOUTUBE.name }
    val savedFB = stateSMConnected.find { it.name == SocialMediaInfo.FACEBOOK.name }
    val savedLI = stateSMConnected.find { it.name == SocialMediaInfo.LINKEDIN.name }

    /*if SM recentLogged first piroty to checked state or else change state*/
    isCheckedYT = if (recentLoggedSM.contains(SocialMediaInfo.YOUTUBE.name)) true
    else savedYT?.isItemChecked ?: false
    isCheckedFB = if (recentLoggedSM.contains(SocialMediaInfo.FACEBOOK.name)) true
    else savedFB?.isItemChecked ?: false
    isCheckedLI = if (recentLoggedSM.contains(SocialMediaInfo.LINKEDIN.name)) true
    else savedLI?.isItemChecked ?: false

    /*
    * if want to hide SM add in to here
    * */
    val visibleSMList = SocialMediaInfo.entries.filter {
        it.name != SocialMediaInfo.NONE.name &&
                it.name != SocialMediaInfo.LINKEDIN.name
    }

    /*load all of SM list*/
    val smList by rememberSaveable { mutableStateOf(visibleSMList) }

    /*and add nessary info token and connected state, according to that need to show connect button*/
    smList.first { it.name == SocialMediaInfo.FACEBOOK.name }.apply {
        isConnect = savedFB?.isConnect ?: false
        isItemChecked = savedFB?.isItemChecked ?: false
        accessToken = savedFB?.accessToken
    }

    smList.first { it.name == SocialMediaInfo.YOUTUBE.name }.apply {
        isConnect = savedYT?.isConnect ?: false
        isItemChecked = savedYT?.isItemChecked ?: false
        accessToken = savedYT?.accessToken
    }

    /*smList.first { it.name == SocialMediaInfo.LINKEDIN.name }.apply {
        isConnect = savedLI?.isConnect ?: false
        isItemChecked = savedLI?.isItemChecked ?: false
        accessToken = savedLI?.accessToken
    }*/

    /*finally display SM list with checkBox or connect button*/
    smList.forEach { item ->
        SocialMediaItemContent(item,
            isSohoPublic,
            isCheckedYT,
            isCheckedFB,
            isCheckedLI,
            onSMItemClicked = {
                /*this is for open connect model*/
                onSMItemClicked.invoke(it)
            },
            onSohoItemChecked = {
                /*check and unCheck state update on toggle*/
                onSohoItemChecked(it)
            },
            onSMItemChecked = { smInfo ->
                /*check and unCheck state update on toggle*/

                /*first view need to show checkedState, so after that no need to check recentLogged state. so remove it*/
                recentLoggedSM.apply { recentLoggedSM.removeIf { it == SocialMediaInfo.YOUTUBE.name } }
                recentLoggedSM.apply { recentLoggedSM.removeIf { it == SocialMediaInfo.FACEBOOK.name } }
                recentLoggedSM.apply { recentLoggedSM.removeIf { it == SocialMediaInfo.LINKEDIN.name } }

                /*smInfo.isItemChecked state update*/
                when (smInfo.name) {
                    SocialMediaInfo.YOUTUBE.name -> {
                        isCheckedYT = smInfo.isItemChecked
                    }

                    SocialMediaInfo.FACEBOOK.name -> {
                        isCheckedFB = smInfo.isItemChecked
                    }

                    SocialMediaInfo.LINKEDIN.name -> {
                        isCheckedLI = smInfo.isItemChecked
                    }
                }

                onSMItemChecked(smInfo)
            })
    }

    SpacerUp(size = 40.dp)
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
    mGState: GlobalState,
    listings: List<PropertyItem>?,
    onItemClicked: (PropertyItem) -> Unit = {}
) {
    val savedProperty = mGState.propertyItemState.value
    var selectedProperty by remember { mutableStateOf(savedProperty) }

    listings?.forEach { propertyItem ->

        PropertyItemRow(item = propertyItem,
            isSelected = propertyItem == selectedProperty,
            onSelect = { selectedItem ->

                selectedProperty = if (selectedProperty == selectedItem) {
                    null
                } else {
                    selectedItem
                }

                // Update the isChecked state for selected item
                selectedItem.apply {
                    isChecked = selectedProperty != null
                }

                onItemClicked(selectedItem)
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
                modifier = Modifier.padding(horizontal = 8.dp), contentAlignment = Alignment.Center
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
    isSohoPublic: Boolean,
    isCheckedYT: Boolean,
    isCheckedFB: Boolean,
    isCheckedLI: Boolean,
    onSMItemClicked: (String) -> Unit,
    onSMItemChecked: (SocialMediaInfo) -> Unit,
    onSohoItemChecked: (Boolean) -> Unit
) {

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

                if (info == SocialMediaInfo.SOHO) {
                    SpacerUp(size = 16.dp)
                    PrivacySettings(isSohoPublic, isWhiteTheme = true, onChangePrivacy = {
                        val isPublic = it == VideoPrivacy.PUBLIC.label
                        onSohoItemChecked.invoke(isPublic)
                    })
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (info.isConnect) {
                //switch
                val isChecked = when (info.name) {
                    SocialMediaInfo.YOUTUBE.name -> {
                        isCheckedYT
                    }

                    SocialMediaInfo.FACEBOOK.name -> {
                        isCheckedFB
                    }

                    SocialMediaInfo.LINKEDIN.name -> {
                        isCheckedLI
                    }

                    else -> {
                        false
                    }
                }
                SwitchCompo(isChecked, modifier = Modifier.height(35.dp), onCheckedChange = {
                    onSMItemChecked.invoke(info.apply {
                        isItemChecked = it
                    })
                })
            } else {
                //button
                ButtonConnect(text = "Connect", color = info.btnColor) {
                    onSMItemClicked.invoke(info.name)
                }
            }
        }
    }
}

@Composable
fun getImageWidth(drawableResId: Int): Size {
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
            agent.imageUrl?.let {
                val urlPainter = rememberAsyncImagePainter(
                    model = it,
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
            } ?: kotlin.run {
                agent.name?.let { InitialProfileImage(it, 68.dp) }
            }

            //info
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .fillMaxWidth()
            ) {
                ProfileNameCheckBox(agent, item.isChecked, textColor)
                SpacerUp(size = 8.dp)
                MainStateHolder.mState.agentEmail?.let {
                    Text400_14spSingleLine(info = it, color = textColor)
                }
            }
        }
    }
}

@Composable
fun AmenitiesView(doc: Document, textColor: Color, isCompact: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        doc.bedroomCount.visibleValue()?.let {
            if (isCompact) {
                Text400_10sp(label = it, txtColor = textColor)
            } else {
                Text400_12sp(label = it, txtColor = textColor)
            }
            AmenitiesIcon(icon = R.drawable.ic_bedroom, iconColor = textColor, isCompact)
        }

        doc.bathroomCount.visibleValue()?.let {
            if (isCompact) {
                Text400_10sp(label = it, txtColor = textColor)
            } else {
                Text400_12sp(label = it, txtColor = textColor)
            }
            AmenitiesIcon(icon = R.drawable.ic_bathroom, iconColor = textColor, isCompact)
        }

        doc.carspotCount.visibleValue()?.let {
            if (isCompact) {
                Text400_10sp(label = it, txtColor = textColor)
            } else {
                Text400_12sp(label = it, txtColor = textColor)
            }
            AmenitiesIcon(icon = R.drawable.ic_car_park, iconColor = textColor, isCompact)
        }

        doc.areaSize()?.let {
            if (isCompact) {
                Text400_10sp(label = it.first, txtColor = textColor)
            } else {
                Text400_12sp(label = it.first, txtColor = textColor)
            }
            AmenitiesIcon(icon = it.second, iconColor = textColor, isCompact)
        }
    }
}

@Composable
private fun AmenitiesIcon(icon: Int, iconColor: Color = AppWhite, isCompact: Boolean) {
    val iconSize = if (isCompact) 12.dp else 14.dp
    Image(
        painter = painterResource(id = icon),
        contentDescription = null,
        modifier = Modifier
            .padding(start = 4.dp, end = 8.dp)
            .size(iconSize),
        contentScale = ContentScale.FillBounds,
        colorFilter = ColorFilter.tint(iconColor)
    )
}

@Composable
fun TypeAndCheckBox(
    isChecked: Boolean,
    isClickable: Boolean,
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
            doc.getPropertyState()?.toUppercaseFirst()?.let {
                Text700_12sp(label = it, txtColor = getTxtColorState(it))
            }
            val dotImage = if (isChecked) R.drawable.space_dot_dark else R.drawable.space_dot
            Image(
                painter = painterResource(id = dotImage),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 8.dp),
                contentScale = ContentScale.FillBounds
            )
            doc.propertyType?.toUppercaseFirst()?.let {
                Text400_12sp(label = it, txtColor = txtColor)
            }
        }

        if (isClickable) {
            Box(
                modifier = Modifier
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
}

fun getTxtColorState(value: String): Color {
    return when (value) {
        "For Sale" -> lowGreen
        "For Rent" -> RentTxtColor
        else -> lowGreen
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

            if (profile.currentStars != 0f && profile.currentStars != null) {
                Image(painter = painterResource(id = R.drawable.ic_star), contentDescription = "")
                SpacerSide(size = 4.dp)
                TextStarRating(rate = profile.currentStars.toString())
            }

            SpacerSide(size = 16.dp)

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
    SpacerUp(8.dp)
    Text950_20sp(title = stepInfo.title)
    SpacerUp(8.dp)
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
        val currentStep = 2

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            item {
                TopContent(countSteps, currentStep)
            }
            item {
                StepContents(currentStepId = currentStep,
                    optionList = mutableListOf(),
                    mGState = GlobalState(),
                    isNowSelected = true,
                    isNoSlots = false,
                    isNotShowProfile = true,
                    mGoLiveSubmit = GoLiveSubmit(),
                    mFieldsError = mutableMapOf(),
                    stateSMConnected = mutableListOf(),
                    recentLoggedSM = mutableListOf(),
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
