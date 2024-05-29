package com.soho.sohoapp.live.network.common

sealed class ApiState<T> {

    data class Data<T>(val data: T? = null) : ApiState<T>()

    data class Loading<T>(val progressBarState: ProgressBarState = ProgressBarState.Idle) :
        ApiState<T>()

    data class Alert<T>(val alertState: AlertState = AlertState.Idle) : ApiState<T>()

}