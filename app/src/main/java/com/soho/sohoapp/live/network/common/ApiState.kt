package com.soho.sohoapp.live.network.common

import network.common.NetworkState

sealed class ApiState<T> {

    data class NetworkStatus<T>(val networkState: NetworkState) : ApiState<T>()

    data class Data<T>(val data: T? = null, val status: Boolean = true) : ApiState<T>()

    data class Loading<T>(val progressBarState: ProgressBarState = ProgressBarState.Idle) :
        ApiState<T>()

    data class Response<T>(val uiComponent: UIComponent) : ApiState<T>()

}