package com.soho.sohoapp.live.ui.view.activity.live

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.pedro.common.ConnectChecker
import com.pedro.encoder.input.gl.render.filters.`object`.ImageObjectFilterRender
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.library.rtmp.RtmpCamera2
import com.pedro.library.util.FpsListener
import com.pedro.library.view.OpenGlView
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.databinding.ActivityLiveStreamBinding
import com.soho.sohoapp.live.enums.CastEnd
import com.soho.sohoapp.live.enums.Orientation
import com.soho.sohoapp.live.enums.SocialMedia
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.enums.StreamBitrate
import com.soho.sohoapp.live.enums.StreamResolution
import com.soho.sohoapp.live.model.AlertData
import com.soho.sohoapp.live.model.LiveCastStatus
import com.soho.sohoapp.live.network.response.LiveRequest
import com.soho.sohoapp.live.ui.components.ShareableLinkDialog
import com.soho.sohoapp.live.ui.view.activity.live.LiveStreamActivity.StreamParameters.resolution
import com.soho.sohoapp.live.utility.copyToClipboard
import com.soho.sohoapp.live.utility.showAlertMessage
import com.soho.sohoapp.live.utility.showProgressDialog
import com.soho.sohoapp.live.utility.showToastTrans
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel

class LiveStreamActivity : AppCompatActivity() {

    private var isLiveCastPaused: Boolean = false
    private val rtmpEndpoint = "rtmp://global-live.mux.com:5222/app/"
    private var streamKey: String = "9b5c8b83-950f-1b09-88ad-8a3dc76c4efd"

    private val viewModel: LiveStreamViewModel by viewModel()
    private lateinit var binding: ActivityLiveStreamBinding

    //private lateinit var timerTextHelper: TimerTextHelper
    private lateinit var reqLive: LiveRequest

    private var watermark: ImageView? = null
    private var rtmpCamera2: RtmpCamera2? = null
    private var openGlView: OpenGlView? = null
    private var progDialog: AlertDialog? = null
    private var countBitrateError = 0
    private var isDidLiveCast = false
    private val PERMISSION_REQUEST_CODE = 101
    private var isLand = false
    private var isPublic = false

    private var elapsedTime = 0L
    private var isRecording = false
    private var handler = Handler(Looper.getMainLooper())
    private var updateTimeRunnable: Runnable? = null

    private object StreamParameters {
        var resolution = StreamResolution.FULL_HD
        val START_BITRATE = getBitRate(resolution)
        const val FPS =
            30 // this FPS set according to the StreamBitrate. if change this then have to change the StreamBitrate also
        const val INTERVAL_SEC = 2

        private fun getBitRate(resolution: StreamResolution): Int {
            return when (resolution) {
                StreamResolution.FULL_HD -> StreamBitrate.FULL_HD.bitrate
                StreamResolution.HD -> StreamBitrate.HD.bitrate
                StreamResolution.SD -> StreamBitrate.SD.bitrate
            }
        }
    }

    companion object {
        const val KEY_PUBLIC = "is_public"
        const val KEY_ORIENTATION = "orientation"
        const val KEY_STREAM = "streamKey"
        const val KEY_LIVE_STATUS = "liveStatus"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeFullScreen()

        binding = ActivityLiveStreamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.liveContent) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkEssentialData()
        changeOrientation()
        init()
        composeView()
        smClickEvent()
        smVisibility()
        checkRequiredPermissions()
        mStateObserveable()
    }

    private fun timerStart() {
        if (!isRecording) {
            isRecording = true
            updateTimeRunnable?.let { handler.post(it) }
        }
    }

    /*private fun timerPause() {
        isRecording = false
        updateTimeRunnable?.let { handler.removeCallbacks(it) }
    }*/

    private fun timerStop() {
        isRecording = false
        elapsedTime = 0L
        updateTimerText()
        updateTimeRunnable?.let { handler.removeCallbacks(it) }
    }

    private fun updateTimerText() {
        val minutes = (elapsedTime / 60).toString().padStart(2, '0')
        val seconds = (elapsedTime % 60).toString().padStart(2, '0')
        binding.txtLiveTime.text = "Live $minutes:$seconds"

        //set enable share and live button after 10 sec start the liveCast
        val totalSeconds = minutes.toInt() * 60 + seconds.toInt()
        val isButtonEnabled = totalSeconds >= 10

        if (isButtonEnabled) {
            binding.imgBtnShare.isEnabled = true
            binding.cardGoLive.isEnabled = true
        }
    }

    private fun smVisibility() {

        val isHasFb = isHasShareLink(SocialMedia.FACEBOOK)
        val isHasYt = isHasShareLink(SocialMedia.YOUTUBE)
        val isHasLi = isHasShareLink(SocialMedia.LINKEDIN)

        binding.imgSmFb.visibility = if (isHasFb) View.VISIBLE else View.GONE
        binding.imgSmYt.visibility = if (isHasYt) View.VISIBLE else View.GONE
        binding.imgSmLi.visibility = if (isHasLi) View.VISIBLE else View.GONE
    }

    //same in Alert
    private fun getSmShareLink(smName: SocialMedia): String {
        return reqLive.simulcastTargets.first {
            it.platform == (smName.name.lowercase())
        }.shareableLink
    }

    //same in Alert
    private fun isHasShareLink(smName: SocialMedia): Boolean {
        return reqLive.simulcastTargets.filter {
            it.platform == (smName.name.lowercase())
        }.isNotEmpty()
    }

    private fun smClickEvent() {
        binding.imgSmSoho.setOnClickListener {
            val linkSoho = reqLive.shareableLink
            copyToClipboard("soho.com.au", linkSoho, true)
        }
        binding.imgSmFb.setOnClickListener {
            val linkFb = getSmShareLink(SocialMedia.FACEBOOK)
            copyToClipboard(SocialMediaInfo.FACEBOOK.title, linkFb, true)
        }
        binding.imgSmYt.setOnClickListener {
            val linkYt = getSmShareLink(SocialMedia.YOUTUBE)
            copyToClipboard(SocialMediaInfo.YOUTUBE.title, linkYt, true)
        }
        binding.imgSmLi.setOnClickListener {
            val linkLi = getSmShareLink(SocialMedia.LINKEDIN)
            copyToClipboard(SocialMediaInfo.LINKEDIN.title, linkLi, true)
        }
    }

    private fun composeView() {
        binding.composeView.setContent {

            val showDialog = remember { mutableStateOf(false) }
            val isGoLiveClick = remember { mutableStateOf(false) }

            binding.imgBtnShare.setOnClickListener {
                isGoLiveClick.value = false
                showDialog.value = true
            }

            binding.cardGoLive.setOnClickListener {
                rtmpCamera2?.let {
                    if (isLiveCastPaused) {
                        isLiveCastPaused = false
                        startBroadcast()
                        updateGoLiveBtn(true)
                    } else {
                        if (it.isStreaming) {
                            showCountDownOverlay(isStart = false)
                        } else {
                            isGoLiveClick.value = true
                            showDialog.value = true
                        }
                    }
                }
            }

            if (showDialog.value) {
                val btnTxt = binding.txtGoLive.text.toString()
                ShareableLinkDialog(
                    btnTxt = btnTxt,
                    reqLive = reqLive,
                    isShowLiveBtn = isGoLiveClick.value,
                    onClickCopy = {
                        when (it) {
                            SocialMedia.FACEBOOK.name -> {
                                val linkFb = getSmShareLink(SocialMedia.FACEBOOK)
                                copyToClipboard(SocialMediaInfo.FACEBOOK.title, linkFb)
                            }

                            SocialMedia.YOUTUBE.name -> {
                                val linkFb = getSmShareLink(SocialMedia.YOUTUBE)
                                copyToClipboard(SocialMediaInfo.YOUTUBE.title, linkFb)
                            }

                            SocialMedia.LINKEDIN.name -> {
                                val linkFb = getSmShareLink(SocialMedia.LINKEDIN)
                                copyToClipboard(SocialMediaInfo.LINKEDIN.title, linkFb)
                            }

                            else -> {
                                val linkSoho = reqLive.shareableLink
                                copyToClipboard("soho.com.au", linkSoho)
                            }
                        }
                    }, onClickLive = {
                        showDialog.value = false
                        showCountDownOverlay()
                    }, onDismiss = {
                        showDialog.value = false
                    })
            }

        }

        binding.composeView.visibility = View.VISIBLE
    }

    // Function to enable edge-to-edge mode and set full screen
    private fun enableEdgeToEdgeFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.post {
                window.decorView.windowInsetsController?.let { controller ->
                    // Hide system bars
                    controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    // Make sure that the system bars remain hidden
                    controller.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                }
            }
        } else {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }

    private fun changeOrientation() {
        val orientation = intent.getStringExtra(KEY_ORIENTATION)
        isLand = orientation == Orientation.LAND.name

        if (isLand) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    private fun checkEssentialData() {
        isPublic = intent.getBooleanExtra(KEY_PUBLIC, false)
        val jsonModel = intent.getStringExtra(KEY_STREAM)

        jsonModel?.let {
            reqLive = Json.decodeFromString<LiveRequest>(it)
        } ?: run {
            handleAlertState(
                AlertData(
                    isFinish = true,
                    isShow = true,
                    title = "Error",
                    message = "Required valid stream data to start live cast"
                )
            )
        }
    }

    // Observe state changes
    private fun mStateObserveable() {
        lifecycleScope.launch {
            viewModel.msLoading.collect { state ->
                handleLoadingState(state)
            }
        }

        lifecycleScope.launch {
            viewModel.msAlert.collect {
                handleAlertState(it)
            }
        }

        lifecycleScope.launch {
            viewModel.msEndCast.collect {
                if (it) {
                    finishSendStatus(CastEnd.COMPLETE)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.msRollBackCast.collect {
                if (it) {
                    finishSendStatus(if (isDidLiveCast) CastEnd.COMPLETE else CastEnd.CANCEL)
                }
            }
        }
    }

    private fun handleAlertState(alertState: AlertData) {
        if (alertState.isShow) {
            viewModel.resetStates()
            showAlertMessage(this, alertState, onClick = {
                if (alertState.isFinish) {
                    finish()
                }
            })
        }
    }

    private fun handleLoadingState(progState: Boolean) {
        when (progState) {
            true -> {
                progDialog = showProgressDialog(this, "Please wait")
            }

            false -> {
                showProgressDialog(
                    activity = this,
                    currentDialog = progDialog,
                    isVisible = false
                )
            }
        }
    }

    private fun init() {
        openGlView = binding.openGlView
        watermark = binding.imgWatermark
        //timerTextHelper = TimerTextHelper(binding.txtLiveTime, binding.imgBtnShare)

        updateTimeRunnable = object : Runnable {
            override fun run() {
                if (isRecording) {
                    elapsedTime += 1
                    updateTimerText()
                    handler.postDelayed(this, 1000)
                }
            }
        }

        binding.txtGoLive.text = if (isPublic) "Go Live" else "Record Now"

        openGlView?.holder?.addCallback(surfaceHolderCallback)

        binding.imgBtnCam.setOnClickListener {
            switchCamera()
        }

        binding.imgBtnMic.setOnClickListener {
            switchMute()
        }

        binding.imgBtnClose.setOnClickListener {
            callRollbackApi()
        }

        createRtmpCamera2()
    }

    //Watermark
    private fun getWatermarkLogoLandscape(): ImageObjectFilterRender {
        // Calculate scale relative to stream size
        val streamWidth = resolution.width
        val streamHeight = 8 // if change this image size will update

        val watermarkBitmap = BitmapFactory.decodeResource(resources, R.drawable.soho_logo_trans)
        val imgWidth = 25
        val imgHeight = 25

        val imgRender = ImageObjectFilterRender()
        imgRender.setAlpha(2.0f)//if increase this img color get more bright but not perfect
        imgRender.setImage(watermarkBitmap)

        // Calculate the scale
        val scaleFactor =
            minOf(streamWidth.toFloat() / imgWidth, streamHeight.toFloat() / imgHeight)
        imgRender.setScale(imgWidth * scaleFactor, imgHeight * scaleFactor)

        // Padding in dp
        val paddingDp = 5
        val scale = resources.displayMetrics.density
        val paddingPx = (paddingDp * scale + 0.5f).toInt()

        /*
        * initial view this setPosition willNot show, so have to set it manual fake view
        * when start the live setPosition is correct
        * */
        imgRender.setPosition(paddingPx.toFloat() - 11, 6f)
        println("imgSize QQ : $imgWidth - $imgHeight")
        return imgRender
    }

    private fun getWatermarkLogoPortrait(): ImageObjectFilterRender {
        // Calculate scale relative to stream size
        val streamWidth = resolution.width
        val streamHeight = 5 // if change this image size will update

        val watermarkBitmap = BitmapFactory.decodeResource(resources, R.drawable.soho_logo_trans)
        val imgWidth = 260
        val imgHeight = watermarkBitmap.height

        val imgRender = ImageObjectFilterRender()
        imgRender.setAlpha(2.0f)//if increase this img color get more bright but not perfect
        imgRender.setImage(watermarkBitmap)

        // Calculate the scale
        val scaleFactor =
            minOf(streamWidth.toFloat() / imgWidth, streamHeight.toFloat() / imgHeight)
        imgRender.setScale(imgWidth * scaleFactor, imgHeight * scaleFactor)

        // Padding in dp
        val paddingDp = 5
        val scale = resources.displayMetrics.density
        val paddingPx = (paddingDp * scale + 0.5f).toInt()

        /*
        * initial view this setPosition willNot show, so have to set it manual fake view
        * when start the live setPosition is correct
        * */
        imgRender.setPosition(paddingPx.toFloat() - 6, 2f)

        return imgRender
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

            runOnUiThread {
                stopBroadcast()

                //reset
                binding.layoutCountdown.visibility = View.GONE
                binding.txtCountdownMsg.text = getString(R.string.livecast_end)
                binding.txtCountdown.visibility = View.VISIBLE
                binding.imgBtnAbort.visibility = View.VISIBLE
                binding.imgDoneTick.visibility = View.GONE

                //call endApi
                viewModel.completeLiveStream(reqLive.liveStreamId)
            }
        }
    }

    private fun callRollbackApi() {
        viewModel.rollbackLiveStream(reqLive)
    }

    private fun goLiveNow() {
        updateGoLiveBtn(true)
        startBroadcast()
    }

    private fun updateGoLiveBtn(isStart: Boolean) {
        binding.txtGoLive.text = if (isStart) "Stop" else "Go Live"
        binding.imgGoLive.setImageResource(if (isStart) R.drawable.liv_cast_stop else R.drawable.livecast)

        val whiteColor = ContextCompat.getColor(this, R.color.white)
        val redColor = ContextCompat.getColor(this, R.color.red_stop)

        binding.cardGoLive.setCardBackgroundColor(if (isStart) whiteColor else redColor)
        binding.txtGoLive.setTextColor(if (isStart) redColor else whiteColor)
        binding.imgGoLive.setColorFilter(if (isStart) redColor else whiteColor)
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

            //it.setAspectRatioMode(AspectRatioMode.Fill)

            if (rtmpCamera2 == null) {
                rtmpCamera2 = RtmpCamera2(it, connectCheckerRtmp).apply {
                    setFpsListener(fpsListenerCallback)
                    //enableAutoFocus()
                }
            }
        }
    }

    private val fpsListenerCallback = FpsListener.Callback { fps ->
        //println("myStream fps : $fps")
    }

    private fun startBroadcast() {
        if (arePermissionsGranted()) {
            if (streamKey.isNotEmpty()) {
                rtmpCamera2?.let {
                    if (!it.isStreaming) {

                        //watermark
                        watermark?.visibility = View.GONE
                        val watermarkLogo =
                            if (isLand) getWatermarkLogoLandscape() else getWatermarkLogoPortrait()
                        it.glInterface.setFilter(watermarkLogo)

                        //start live
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
                            it.startStream(rtmpEndpoint + reqLive.streamKey)
                            showLiveTime(true)
                            hideShareableLinkView()
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

    private fun hideShareableLinkView() {
        binding.imgBtnShare.isEnabled = false
        binding.cardGoLive.isEnabled = false
        binding.layoutShareable.visibility = View.GONE
        binding.layoutBottom.background =
            AppCompatResources.getDrawable(this, R.drawable.top_rounded_trans_bg)
    }

    private fun showLiveTime(isStarted: Boolean) {
        rtmpCamera2?.let {
            binding.cardGoLive.isEnabled = true
            binding.imgBtnClose.visibility = if (isStarted) View.GONE else View.VISIBLE
            binding.txtLiveTime.visibility = if (isStarted) View.VISIBLE else View.GONE

            if (isStarted) {
                //timerTextHelper.start()
                timerStart()
            } else {
                //binding.txtLiveTime.text = "Live 00:00"
                //timerTextHelper.stop()
                //timerPause()
            }
        }
    }

    private fun stopBroadcast(isStopLive: Boolean = true) {
        /*
        * isDidLiveCast if sometimes completeApi will fail. so the screen will not close automatically
        * so when go back want to identify is it actually the liveCast done or not
        * */
        if (isStopLive) {
            isDidLiveCast = true
            showLiveTime(false)
        }

        rtmpCamera2?.let {
            if (it.isStreaming) {
                binding.txtCountdownMsg.text = getString(R.string.livecast_start)
                updateGoLiveBtn(false)

                it.stopStream()
            }
        }
    }

    private fun finishSendStatus(castEnd: CastEnd) {

        rotateToPortrait()

        val status = LiveCastStatus(1, castEnd)
        val jsonString = Json.encodeToString(status)

        val resultIntent = Intent().apply {
            putExtra(KEY_LIVE_STATUS, jsonString)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun rotateToPortrait() {
        // Check the current orientation
        val currentOrientation = resources.configuration.orientation

        // If the current orientation is landscape, change it to portrait
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private val connectCheckerRtmp = object : ConnectChecker {
        override fun onAuthError() {
            println("myStream onAuthError")
        }

        override fun onAuthSuccess() {
            println("myStream onAuthSuccess")
        }

        override fun onConnectionStarted(url: String) {
            println("myStream onConnectionStarted $url")
        }

        override fun onConnectionSuccess() {
            println("myStream onConnectionSuccess")
        }

        override fun onConnectionFailed(reason: String) {
            println("myStream onConnectionFailed $reason")
            if (!isLiveCastPaused) {
                onPause()
            }
            onResume()
        }

        override fun onDisconnect() {
            println("myStream onDisconnect")
            if (!isLiveCastPaused) {
                onResume()
            }
        }

        override fun onNewBitrate(bitrate: Long) {
            println("myStream onNewBitrate $bitrate")
            println("myStream noData $countBitrateError")

            //if onNewBitrate  = 0 take longer then stop lve cast and reconnect
            if (bitrate == 0L) {
                countBitrateError++

                //stop liveCast and reconnect
                if (countBitrateError == 5) {
                    onPause()
                    onResume()
                    countBitrateError = 0
                    println("myStream tempStop")
                }
            } else {
                countBitrateError = 0
            }
        }
    }

    private val surfaceHolderCallback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            rtmpCamera2?.startPreview()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            println("onSurfaceChanged $width : $height")

            //change rtmpCamera2 orientation
            /*val rotation = windowManager.defaultDisplay.rotation
            openGlView?.setStreamRotation(rotation)
            val layParam = openGlView?.layoutParams as ConstraintLayout.LayoutParams
            when (rotation) {
                0 -> layParam.dimensionRatio = "w,16:9"
                1 -> layParam.dimensionRatio = "h,9:16"
            }
            openGlView?.layoutParams = layParam*/


            /*val rotation = windowManager.defaultDisplay.rotation
            val layoutParams = openGlView?.layoutParams as ConstraintLayout.LayoutParams
            openGlView?.setStreamRotation(rotation)
            layoutParams.width = resolution.width
            layoutParams.height = resolution.height
            layoutParams.dimensionRatio = "w,16:9"
            openGlView?.layoutParams = layoutParams*/


            //ORIGINAL
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
                rtmpCamera2?.startPreview()
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
        streamKey = streamKey.ifEmpty { reqLive.streamKey }

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

    override fun onPause() {
        super.onPause()

        rtmpCamera2?.let {
            if (it.isStreaming) {
                binding.cardGoLive.isEnabled = true
                isLiveCastPaused = true
                // timerPause()
                stopBroadcast(false) // Stop the broadcast if it's still running
            }
            it.stopPreview() // Stop the camera preview
            it.glInterface?.clearFilters()
        }
    }

    override fun onResume() {
        if (isFinishing || isDestroyed) {
            return
        }

        super.onResume()
        openGlView?.let {
            if (it.holder == null) {
                it.holder?.addCallback(surfaceHolderCallback)
            }
        }

        // Recreate the RtmpCamera2 instance
        if (rtmpCamera2 == null) {
            createRtmpCamera2()
        }

        // Resume the camera preview if streaming
        if (rtmpCamera2?.isStreaming == true) {
            rtmpCamera2?.startPreview()
        }

        if (isLiveCastPaused) {
            showToastTrans("Connection Failed! Reconnecting...")

            updateGoLiveBtn(true)

            GlobalScope.launch {
                delay(500)
                runOnUiThread {
                    isLiveCastPaused = false
                    startBroadcast()
                }
            }
        }
    }

    override fun onBackPressed() {
        callRollbackApi()
        //super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()

        stopBroadcast()

        rtmpCamera2?.stopStream() // Stop the stream if still active
        rtmpCamera2?.stopPreview() // Stop the preview
        rtmpCamera2?.glInterface?.clearFilters() // Clear filters like the watermark
    }

}