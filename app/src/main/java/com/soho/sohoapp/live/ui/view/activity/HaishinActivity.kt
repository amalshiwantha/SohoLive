package com.soho.sohoapp.live.ui.view.activity

import android.os.Bundle
import android.view.SurfaceHolder
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pedro.common.ConnectChecker
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.library.rtmp.RtmpCamera2
import com.pedro.library.util.FpsListener
import com.pedro.library.view.OpenGlView
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.StreamResolution


class HaishinActivity : AppCompatActivity() {

    private var rtmpCamera2: RtmpCamera2? = null
    private var openGlView: OpenGlView? = null
    private var btnStartLive: Button? = null
    private val rtmpEndpoint = "rtmp://global-live.mux.com:5222/app/"
    private var streamKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_haishin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inits()
        showStreamKeyDialog()
    }

    private fun showStreamKeyDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Enter Mux Stream Key")

        val input = EditText(this)
        input.hint = "Find it form https://dashboard.mux.com/"
        alertDialogBuilder.setView(input)

        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            val key = input.text.toString().trim()
            if (key.isNotEmpty()) {
                streamKey = key
            } else {
                showStreamKeyDialog()
            }
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
            finish()
        }

        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private object StreamParameters {
        var resolution = StreamResolution.HD
        const val fps = 30
        const val startBitrate = 400 * 1024
        const val iFrameIntervalInSeconds = 5
        const val maxBitrate = 4000 * 1024
        const val backgroundStreamingTimeOutInMillis: Long = 60000
    }

    private fun inits() {
        openGlView = findViewById(R.id.openGlView)
        btnStartLive = findViewById(R.id.btnStartLive)

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


        /*rtmpCamera2?.let {
            it.replaceView(openGlView)
            it.startPreview(StreamParameters.resolution.width, StreamParameters.resolution.height)
        }*/
    }

    fun createRtmpCamera2() {
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

    fun startBroadcast() {
        if (streamKey.isNotEmpty()) {
            rtmpCamera2?.let {
                if (!it.isStreaming) {
                    if (it.prepareAudio() && it.prepareVideo(
                            StreamParameters.resolution.width,
                            StreamParameters.resolution.height,
                            StreamParameters.fps,
                            StreamParameters.startBitrate,
                            StreamParameters.iFrameIntervalInSeconds,
                            CameraHelper.getCameraOrientation(application)
                        )
                    ) {
                        println("myStream  startBroadcast")
                        it.startStream(rtmpEndpoint + streamKey)
                    } else {
                        println("myStream Error startBroadcast")
                    }
                }
            }
        } else {
            showStreamKeyDialog()
        }
    }

    fun stopBroadcast() {
        rtmpCamera2?.let {
            if (it.isStreaming) {
                btnStartLive?.text = "Go Live"
                println("myStream stopBroadcast")
                it.stopStream()
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
            Toast.makeText(
                applicationContext,
                "ConnectionFailed. Add new Stream Key.",
                Toast.LENGTH_LONG
            ).show()
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
            Toast.makeText(
                applicationContext,
                "Disconnected. Add new Stream Key.",
                Toast.LENGTH_LONG
            ).show()
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
}