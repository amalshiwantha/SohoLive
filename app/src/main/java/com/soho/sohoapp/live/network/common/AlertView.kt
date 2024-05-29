package com.soho.sohoapp.live.network.common

sealed class AlertView {

    data class Toast(
        val title: String,
    ) : AlertView()

    data class Dialog(
        val title: String, val description: String
    ) : AlertView()
}