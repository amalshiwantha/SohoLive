package com.soho.sohoapp.live.ui.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.SurfaceHolder
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pedro.common.ConnectChecker
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.library.rtmp.RtmpCamera2
import com.pedro.library.util.FpsListener
import com.pedro.library.view.OpenGlView
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.databinding.ActivityHaishinBinding
import com.soho.sohoapp.live.enums.StreamResolution
import com.soho.sohoapp.live.utility.TimerTextHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HaishinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHaishinBinding
    private var watermark: ImageView? = null
    private var rtmpCamera2: RtmpCamera2? = null
    private var openGlView: OpenGlView? = null
    private val rtmpEndpoint = "rtmp://global-live.mux.com:5222/app/"
    private var streamKey: String = "387d87aa-0942-4f26-9ea5-75c9d8b06966"
    private val PERMISSION_REQUEST_CODE = 101
    private lateinit var timerTextHelper: TimerTextHelper

    private object StreamParameters {
        var resolution = StreamResolution.FULL_HD
        const val FPS = 30
        const val START_BITRATE = 400 * 1024
        const val INTERVAL_SEC = 5
        /*const val maxBitrate = 4000 * 1024
        const val backgroundStreamingTimeOutInMillis: Long = 60000*/
    }

    companion object {
        const val KEY_STREAM = "streamKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHaishinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.liveContent) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        checkRequiredPermissions()
    }

    private fun init() {
        openGlView = binding.openGlView
        watermark = binding.imgWatermark
        timerTextHelper = TimerTextHelper(binding.txtLiveTime)

        openGlView?.holder?.addCallback(surfaceHolderCallback)

        binding.cardGoLive.setOnClickListener {
            rtmpCamera2?.let {
                if (it.isStreaming) {
                    showCountDownOverlay(isStart = false)
                } else {
                    showCountDownOverlay()
                }
            }
        }

        binding.imgBtnCam.setOnClickListener {
            switchCamera()
        }

        binding.imgBtnMic.setOnClickListener {
            switchMute()
        }

        binding.imgBtnClose.setOnClickListener {
            finish()
        }

        createRtmpCamera2()
    }

    /*
    * isStart = true mean going to start liveCast if false
    * going to end and have to change the text as well as tick icon for end
    * */
    private fun showCountDownOverlay(isStart: Boolean = true) {
        binding.layoutCountdown.visibility = View.VISIBLE

        //show 3 to 1 countdown using countdownTimer
        val countdownTimer = object : CountDownTimer(3000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.txtCountdown.text = (millisUntilFinished / 1000 + 1).toString()
            }

            override fun onFinish() {
                if (isStart) {
                    binding.layoutCountdown.visibility = View.GONE
                    binding.txtCountdownMsg.text = getString(R.string.livecast_end)
                    goLiveNow()
                } else {
                    binding.txtCountdownMsg.text = getString(R.string.livecast_ended)
                    binding.txtCountdown.visibility = View.GONE
                    binding.imgDoneTick.visibility = View.VISIBLE
                    binding.imgBtnAbort.visibility = View.GONE
                    callLiveEndApi()
                }

                if (isStart) {
                    binding.layoutCountdown.visibility = View.GONE
                }
            }
        }
        countdownTimer.start()

        //abort button
        binding.imgBtnAbort.setOnClickListener {
            countdownTimer.cancel()
            binding.layoutCountdown.visibility = View.GONE
        }
    }

    private fun callLiveEndApi() {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                binding.layoutCountdown.visibility = View.GONE
                stopBroadcast()

                //reset
                binding.txtCountdownMsg.text = getString(R.string.livecast_start)
                binding.txtCountdown.visibility = View.VISIBLE
                binding.imgBtnAbort.visibility = View.VISIBLE
                binding.imgDoneTick.visibility = View.GONE
            }
        }
    }

    private fun goLiveNow() {
        updateGoLiveBtn(true)
        startBroadcast()
    }

    private fun updateGoLiveBtn(isStart: Boolean) {
        binding.txtGoLive.text = if (isStart) "Stop" else "Go Live"
        binding.imgGoLive.setImageResource(if (isStart) R.drawable.liv_cast_stop else R.drawable.livecast)
    }

    override fun onPause() {
        super.onPause()

        rtmpCamera2?.let {
            if (it.isStreaming) {
                stopBroadcast()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopBroadcast()
    }

    private fun switchCamera() {
        rtmpCamera2?.switchCamera()
    }

    private fun switchMute() {
        rtmpCamera2?.let {
            if (it.isAudioMuted) {
                it.enableAudio()
                binding.imgBtnMic.setImageResource(R.drawable.ic_mic)
            } else {
                it.disableAudio()
                binding.imgBtnMic.setImageResource(R.drawable.ic_mic_off)
            }
        }
    }

    //LIVE STREAMING
    private fun createRtmpCamera2() {
        openGlView?.let {
            if (rtmpCamera2 == null) {
                rtmpCamera2 = RtmpCamera2(it, connectCheckerRtmp).apply {
                    setFpsListener(fpsListenerCallback)
                    enableAutoFocus()
                }
            }
        }
    }

    private val fpsListenerCallback = FpsListener.Callback { fps ->
        println("myStream fps : $fps")
    }

    private fun startBroadcast() {
        if (arePermissionsGranted()) {
            if (streamKey.isNotEmpty()) {
                rtmpCamera2?.let {
                    if (!it.isStreaming) {
                        if (it.prepareAudio() && it.prepareVideo(
                                StreamParameters.resolution.width,
                                StreamParameters.resolution.height,
                                StreamParameters.FPS,
                                StreamParameters.START_BITRATE,
                                StreamParameters.INTERVAL_SEC,
                                CameraHelper.getCameraOrientation(application)
                            )
                        ) {
                            //showToast("Started Broadcast")
                            println("myStream startBroadcast")
                            it.startStream(rtmpEndpoint + streamKey)
                            showLiveTime(true)
                        } else {
                            //showToast("Broadcast Error")
                            println("myStream Error startBroadcast")
                        }
                    }
                }
            } else {
                showStreamKeyDialog()
            }
        } else {
            requestPermissions()
        }
    }

    private fun showLiveTime(isStarted: Boolean) {
        rtmpCamera2?.let {
            binding.imgBtnClose.visibility = if (isStarted) View.GONE else View.VISIBLE
            binding.txtLiveTime.visibility = if (isStarted) View.VISIBLE else View.GONE

            if (isStarted) {
                timerTextHelper.start()
            } else {
                binding.txtLiveTime.text = "Live 00:00"
                timerTextHelper.stop()
            }
        }
    }

    private fun stopBroadcast() {
        showLiveTime(false)

        rtmpCamera2?.let {
            if (it.isStreaming) {
                updateGoLiveBtn(false)
                println("myStream stopBroadcast")
                it.stopStream()
                //showToast("Stopped Broadcast")
            }
        }
    }

    private val connectCheckerRtmp = object : ConnectChecker {
        override fun onAuthError() {
            println("myStream onAuthError")
        }

        override fun onAuthSuccess() {
            println("myStream onAuthSuccess")
        }

        override fun onConnectionFailed(reason: String) {
            println("myStream onConnectionFailed $reason")
            stopBroadcast()
            finish()
        }

        override fun onConnectionStarted(url: String) {
            println("myStream onConnectionStarted $url")
        }

        override fun onConnectionSuccess() {
            println("myStream onConnectionSuccess")
        }

        override fun onDisconnect() {
            println("myStream onDisconnect")
            //showToast("Broadcast Disconnected. Add new Key")
        }

        override fun onNewBitrate(bitrate: Long) {
            println("myStream onNewBitrate $bitrate")
        }
    }

    private val surfaceHolderCallback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            rtmpCamera2?.startPreview()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            /*// Stop the preview if it's running
            rtmpCamera2?.stopPreview()

            // Re-constrain the layout a little if the rotation of the application has changed
            val rotation = windowManager.defaultDisplay.rotation
            val l = openGlView?.layoutParams as ConstraintLayout.LayoutParams
            when (rotation) {
                Surface.ROTATION_90, Surface.ROTATION_270 -> l.dimensionRatio = "w,16:9"
                else -> l.dimensionRatio = "h,9:16"
            }
            openGlView?.layoutParams = l

            // Re-start the preview, which will also reset the rotation of the preview
            rtmpCamera2?.startPreview(1920, 1080)*/
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            //viewModel.appInBackground()
        }
    }
    //LIVE STREAMING END

    //PERMISSION
    private fun checkRequiredPermissions() {
        if (arePermissionsGranted()) {
            showStreamKeyDialog()
        } else {
            requestPermissions()
        }
    }

    private fun arePermissionsGranted(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val audioPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        return cameraPermission == PackageManager.PERMISSION_GRANTED &&
                audioPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                showStreamKeyDialog()
            } else {
                showPermissionDeniedDialog()
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permissions Required")
            .setMessage("Camera and Audio permissions are required to start the live stream.")
            .setPositiveButton("Retry") { dialog, _ ->
                requestPermissions()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun showStreamKeyDialog() {
        streamKey = streamKey.ifEmpty { intent.getStringExtra(KEY_STREAM) ?: "" }

        if (streamKey.isEmpty()) {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Enter Mux Stream Key")

            val input = EditText(this)
            input.hint = "Find it form https://dashboard.mux.com/"
            alertDialogBuilder.setView(input)

            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                val key = input.text.toString().trim()
                if (key.isNotEmpty()) {
                    streamKey = key
                } else {
                    showStreamKeyDialog()
                }
            }

            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                finish()
            }

            alertDialogBuilder.setCancelable(false)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }
    //PERMISSION END
}
