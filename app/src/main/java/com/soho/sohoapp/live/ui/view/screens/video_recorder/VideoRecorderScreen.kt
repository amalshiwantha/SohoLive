package com.soho.sohoapp.live.ui.view.screens.video_recorder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text700_12sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text800_10sp
import com.soho.sohoapp.live.ui.components.Text800_12sp
import com.soho.sohoapp.live.ui.components.Text800_14sp
import com.soho.sohoapp.live.ui.theme.AppRed
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.BgGradientPurpleDark
import com.soho.sohoapp.live.ui.view.screens.golive.RequestNotificationPermission
import com.soho.sohoapp.live.ui.view.screens.player.AgentPropertyInfo
import com.soho.sohoapp.live.utility.rotateScreen
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
    var isLoading by remember { mutableStateOf(true) }
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.VIDEO_CAPTURE
            )
        }
    }

    //Rotate Screen
    LaunchedEffect(rotateScreen) {
        if (rotateScreen == Orientation.LAND.name) {
            isRotateLandScreen = true
        }
    }

    if (isRotateLandScreen) {
        cont.getActivity()?.let {
            rotateScreen(rotateScreen, it)
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

    //Auto Start Recording
    LaunchedEffect("startRecording") {
        delay(2000)
        isLoading = false
        startStopRecord(controller, onRecord = {
            isRecording = it
        }, onDone = {
            recFile = it
            vmVidRec.saveVideoItem(
                goLiveData,
                it
            )
        })
    }

    //Content permission view and Camera
    if (hasCameraPermission && hasMicPermission) {
        //RecorderScreen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgGradientPurpleDark)
        )
        {
            val isLandScreen = rotateScreen == Orientation.LAND.name

            //Main Camera
            CameraPreview(
                controller = controller,
                isRecordMode = true,
                isLandscape = isLandScreen,
                modifier = Modifier.fillMaxSize()
            )

            //CircularProgress Center
            if (isLoading) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Center),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = BgGradientPurpleDark)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text800_12sp(
                            label = "Preparing Recorder. " +
                                    "Recording will start automatically.",
                            isBold = false,
                            txtAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(color = AppWhite)
                    }
                }
            }

            //Top Left Soho Watermark
            Image(
                painter = painterResource(id = R.drawable.soho_watermark),
                contentDescription = "watermark",
                modifier = Modifier.offset(16.dp, 72.dp)
            )

            //Timer Top Right
            TimerCardLive(
                timerValue = timerValue,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 72.dp)
            )

            //BOTTOM
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {

                //bottom agent info and property info
                goLiveData.agentProperty?.let {
                    if (MainStateHolder.mState.isTemplateWithBrand.value) {
                        AgentPropertyInfo(
                            agProp = it,
                            boxMod = Modifier.fillMaxWidth()
                        )
                    }
                }

                //Bottom Stop Button
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    ),
                    colors = CardDefaults.cardColors(containerColor = BgGradientPurpleDark)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        //Stop & Rec Button
                        StartStopButton(isRecording, onBtnClick = {
                            if (!isLoading) {
                                startStopRecord(controller, onRecord = {
                                    isRecording = it
                                }, onDone = {
                                    recFile = it
                                    vmVidRec.saveVideoItem(
                                        goLiveData,
                                        it
                                    )
                                })
                            }
                        })

                    }
                }
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
                    navController.popBackStack()
                }
            )
        }
    }
}

fun startStopRecord(
    controller: LifecycleCameraController,
    onRecord: (Boolean) -> Unit,
    onDone: (Uri) -> Unit
) {
    recordVideo(controller, onRecord = {
        onRecord(it)
    }, onDone = {
        onDone(it)
    })
}

@Composable
fun BrandingOption(isSelected: Boolean, label: String, image: Int, onSelectTemplate: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onSelectTemplate() }
    ) {
        Box(modifier = Modifier.size(width = 72.dp, height = 128.dp)) {
            //Brand
            Image(
                painter = painterResource(id = image),
                modifier = Modifier.fillMaxSize(),
                contentDescription = ""
            )

            //Tick & Border Selection
            if (isSelected) {
                Image(
                    painter = painterResource(id = R.drawable.brand_selection_border),
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.Center)
                )

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
fun BrandingOptionLand(
    isSelected: Boolean,
    label: String,
    image: Int,
    onSelectTemplate: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onSelectTemplate() }
            .fillMaxWidth()
    ) {
        Box(modifier = Modifier.size(width = 156.dp, height = 120.dp)) {
            //Brand
            Image(
                painter = painterResource(id = image),
                modifier = Modifier.fillMaxSize(),
                contentDescription = ""
            )

            //Tick Selection
            if (isSelected) {
                Image(
                    painter = painterResource(id = R.drawable.brand_selection_tick),
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        SpacerSide(size = 8.dp)
        val txtColor = if (isSelected) Color.White else Color(0xFF99979C)
        Text700_12sp(label = label, txtColor = txtColor)
    }
}

@Composable
fun StartStopButton(isStart: Boolean, onBtnClick: () -> Unit) {
    val btnTxt = "End"
    val btnColor = AppWhite
    val txtColor = AppRed
    val btnIcon = R.drawable.liv_cast_stop_red

    ButtonColoredIconWrap(
        title = btnTxt,
        btnColor = btnColor,
        txtColor = txtColor,
        icon = btnIcon
    ) {
        onBtnClick()
    }
}

@Composable
fun StartStopButtonTemp(isStart: Boolean, onBtnClick: () -> Unit) {
    val btnTxt = if (isStart) "End" else "Record Now"
    val btnColor = if (isStart) AppWhite else AppRed
    val txtColor = if (isStart) AppRed else AppWhite
    val btnIcon = if (isStart) R.drawable.liv_cast_stop_red else R.drawable.livecast

    ButtonColoredIconWrap(
        title = btnTxt,
        btnColor = btnColor,
        txtColor = txtColor,
        icon = btnIcon
    ) {
        onBtnClick()
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
fun TimerCardLive(
    timerValue: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.5f),
                        Color.White.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.padding(vertical = 7.dp, horizontal = 8.dp)) {
            Text800_14sp(label = timerValue, txtColor = Color.Black)
        }
    }
}

@Composable
fun TimerCard(
    timerValue: String,
    modifier: Modifier,
    bgColor: Color = AppRed,
    txtColor: Color = AppWhite
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.padding(vertical = 7.dp, horizontal = 8.dp)) {
            Text800_10sp(label = timerValue, txtColor = txtColor)
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