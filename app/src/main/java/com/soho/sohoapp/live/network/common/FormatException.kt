package com.soho.sohoapp.live.network.common


fun <T> formatException(e: Exception): ApiState<T> {

    e.printStackTrace()

    val errorTitle = e.cause?.message
    val errorMsg = e.message

    return ApiState.AlertResponse(
        alertView = AlertView.Dialog(
            title = errorTitle.toString(),
            description = errorMsg.toString()
        )
    )
}
