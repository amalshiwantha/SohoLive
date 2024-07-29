package com.soho.sohoapp.live.ui.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.SurfaceHolder
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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
import com.soho.sohoapp.live.SohoLiveApp
import com.soho.sohoapp.live.enums.StreamResolution
import com.soho.sohoapp.live.utility.showToast

class HaishinActivity : AppCompatActivity() {

    private var watermark: ImageView? = null
    private var rtmpCamera2: RtmpCamera2? = null
    private var openGlView: OpenGlView? = null
    private var btnStartLive: Button? = null
    private val rtmpEndpoint = "rtmp://global-live.mux.com:5222/app/"
    private var streamKey: String = "0495bb6b-1ec2-6183-2b3a-eb4c04091554"
    private val PERMISSION_REQUEST_CODE = 101

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

        setContentView(R.layout.activity_haishin)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        checkRequiredPermissions()
    }

    private fun init() {
        openGlView = findViewById(R.id.openGlView)
        btnStartLive = findViewById(R.id.btnStartLive)
        watermark = findViewById(R.id.img_watermark)

        openGlView?.holder?.addCallback(surfaceHolderCallback)
        btnStartLive?.setOnClickListener {
            rtmpCamera2?.let {
                if (it.isStreaming) {
                    stopBroadcast()
                } else {
                    btnStartLive?.text = "Stop Live"
                    startBroadcast()
                }
            }
        }

        createRtmpCamera2()
    }

    override fun onPause() {
        super.onPause()

        rtmpCamera2?.let {
            if (it.isStreaming) {
                stopBroadcast()
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
                    setWatermark()
                }
            }
        }
    }

    private fun setWatermark() {
        /*val watermarkBitmap = BitmapFactory.decodeResource(resources, R.drawable.watermark_logo)
        rtmpCamera2?.glInterface?.let {
            val watermarkRender = WatermarkRender(context = SohoLiveApp.context)
            it.setFilter(watermarkRender)
            watermarkRender.setWatermark(watermarkBitmap, openGlView!!.width - watermarkBitmap.width - 16, openGlView!!.height - watermarkBitmap.height - 16)
        }*/
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

    private fun stopBroadcast() {
        rtmpCamera2?.let {
            if (it.isStreaming) {
                btnStartLive?.text = "Go Live"
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
            //viewModel.appInForeground(binding.openGlView)
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

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
