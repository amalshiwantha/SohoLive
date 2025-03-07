package com.soho.sohoapp.live.ui.view.screens.video_recorder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.SohoLiveApp.Companion.getActivity
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.enums.LiveFormat
import com.soho.sohoapp.live.enums.Orientation
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.MainStateHolder
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.LiveRequest
import com.soho.sohoapp.live.network.response.LiveTarget
import com.soho.sohoapp.live.ui.components.LoadingDialog
import com.soho.sohoapp.live.ui.components.NotEnableStreamAlert
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text800_14sp
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.BgGradientPurpleDark
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.view.activity.main.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveEvent
import com.soho.sohoapp.live.ui.view.screens.golive.GoLiveViewModel
import com.soho.sohoapp.live.ui.view.screens.golive.RequestNotificationPermission
import com.soho.sohoapp.live.ui.view.screens.golive.ShowAlert
import com.soho.sohoapp.live.ui.view.screens.golive.getAlertConfig
import com.soho.sohoapp.live.ui.view.screens.golive.openWebView
import com.soho.sohoapp.live.ui.view.screens.player.AgentPropertyInfo
import com.soho.sohoapp.live.utility.Const.Companion.YT_ENABLE
import com.soho.sohoapp.live.utility.Const.Companion.YT_VERIFY
import com.soho.sohoapp.live.utility.rotateScreen
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject


@Composable
fun TemplateScreen(
    navController: NavHostController,
    goLiveData: GoLiveSubmit,
    goLiveVm: GoLiveViewModel = koinInject(),
    viewMMain: MainViewModel,
    onStartRecClick: () -> Unit
) {
    val cont = LocalContext.current
    val activity = LocalContext.current as ComponentActivity
    val stateVm = goLiveVm.liveState.value
    val isCompletedMinRecTime by remember { mutableStateOf(false) }
    val isRecording by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    var hasMicPermission by remember { mutableStateOf(false) }
    var shouldShowSettingsButton by remember { mutableStateOf(false) }
    val rotateScreen by remember { mutableStateOf(MainStateHolder.mState.liveOrientation.value) }
    var isRotateLandScreen by remember { mutableStateOf(false) }
    var isTemplateWithBrand by remember { mutableStateOf(MainStateHolder.mState.isTemplateWithBrand.value) }
    val alertState = remember { mutableStateOf(Pair(false, null as AlertConfig?)) }

    /*
    * show stream not enabled view
    * */
    if (stateVm.isStreamNotEnabled.value) {
        NotEnableStreamAlert(onDismiss = {
            stateVm.isStreamNotEnabled.value = false
        }, onEnableClick = {
            stateVm.isStreamNotEnabled.value = false
            openWebView(YT_ENABLE)
        }, onVerifyClick = {
            stateVm.isStreamNotEnabled.value = false
            openWebView(YT_VERIFY)
        })
    }

    /*
    * if goLiveApi got success response then want
    * to open the LiveCast Screen
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
            viewMMain.openLiveCastScreen(jsonStr)
            navController.popBackStack()
        }
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

    // Handle back press
    BackHandler(enabled = true) {
        backClose(navController, activity)
    }

    //Rotate Screen
    LaunchedEffect(rotateScreen) {
        if (rotateScreen == Orientation.LAND.name) {
            isRotateLandScreen = true
        }
    }

    if (isRotateLandScreen) {
        rotateScreen(rotateScreen, activity)
        isRotateLandScreen = false
    }

    // Launcher for requesting multiple permissions
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            hasCameraPermission = permissions[Manifest.permission.CAMERA] ?: false
            hasMicPermission = permissions[Manifest.permission.RECORD_AUDIO] ?: false

            // Check if permissions are denied permanently
            cont.getActivity()?.let {
                if (!hasCameraPermission || !ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        Manifest.permission.CAMERA
                    )
                ) {
                    shouldShowSettingsButton = true
                }

                if (!hasCameraPermission || !ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        Manifest.permission.RECORD_AUDIO
                    )
                ) {
                    shouldShowSettingsButton = true
                }
            }
        }
    )

    // Check initial permissions
    RequestNotificationPermission()

    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        hasMicPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        // Request permissions if any are not granted
        if (!hasCameraPermission || !hasMicPermission) {
            permissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
            )
        }
    }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.VIDEO_CAPTURE
            )
        }
    }

    //Content permission view and Camera
    if (hasCameraPermission && hasMicPermission) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgGradientPurpleDark)
        ) {
            //Main Content
            if (MainStateHolder.mState.liveOrientation.value == Orientation.LAND.name) {
                LandscapeView(controller,
                    goLiveData,
                    isTemplateWithBrand,
                    isCompletedMinRecTime,
                    isRecording,
                    onStartRecClick = {
                        navigate(goLiveData, onPreRecording = {
                            onStartRecClick()
                        }, onLiveCast = {
                            goLiveVm.onTriggerEvent(GoLiveEvent.CallSubmitGoLive(goLiveData))
                        })
                    },
                    onSelection = {
                        isTemplateWithBrand = it
                        updateSelection(it)
                    },
                    onBackClick = {
                        backClose(navController, activity)
                    })
            } else {
                PortraitView(
                    controller,
                    goLiveData,
                    isTemplateWithBrand,
                    isCompletedMinRecTime,
                    isRecording,
                    onStartRecClick = {
                        navigate(goLiveData, onPreRecording = {
                            onStartRecClick()
                        }, onLiveCast = {
                            goLiveVm.onTriggerEvent(GoLiveEvent.CallSubmitGoLive(goLiveData))
                        })
                    },
                    onSelection = {
                        isTemplateWithBrand = it
                        updateSelection(it)
                    },
                    onBackClick = {
                        backClose(navController, activity)
                    })
            }

            //Center Loading Progress
            if (stateVm.loadingState == ProgressBarState.Loading) {
                LoadingDialog(stateVm.loadingMessage)
            }
        }
    } else {
        //permission view
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgGradientPurpleDark)
        ) {
            val mod = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp)
            PermissionView(
                mod,
                cont.getActivity(),
                permissionsLauncher,
                hasCameraPermission,
                hasMicPermission,
                shouldShowSettingsButton,
                onBackClick = {
                    backClose(navController, activity)
                }
            )
        }
    }
}

fun navigate(goLiveData: GoLiveSubmit, onPreRecording: () -> Unit, onLiveCast: () -> Unit) {
    if (MainStateHolder.mState.liveFormat.value == LiveFormat.LIVE.name) {
        onLiveCast()
    } else {
        onPreRecording()
    }
}

fun backClose(navController: NavHostController, activity: ComponentActivity) {
    if (MainStateHolder.mState.liveOrientation.value == Orientation.LAND.name) {
        rotateScreen(Orientation.PORT.name, activity)
    }
    navController.popBackStack()
}

fun updateSelection(it: Boolean) {
    MainStateHolder.mState.isTemplateWithBrand.value = it
}

@Composable
fun PortraitView(
    controller: LifecycleCameraController,
    goLiveData: GoLiveSubmit,
    isTemplateWithBrand: Boolean,
    isCompletedMinRecTime: Boolean,
    isRecording: Boolean,
    onStartRecClick: () -> Unit,
    onSelection: (Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val (cameraPreview, bottomTemplate) = createRefs()

        //CamPreview
        Box(modifier = Modifier
            .fillMaxSize()
            .constrainAs(cameraPreview) {
                top.linkTo(parent.top)
                bottom.linkTo(bottomTemplate.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            })
        {
            //Main Camera
            CameraPreview(
                controller = controller,
                camPadding = getDisplaySize(),
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )

            //back close button
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable { onBackClick() }
                    .padding(8.dp),
                painter = painterResource(id = R.drawable.circle_close),
                contentDescription = "close_button"
            )

            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            val screenHeight = configuration.screenHeightDp.dp

            // Calculate dynamic padding based on screen width and height
            val horizontalPadding = screenWidth * 0.20f
            val verticalPadding = screenHeight * 0.02f

            //Top Left Soho Watermark
            Image(
                painter = painterResource(id = R.drawable.soho_watermark),
                contentDescription = "watermark",
                modifier = Modifier.padding(horizontalPadding, verticalPadding, 0.dp, 0.dp)
            )

            //Timer Top Right
            TimerCard(
                timerValue = "PREVIEW",
                bgColor = HintGray,
                txtColor = AppWhite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(0.dp, verticalPadding, horizontalPadding, 0.dp)
            )

            //bottom agent info and property info
            val targetPaddingDp = screenWidth * (55f / 360f)

            goLiveData.agentProperty?.let {
                if (isTemplateWithBrand) {
                    val mod = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = targetPaddingDp, vertical = 0.dp)
                        .fillMaxWidth()
                    AgentPropertyInfo(
                        agProp = it, boxMod = mod,
                        isHideAgent = goLiveData.isHideAgent
                    )
                }
            }
        }

        //template selection and rec start button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomTemplate) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            ),
            colors = CardDefaults.cardColors(containerColor = BgGradientPurpleDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text800_14sp(label = stringResource(R.string.apply_agent_agency_branding))

                SpacerUp(size = 16.dp)

                //selections

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BrandingOption(
                        isSelected = isTemplateWithBrand,
                        label = "With Branding",
                        image = if (goLiveData.isHideAgent) R.drawable.template_with_brand_no_info else R.drawable.template_with_brand,
                        onSelectTemplate = {
                            onSelection(!isTemplateWithBrand)
                        }
                    )
                    BrandingOption(
                        isSelected = !isTemplateWithBrand,
                        label = "No Branding",
                        image = R.drawable.template_no_brand,
                        onSelectTemplate = {
                            onSelection(!isTemplateWithBrand)
                        }
                    )
                }

                SpacerUp(size = 16.dp)

                //bottom start and cam switch buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //Camera Switch
                    Image(
                        painter = painterResource(id = R.drawable.ic_cam_switch),
                        contentDescription = "Camera Switch",
                        modifier = Modifier.clickable {
                            if (!isRecording) {
                                controller.cameraSelector =
                                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    } else CameraSelector.DEFAULT_BACK_CAMERA
                            }
                        })

                    Spacer(modifier = Modifier.weight(1f))

                    //Stop & Rec Button
                    StartStopButtonTemp(isRecording, onBtnClick = {
                        onStartRecClick()
                    })
                }
            }
        }
    }
}

fun getDisplaySize(): Pair<Int, Int> {
    val cameraPadding = 0.15f //15%
    val displayMet = Resources.getSystem().displayMetrics
    val screenW = displayMet.widthPixels
    val screenH = displayMet.heightPixels
    val dynamicWPad = (screenW * cameraPadding).toInt()
    val dynamicHPad = (screenH * cameraPadding).toInt()

    return Pair(dynamicWPad, dynamicHPad)
}

@Composable
fun LandscapeView(
    controller: LifecycleCameraController,
    goLiveData: GoLiveSubmit,
    isTemplateWithBrand: Boolean,
    isCompletedMinRecTime: Boolean,
    isRecording: Boolean,
    onStartRecClick: () -> Unit,
    onSelection: (Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        //camera View
        Box(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxSize()
                .background(Color.Black)
        ) {
            //camera
            val showCameraPreview = remember { mutableStateOf(false) }
            LaunchedEffect("cam_preview") {
                delay(500)
                showCameraPreview.value = true
            }

            Box {
                if (showCameraPreview.value) {
                    CameraPreview(
                        controller = controller,
                        camPadding = getDisplaySize(),
                        isLandscape = true,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                }
            }

            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            val screenHeight = configuration.screenHeightDp.dp

            // Calculate dynamic padding based on screen width and height
            val horizontalPadding = screenWidth * 0.02f
            val verticalPadding = screenHeight * 0.20f

            //sohoLogo
            Image(
                painter = painterResource(id = R.drawable.soho_watermark),
                contentDescription = "watermark",
                modifier = Modifier.padding(horizontalPadding * 4f, verticalPadding, 0.dp, 0.dp)
            )

            //Timer
            TimerCard(
                timerValue = "PREVIEW",
                bgColor = HintGray,
                txtColor = AppWhite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(0.dp, verticalPadding, horizontalPadding, 0.dp)
            )

            //bottom agent info and property info
            val targetPaddingDp = screenHeight * (55f / 360f)
            goLiveData.agentProperty?.let {
                if (isTemplateWithBrand) {
                    val mod = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(
                            start = horizontalPadding * 3.3f,
                            end = 0.dp,
                            top = targetPaddingDp,
                            bottom = targetPaddingDp
                        )
                    AgentPropertyInfo(
                        agProp = it,
                        boxMod = mod,
                        isHideAgent = goLiveData.isHideAgent
                    )
                }
            }
        }

        // selection view
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
                .background(BgGradientPurpleDark)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text800_14sp(
                        label = stringResource(R.string.apply_agent_agency_branding),
                        modifier = Modifier.weight(1f)
                    )

                    Image(
                        modifier = Modifier.clickable { onBackClick() },
                        painter = painterResource(id = R.drawable.circle_close),
                        contentDescription = "close_button"
                    )

                }
                SpacerUp(size = 8.dp)

                //selections
                Column(modifier = Modifier.fillMaxWidth()) {
                    BrandingOptionLand(
                        isSelected = isTemplateWithBrand,
                        label = "With Branding",
                        image = if (goLiveData.isHideAgent) R.drawable.template_with_brand_no_info_land else R.drawable.template_with_brand_land,
                        onSelectTemplate = {
                            onSelection(!isTemplateWithBrand)
                        }
                    )

                    SpacerUp(size = 8.dp)

                    BrandingOptionLand(
                        isSelected = !isTemplateWithBrand,
                        label = "No Branding",
                        image = R.drawable.template_without_brand_land,
                        onSelectTemplate = {
                            onSelection(!isTemplateWithBrand)
                        }
                    )
                }
            }

            //bottom start and cam switch buttons
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Camera Switch
                Image(
                    painter = painterResource(id = R.drawable.ic_cam_switch),
                    contentDescription = "Camera Switch",
                    modifier = Modifier.clickable {
                        if (!isRecording) {
                            controller.cameraSelector =
                                if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else CameraSelector.DEFAULT_BACK_CAMERA
                        }
                    })

                Spacer(modifier = Modifier.weight(1f))

                //Stop & Rec Button
                StartStopButtonTemp(isRecording, onBtnClick = {
                    onStartRecClick()
                })
            }
        }
    }
}

@Composable
private fun PermissionView(
    mod: Modifier,
    activity: ComponentActivity?,
    permissionsLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    hasCameraPermission: Boolean,
    hasMicPermission: Boolean,
    shouldShowSettingsButton: Boolean,
    onBackClick: () -> Unit
) {
    val buttonText =
        if (shouldShowSettingsButton) "Go to Settings" else "Request Permissions"

    Column(
        modifier = mod,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // Centers content horizontally
    ) {
        Text700_14sp(
            step = "Camera and Microphone permissions are required to record video.",
            color = AppWhite,
            isCenter = true,
            isBold = false
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (shouldShowSettingsButton) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                activity?.startActivity(intent)
                onBackClick()
            } else {
                if (!hasCameraPermission || !hasMicPermission) {
                    permissionsLauncher.launch(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO
                        )
                    )
                }
            }
        }) {
            Text(buttonText)
        }
    }
}
