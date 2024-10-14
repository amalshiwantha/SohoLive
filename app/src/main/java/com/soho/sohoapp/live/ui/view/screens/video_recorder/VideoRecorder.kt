package com.soho.sohoapp.live.ui.view.screens.video_recorder

import android.net.Uri
import android.os.Environment
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun VideoRecorder(
    navController: NavHostController,
    onVideoSaved: (Uri) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner

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

        // Timer logic
        LaunchedEffect(isRecording) {
            while (isRecording) {
                delay(1000) // Update every second
                timerValue = updateTimer(timerValue)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd) // Align to top right corner
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .background(Color.Red) // Red background
                .padding(8.dp) // Padding inside the box
        ) {
            Text(
                text = timerValue,
                color = Color.White,
                fontWeight = FontWeight.Bold
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
                                    } else {
                                        println("myVidRec : Recording Saved: ${videoFile.absolutePath}")
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
            // Toggle button text between Start and Stop based on recording state
            Text(if (isRecording) "Stop" else "Start")
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
    val customDir = File(movieDir, "SohoPreRecord")

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
