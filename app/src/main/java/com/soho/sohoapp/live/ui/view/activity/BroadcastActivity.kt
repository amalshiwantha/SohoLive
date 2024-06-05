package com.soho.sohoapp.live.ui.view.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pedro.common.ConnectChecker
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.library.rtmp.RtmpCamera1
import com.pedro.library.util.FpsListener
import com.soho.sohoapp.live.R


class BroadcastActivity : AppCompatActivity(), SurfaceHolder.Callback, ConnectChecker,
    View.OnTouchListener {

    // Logging tag
    private val TAG = "MuxLive"

    // Mux's RTMP Entry point
    private val rtmpEndpoint = "rtmp://global-live.mux.com:5222/app/"
    private var streamKey: String = "cc62507f-a298-3091-6114-0360ad1d4de6"

    // UI Element references
    private var goLiveButton: Button? = null
    private var btnChangeCamera: ImageButton? = null
    private var bitrateLabel: TextView? = null
    private var fpsLabel: TextView? = null
    private var surfaceView: SurfaceView? = null

    private var rtmpCamera: RtmpCamera1? = null
    private var liveDesired = false
    private var preset: Preset = Preset.sd_360p_30fps_1mbps

    // Encoding presets and profiles
    enum class Preset(var bitrate: Int, var width: Int, var height: Int, var frameRate: Int) {
        hd_1080p_30fps_5mbps(5000 * 1024, 1920, 1080, 30),
        hd_720p_30fps_3mbps(3000 * 1024, 1280, 720, 30),
        sd_540p_30fps_2mbps(2000 * 1024, 960, 540, 30),
        sd_360p_30fps_1mbps(1000 * 1024, 640, 360, 30),
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_broadcast)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Init the Surface View for the camera preview
        surfaceView = findViewById(R.id.surfaceView)
        surfaceView?.holder?.addCallback(this)

        // Bind to labels and buttons
        goLiveButton = findViewById(R.id.btnGoLive)
        btnChangeCamera = findViewById(R.id.btnChangeCamera)
        bitrateLabel = findViewById(R.id.bitrateLabel)
        fpsLabel = findViewById(R.id.fpslabel)

        goLiveButton?.setOnClickListener { goLiveClicked() }
        btnChangeCamera?.setOnClickListener { changeCameraClicked() }


        // Setup the camera
        surfaceView?.let {

            rtmpCamera = RtmpCamera1(it, this)

            // Listen for FPS change events to update the FPS indicator
            val callback = FpsListener.Callback { fps ->
                Log.i(TAG, "FPS: $fps")
                this@BroadcastActivity.runOnUiThread { fpsLabel?.text = "$fps fps" }
            }

            rtmpCamera?.setFpsListener(callback)

            // Keep the screen active on the Broadcast Activity
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    fun goLiveClicked() {
        Log.i(TAG, "Go Live Button tapped")

        if (liveDesired) {
            // Calling the "stopStream" function can take a while, so this happens on a new thread.
            goLiveButton?.text = "Stopping..."
            Thread {
                rtmpCamera?.stopStream()
                this@BroadcastActivity.runOnUiThread {
                    goLiveButton?.text = "Go Live!"
                }
            }.start()
            liveDesired = false
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED // Unlock orientation
        } else {
            // Lock orientation to the current orientation while stream is active
            val rotation = windowManager.defaultDisplay.rotation
            requestedOrientation = when (rotation) {
                Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                else -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            // Configure the stream using the configured preset
            rtmpCamera?.prepareVideo(
                preset.width,
                preset.height,
                preset.frameRate,
                preset.bitrate,
                2,  // Fixed 2s keyframe interval
                CameraHelper.getCameraOrientation(this)
            )

            rtmpCamera?.prepareAudio(
                128 * 1024,  // 128kbps
                48000,  // 48k
                true // Stereo
            )

            // Start the stream!
            rtmpCamera?.startStream(rtmpEndpoint + streamKey)
            liveDesired = true
            goLiveButton?.text = "Connecting... (Cancel)"
        }
    }

    // Switches between the front and back camera
    fun changeCameraClicked() {
        Log.i(TAG, "Change Camera Button tapped")
        rtmpCamera?.switchCamera()
    }

    // Little wrapper to relocate and re-pad the toast a little
    private fun muxToast(message: String) {
        val t = Toast.makeText(this@BroadcastActivity, message, Toast.LENGTH_LONG)
        t.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 5)
        t.show()
    }

    // Surfaceview Callbacks
    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        // Stop the preview if it's running

        rtmpCamera?.stopPreview()

        // Re-constrain the layout a little if the rotation of the application has changed
        val rotation = windowManager.defaultDisplay.rotation
        val l = surfaceView?.layoutParams as ConstraintLayout.LayoutParams
        when (rotation) {
            Surface.ROTATION_90, Surface.ROTATION_270 -> l.dimensionRatio = "w,16:9"
            else -> l.dimensionRatio = "h,9:16"
        }
        surfaceView?.layoutParams = l

        // Re-start the preview, which will also reset the rotation of the preview
        rtmpCamera?.startPreview(1920, 1080)
    }

    // RTMP Checker Callbacks
    fun onConnectionSuccessRtmp() {
        goLiveButton?.text = "Stop Streaming!"
        Log.i(TAG, "RTMP Connection Success")
        this@BroadcastActivity.runOnUiThread { muxToast("RTMP Connection Successful!") }
    }

    fun onConnectionFailedRtmp(reason: String) {
        Log.w(TAG, "RTMP Connection Failure")
        this@BroadcastActivity.runOnUiThread {
            goLiveButton?.text = "Reconnecting... (Cancel)"
            muxToast("RTMP Connection Failure: $reason")
        }

        goLiveClicked()
        // Retry RTMP connection failures every 5 seconds
        //rtmpCamera?.retry(5000, reason)

    }

    fun onNewBitrateRtmp(bitrate: Long) {
        //Log.d(TAG, "RTMP Bitrate Changed: " + (bitrate / 1024))
        this@BroadcastActivity.runOnUiThread {
            bitrateLabel?.text = (bitrate / 1024).toString() + " kbps"
        }
    }

    fun onDisconnectRtmp() {
        Log.i(TAG, "RTMP Disconnect")
        this@BroadcastActivity.runOnUiThread {
            bitrateLabel?.text = "0 kbps"
            fpsLabel?.text = "0 fps"
            muxToast("RTMP Disconnected!")
        }
    }

    override fun onAuthError() {
        Log.i(TAG, "onAuthError")
    }

    override fun onAuthSuccess() {
        Log.i(TAG, "onAuthSuccess")
    }

    override fun onConnectionFailed(reason: String) {
        onConnectionFailedRtmp(reason)
    }

    override fun onConnectionStarted(url: String) {
        Log.i(TAG, "onConnectionStarted $url")
    }

    override fun onConnectionSuccess() {
        onConnectionSuccessRtmp()
    }

    override fun onDisconnect() {
        onDisconnectRtmp()
    }

    override fun onNewBitrate(bitrate: Long) {
        onNewBitrateRtmp(bitrate)
    }

    // Touch Listener Callbacks
    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        return false
    }

}

