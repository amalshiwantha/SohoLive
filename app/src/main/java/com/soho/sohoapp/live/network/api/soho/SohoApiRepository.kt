package com.soho.sohoapp.live.network.api.soho

import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.common.formatException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SohoApiRepository(private val service: SohoApiServices) {

    fun signIn(loginReq: SignInRequest): Flow<ApiState<String>> = flow {
        try {

            emit(ApiState.Loading(progressBarState = ProgressBarState.Loading))

            val apiResponse = service.login(loginReq)
            val recipeResult = apiResponse.alert.message

            emit(ApiState.Data(recipeResult))

        } catch (e: Exception) {
            emit(formatException(e))
        } finally {
            emit(ApiState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}
