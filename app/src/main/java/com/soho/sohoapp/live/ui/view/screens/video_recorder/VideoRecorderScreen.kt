package com.soho.sohoapp.live.ui.view.screens.video_recorder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.SohoLiveApp.Companion.getActivity
import com.soho.sohoapp.live.enums.Orientation
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.MainStateHolder
import com.soho.sohoapp.live.ui.components.ButtonColoredIconWrap
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text700_12sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text800_14sp
import com.soho.sohoapp.live.ui.components.TextWhite14Normal
import com.soho.sohoapp.live.ui.theme.AppRed
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.BgGradientPurpleDark
import com.soho.sohoapp.live.ui.theme.TextDark
import com.soho.sohoapp.live.utility.RotateScreen
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val PvtRecFolder = "SohoPreRecord"
private var recording: Recording? = null
private var recFile: Uri? = null

@Composable
fun VideoRecorderScreen(
    navController: NavHostController,
    goLiveData: GoLiveSubmit,
    mGState: GlobalState,
    vmVidRec: VideoRecorderViewModel = koinInject(),
    onVideoSaved: (Uri) -> Unit
) {
    val cont = LocalContext.current
    val mState = vmVidRec.mState.value
    var timerValue by remember { mutableStateOf("00:00") }
    var isCompletedMinRecTime by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    var hasMicPermission by remember { mutableStateOf(false) }
    var shouldShowSettingsButton by remember { mutableStateOf(false) }
    var rotateScreen by remember { mutableStateOf(MainStateHolder.mState.liveOrientation.value) }
    var isRotateLandScreen by remember { mutableStateOf(false) }

    //Rotate Screen
    LaunchedEffect(rotateScreen) {
        if (rotateScreen == Orientation.LAND.name) {
            isRotateLandScreen = true
        }
    }

    if (isRotateLandScreen) {
        cont.getActivity()?.let {
            RotateScreen(rotateScreen, it)
            isRotateLandScreen = false
        }
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

    //If save success then open player
    LaunchedEffect(mState.isSuccess) {
        if (mState.isSuccess) {

            //Reset Rotate
            isRotateLandScreen = rotateScreen == Orientation.LAND.name
            if (isRotateLandScreen) {
                rotateScreen = Orientation.PORT.name
            }

            mGState.apply {
                privateVideoId.value = mState.lastSavedId
            }
            recFile?.let { onVideoSaved(it) }

            vmVidRec.reset()
        }
    }

    // Timer logic
    LaunchedEffect(isRecording) {
        while (isRecording) {
            delay(1000)
            timerValue = updateTimer(timerValue, onMinRecTimeDone = {
                isCompletedMinRecTime = it
            })
        }

        //reset timer
        if (!isRecording) {
            delay(1000)
            timerValue = "00:00"
        }
    }

    //Template
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(BgGradientPurpleDark)
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
            if (hasCameraPermission && hasMicPermission) {
                CameraPreview(
                    controller = controller,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
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

            //Top Left Soho Watermark
            Image(
                painter = painterResource(id = R.drawable.soho_watermark),
                contentDescription = "watermark",
                modifier = Modifier.offset(16.dp, 16.dp)
            )

            //Timer Top Right
            TimerCard(
                timerValue = "PREVIEW",
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }

        //template selection
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

                Text800_14sp(label = "Apply agent & agency branding")

                SpacerUp(size = 16.dp)

                //selections
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BrandingOption(
                        isSelected = true,
                        label = "With Branding",
                        image = R.drawable.template_with_brand
                    )
                    BrandingOption(
                        isSelected = false,
                        label = "No Branding",
                        image = R.drawable.template_with_brand
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
                        recordVideo(controller, onRecord = {
                            isRecording = it
                        }, onDone = {
                            recFile = it
                            vmVidRec.saveVideoItem(
                                goLiveData,
                                it
                            )
                        })
                    })
                }
            }
        }
    }

    //RecorderScreen
    val isShowRecorder = false
    if (isShowRecorder) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(BgGradientPurpleDark)
        ) {
            val (cameraContent, bottomButton) = createRefs()

            //Camera Content
            Box(modifier = Modifier
                .fillMaxSize()
                .constrainAs(cameraContent) {
                    top.linkTo(parent.top)
                    bottom.linkTo(bottomButton.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                })
            {
                //Main Camera
                if (hasCameraPermission && hasMicPermission) {
                    CameraPreview(
                        controller = controller,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
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

                //Top Left Soho Watermark
                Image(
                    painter = painterResource(id = R.drawable.soho_watermark),
                    contentDescription = "watermark",
                    modifier = Modifier.offset(16.dp, 16.dp)
                )

                //Timer Top Right
                TimerCard(
                    timerValue = timerValue,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }

            //Bottom Buttons
            Row(modifier = Modifier
                .padding(16.dp)
                .constrainAs(bottomButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
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
                    recordVideo(controller, onRecord = {
                        isRecording = it
                    }, onDone = {
                        recFile = it
                        vmVidRec.saveVideoItem(
                            goLiveData,
                            it
                        )
                    })
                })
            }
        }
    }
}

@Composable
fun BrandingOption(isSelected: Boolean, label: String, image: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(width = 72.dp, height = 128.dp)) {
            //Brand
            Image(
                painter = painterResource(id = image),
                modifier = Modifier.fillMaxSize(),
                contentDescription = ""
            )

            //Tick Selection
            if(isSelected){
                Image(
                    painter = painterResource(id = R.drawable.brand_selection_tick),
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        SpacerUp(size = 8.dp)
        val txtColor = if (isSelected) Color.White else Color(0xFF99979C)
        Text700_12sp(label = label, txtColor = txtColor)
    }
}

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(
                context,
                "Notification not allowed. So upload progress will not show.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Check if permission is needed
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(permission)
            }
        }
    }
}

@Composable
fun StartStopButton(isStart: Boolean, isMinRecTimeDone: Boolean, onBtnClick: () -> Unit) {
    val btnTxt = if (isStart) "Stop" else "Record Now"
    val btnColor = if (isStart) AppWhite else AppRed
    val txtColor = if (isStart) AppRed else AppWhite
    val btnIcon = if (isStart) R.drawable.liv_cast_stop_red else R.drawable.livecast
    var isAllowClick = false

    //if start then check recTime has complete 10sec to stop
    if (isStart && isMinRecTimeDone) {
        isAllowClick = true
    } else if (!isStart) {
        isAllowClick = true
    }

    ButtonColoredIconWrap(
        title = btnTxt,
        btnColor = btnColor,
        txtColor = txtColor,
        icon = btnIcon
    ) {
        if (isAllowClick) {
            onBtnClick()
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
            color = TextDark,
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

@SuppressLint("MissingPermission")
private fun recordVideo(
    controller: LifecycleCameraController,
    onRecord: (Boolean) -> Unit,
    onDone: (Uri) -> Unit
) {

    if (recording != null) {
        onRecord(false)
        recording?.stop()
        recording = null
        return
    }

    val videoFile = createVideoFile()
    recording = controller.startRecording(
        FileOutputOptions.Builder(videoFile).build(),
        AudioConfig.create(true),
        ContextCompat.getMainExecutor(
            context
        )
    ) { event ->
        when (event) {
            is VideoRecordEvent.Finalize -> {
                if (!event.hasError()) {
                    println("myVidRec : Recording Saved: ${videoFile.toUri()}")
                    onRecord(false)
                    onDone(videoFile.toUri())
                } else {
                    println("myVidRec : Recording Error")
                    onRecord(false)
                    recording?.close()
                    recording = null
                }
            }

            is VideoRecordEvent.Start -> {
                println("myVidRec : Recording Start")
                onRecord(true)
            }
        }
    }

}

@Composable
fun TimerCard(timerValue: String, modifier: Modifier) {
    Card(
        modifier = modifier
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppRed),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            TextWhite14Normal(title = timerValue)
        }
    }
}

// Function to update timer string
fun updateTimer(currentTimer: String, onMinRecTimeDone: (Boolean) -> Unit): String {
    val parts = currentTimer.split(":").map { it.toInt() }
    var minutes = parts[0]
    var seconds = parts[1] + 1

    if (seconds >= 60) {
        seconds = 0
        minutes += 1
    }

    // Check if 10 seconds have passed
    if (minutes == 0 && seconds >= 10) {
        onMinRecTimeDone(true)
    } else if (minutes > 0 || seconds < 10) {
        onMinRecTimeDone(false)
    }

    return String.format("%02d:%02d", minutes, seconds)
}

// Function to create a video file in a custom directory "SohoPreRecording"
fun createVideoFile(): File {
    // Get the videos directory
    val movieDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
    //val extStorageDir = Environment.getExternalStorageDirectory()

    // Create a custom folder named "SohoPreRecord" directly in the root of external storage
    val customDir = File(movieDir, PvtRecFolder)

    // Create a folder named "SohoPreRecord" in the Videos directory
    //val customDir = File(videosDir, "SohoPreRecord")
    if (!customDir.exists()) {
        customDir.mkdirs() // Create the directory if it doesn't exist
    }

    // Generate a unique filename with the current timestamp
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "SohoLive_$timeStamp.mp4"

    // Return the file path
    return File(customDir, fileName)
}

// Check available storage before starting recording
fun isEnoughSpaceToRecord(): Boolean {
    val stat = StatFs(Environment.getExternalStorageDirectory().path)
    val availableBytes = stat.availableBytes
    val availableMB = availableBytes / (1024 * 1024) // Convert to MB

    /*
    * Estimate space required for 5 minutes of 720p video recording
    * 5 Mbps = 5 / 8 MBps = 0.625 MBps (bitrate to mbps -> per sec)
    * for 5min 0.625 MBps * 300 seconds = 187.5 MB
    * */
    val requiredSpaceFor5Min = 187.5

    return availableMB >= requiredSpaceFor5Min
}

// Calculate max video time based on available storage
fun calculateMaxVideoTime(): Int {
    val stat = StatFs(Environment.getExternalStorageDirectory().path)
    val availableBytes = stat.availableBytes
    val availableMB = availableBytes / (1024 * 1024)

    // Assume 720p video consumes approximately 5 MB per second
    return (availableMB / 5).toInt()
}

// Function to convert seconds to hh:mm format
fun convertMinutesToHHMM(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return "$hours hours and $remainingMinutes min"
}