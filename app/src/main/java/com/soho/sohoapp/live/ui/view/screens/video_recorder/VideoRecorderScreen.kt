package com.soho.sohoapp.live.ui.view.screens.video_recorder

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.StatFs
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhite
import com.soho.sohoapp.live.ui.components.TextWhite14Normal
import com.soho.sohoapp.live.ui.theme.AppRed
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

const val PvtRecFolder = "SohoPreRecord"
private var recording: Recording? = null

@Composable
fun VideoRecorderScreen(
    goLiveData: GoLiveSubmit,
    mGState: GlobalState,
    vmVidRec: VideoRecorderViewModel = koinInject(),
    onVideoSaved: (Uri) -> Unit
) {

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.VIDEO_CAPTURE
            )
        }
    }

    var timerValue by remember { mutableStateOf("00:00") }
    var isRecording by remember { mutableStateOf(false) }

    // Timer logic
    LaunchedEffect(isRecording) {
        while (isRecording) {
            delay(1000)
            timerValue = updateTimer(timerValue)
        }

        //reset timer
        if (!isRecording) {
            delay(1000)
            timerValue = "00:00"
        }
    }

    //Main Content
    Box(modifier = Modifier.fillMaxSize()) {
        //Main Camera
        CameraPreview(
            controller = controller,
            modifier = Modifier.fillMaxSize()
        )

        //Switch Camera View
        IconButton(
            onClick = {
                controller.cameraSelector =
                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else CameraSelector.DEFAULT_BACK_CAMERA
            },
            modifier = Modifier
                .offset(16.dp, 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Cameraswitch,
                contentDescription = "Switch camera"
            )
        }

        //Timer Top Right
        TimerCard(
            timerValue = timerValue,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        //Bottom Action Btn
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(32.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ButtonOutlineWhite(text = if (isRecording) "Stop" else "Start") {
                recordVideo(controller, onRecord = {
                    isRecording = it
                })
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun recordVideo(controller: LifecycleCameraController, onRecord: (Boolean) -> Unit) {
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
fun VideoRecorderScreenORI(
    goLiveData: GoLiveSubmit,
    mGState: GlobalState,
    vmVidRec: VideoRecorderViewModel = koinInject(),
    onVideoSaved: (Uri) -> Unit
) {
    val mState = vmVidRec.mState.value
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner

    //If save success then open player
    LaunchedEffect(mState.isSuccess) {
        if (mState.isSuccess) {
            mGState.apply {
                privateVideoId.value = mState.lastSavedId
            }

            val tempVidFile =
                "file:///storage/emulated/0/Movies/SohoPreRecord/SohoLive_20241026_161648.mp4"
            onVideoSaved(Uri.parse(tempVidFile))
            vmVidRec.reset()
        }
    }

    // Camera provider instance
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraProvider = cameraProviderFuture.get()

    val executor: ExecutorService = Executors.newSingleThreadExecutor()

    // PreviewView setup
    var previewView: androidx.camera.view.PreviewView? = null
    var videoCapture: VideoCapture<Recorder>? by remember { mutableStateOf(null) }

    // Preview UseCase
    val preview = Preview.Builder().build()

    // Camera selector (front or back)
    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    // Recorder for video capture with 720p resolution
    val recorder = Recorder.Builder()
        .setQualitySelector(QualitySelector.from(Quality.HD)) // 720p resolution
        .build()

    videoCapture = VideoCapture.withOutput(recorder)

    DisposableEffect(Unit) {
        // Bind the preview and video capture use cases to the lifecycle
        try {
            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                videoCapture
            )
        } catch (e: Exception) {
            println("myVidRec : Err $e")
        }
        onDispose { cameraProvider.unbindAll() }
    }

    // Layout for Camera Preview and Buttons
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Camera preview area using PreviewView wrapped in AndroidView
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                androidx.camera.view.PreviewView(ctx).apply {
                    previewView = this
                    preview.setSurfaceProvider(surfaceProvider)
                }
            }
        )

        // Video control buttons
        var recording: Recording? by remember { mutableStateOf(null) }
        var isRecording by remember { mutableStateOf(false) }
        var timerValue by remember { mutableStateOf("00:00") }
        var showAlert by remember { mutableStateOf(false) }
        var maxVideoTime by remember { mutableIntStateOf(0) }

        // Timer logic
        LaunchedEffect(isRecording) {
            var elapsedTime = 0

            while (isRecording && elapsedTime < maxVideoTime) {
                delay(1000)
                elapsedTime++
                timerValue = updateTimer(timerValue)

                // Check if the elapsed time exceeds the maximum allowed video time
                //stop before 5sec
                if (elapsedTime == maxVideoTime - 5) {
                    recording?.stop()
                    recording = null
                    isRecording = false
                    timerValue = "00:00"
                    println("myVidRec : Max recording time reached")
                    break
                }
            }

        }

        //top right button to all List
        Image(
            painter = painterResource(id = R.drawable.watermark_soho),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(vertical = 32.dp, horizontal = 16.dp)
        )

        //Timer Top Right
        TimerCard(
            timerValue = timerValue,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        // Show alert if storage is below 100MB
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = {
                    Text(text = "Insufficient Storage")
                },
                text = {
                    val maxRecTime = convertMinutesToHHMM(maxVideoTime)
                    Text(text = "You have less than 100MB of storage available. You can record max $maxRecTime under the HD resolution.")
                },
                confirmButton = {
                    Button(onClick = { showAlert = false }) {
                        Text("OK")
                    }
                }
            )
        }

        // Start & Stop recording button
        Button(
            onClick = {
                if (isRecording) {
                    // Stop recording
                    recording?.stop()
                    recording = null
                    isRecording = false
                    timerValue = "00:00"
                } else {

                    // Check storage before starting
                    maxVideoTime = calculateMaxVideoTime()

                    if (!isEnoughSpaceToRecord()) {
                        showAlert = true
                        return@Button
                    }

                    // Start recording
                    val videoFile = createVideoFile()
                    val outputOptions = FileOutputOptions.Builder(videoFile).build()

                    recording = videoCapture?.output
                        ?.prepareRecording(context, outputOptions)
                        ?.apply {
                            withAudioEnabled() // Enable audio
                        }
                        ?.start(executor) { recordEvent ->
                            when (recordEvent) {
                                is VideoRecordEvent.Start -> {
                                    println("myVidRec : Recording Started")
                                }

                                is VideoRecordEvent.Finalize -> {
                                    if (recordEvent.hasError()) {
                                        println("myVidRec : Recording Error")
                                        Handler(Looper.getMainLooper()).post {
                                            vmVidRec.saveVideoItem(
                                                goLiveData,
                                                Uri.fromFile(videoFile)
                                            )
                                        }
                                    } else {
                                        //Open Video Player screen with last recorded video
                                        val lastVidUri = Uri.fromFile(videoFile)
                                        println("myVidRec : Recording Saved: ${lastVidUri}")
                                        Handler(Looper.getMainLooper()).post {
                                            vmVidRec.saveVideoItem(
                                                goLiveData,
                                                Uri.fromFile(videoFile)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    isRecording = true
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
                .fillMaxWidth()
        ) {
            TextWhite14Normal(title = if (isRecording) "Stop" else "Start")
        }
    }
}

@Composable
fun TimerCard(timerValue: String, modifier: Modifier) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 32.dp),
        colors = CardDefaults.cardColors(containerColor = AppRed),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            TextWhite14Normal(title = timerValue)
        }
    }
}

// Function to update timer string
fun updateTimer(currentTimer: String): String {
    val parts = currentTimer.split(":").map { it.toInt() }
    var minutes = parts[0]
    var seconds = parts[1] + 1

    if (seconds >= 60) {
        seconds = 0
        minutes += 1
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