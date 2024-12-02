package com.soho.sohoapp.live.ui.view.screens.video_recorder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import com.soho.sohoapp.live.enums.Orientation
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.MainStateHolder
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text800_14sp
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.BgGradientPurpleDark
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.view.screens.player.AgentPropertyInfo
import com.soho.sohoapp.live.utility.rotateScreen
import org.koin.compose.koinInject


@Composable
fun TemplateScreen(
    navController: NavHostController,
    goLiveData: GoLiveSubmit,
    vmVidRec: VideoRecorderViewModel = koinInject(),
    onStartRecClick: () -> Unit
) {
    val cont = LocalContext.current
    val isCompletedMinRecTime by remember { mutableStateOf(false) }
    val isRecording by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    var hasMicPermission by remember { mutableStateOf(false) }
    var shouldShowSettingsButton by remember { mutableStateOf(false) }
    val rotateScreen by remember { mutableStateOf(MainStateHolder.mState.liveOrientation.value) }
    var isRotateLandScreen by remember { mutableStateOf(false) }
    var isTemplateWithBrand by remember { mutableStateOf(MainStateHolder.mState.isTemplateWithBrand.value) }
    val activity = LocalContext.current as ComponentActivity

    // Handle back press
    BackHandler(enabled = true) {
        if (MainStateHolder.mState.liveOrientation.value == Orientation.LAND.name) {
            rotateScreen(Orientation.PORT.name, activity)
        }
        navController.popBackStack()
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

        if (MainStateHolder.mState.liveOrientation.value == Orientation.LAND.name) {
            LandscapeView(controller,
                goLiveData,
                isTemplateWithBrand,
                isCompletedMinRecTime,
                isRecording,
                onStartRecClick = {
                    onStartRecClick()
                },
                onSelection = {
                    isTemplateWithBrand = it
                    updateSelection(it)
                },
                onBackClick = {
                    navController.popBackStack()
                })
        } else {
            PortraitView(
                controller,
                goLiveData,
                isTemplateWithBrand,
                isCompletedMinRecTime,
                isRecording,
                onStartRecClick = {
                    onStartRecClick()
                },
                onSelection = {
                    isTemplateWithBrand = it
                    updateSelection(it)
                })
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
                    navController.popBackStack()
                }
            )
        }
    }
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(64.dp)
                    .align(Alignment.Center)
            )

            //Top Left Soho Watermark
            Image(
                painter = painterResource(id = R.drawable.soho_watermark),
                contentDescription = "watermark",
                modifier = Modifier.padding(top = 32.dp, start = 32.dp)
            )

            //Timer Top Right
            TimerCard(
                timerValue = "PREVIEW",
                bgColor = HintGray,
                txtColor = AppWhite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 32.dp, end = 32.dp)
            )

            //bottom agent info and property info
            goLiveData.agentProperty?.let {
                if (isTemplateWithBrand) {
                    val mod = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                    AgentPropertyInfo(agProp = it, boxMod = mod)
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
                        image = R.drawable.template_with_brand,
                        onSelectTemplate = {
                            onSelection(!isTemplateWithBrand)
                        }
                    )
                    BrandingOption(
                        isSelected = !isTemplateWithBrand,
                        label = "No Branding",
                        image = R.drawable.template_with_brand,
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
                    StartStopButton(isRecording, isCompletedMinRecTime, onBtnClick = {
                        onStartRecClick()
                    })
                }
            }
        }
    }
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
        modifier = Modifier
            .fillMaxSize()
    ) {
        //camera View
        Box(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight()
                .background(Color.Black)
        ) {
            //camera
            CameraPreview(
                controller = controller,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )

            //sohoLogo
            Image(
                painter = painterResource(id = R.drawable.soho_watermark),
                contentDescription = "watermark",
                modifier = Modifier.padding(16.dp)
            )

            //Timer
            TimerCard(
                timerValue = "PREVIEW",
                bgColor = HintGray,
                txtColor = AppWhite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )

            //bottom agent info and property info
            goLiveData.agentProperty?.let {
                if (isTemplateWithBrand) {
                    val mod = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                    AgentPropertyInfo(agProp = it, boxMod = mod)
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
                SpacerUp(size = 16.dp)

                //selections
                Column(modifier = Modifier.fillMaxWidth()) {
                    BrandingOptionLand(
                        isSelected = isTemplateWithBrand,
                        label = "With Branding",
                        image = R.drawable.template_with_brand_land,
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
                StartStopButton(isRecording, isCompletedMinRecTime, onBtnClick = {
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
