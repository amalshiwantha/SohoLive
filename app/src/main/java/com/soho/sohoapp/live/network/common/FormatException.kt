package com.soho.sohoapp.live.network.common


fun <T> formatException(e: Exception): ApiState<T> {

    e.printStackTrace()

    val errorTitle = e.cause?.message
    val errorMsg = e.message

    return ApiState.Alert(
        alertState = AlertState.Display
    )
}
