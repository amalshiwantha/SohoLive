package com.soho.sohoapp.live.utility

import android.os.Handler
import android.widget.TextView
import kotlin.concurrent.Volatile


class TimerTextHelper(private val textView: TextView) : Runnable {
    private val handler: Handler = Handler()

    @Volatile
    private var startTime: Long = 0

    @Volatile
    var elapsedTime: Long = 0
        private set

    override fun run() {
        val millis = System.currentTimeMillis() - startTime
        var seconds = (millis / 1000).toInt()
        val minutes = seconds / 60
        seconds = seconds % 60

        textView.text = "Live "+String.format("%02d:%02d", minutes, seconds)

        if (elapsedTime == -1L) {
            handler.postDelayed(this, 500)
        }
    }

    fun start() {
        this.startTime = System.currentTimeMillis()
        this.elapsedTime = -1
        handler.post(this)
    }

    fun stop() {
        this.elapsedTime = System.currentTimeMillis() - startTime
        handler.removeCallbacks(this)
    }
}