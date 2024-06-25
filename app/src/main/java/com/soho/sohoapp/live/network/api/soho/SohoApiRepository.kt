package com.soho.sohoapp.live.network.api.soho

import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.model.TsPropertyRequest
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.AuthResponse
import com.soho.sohoapp.live.network.response.GoLiveResponse
import com.soho.sohoapp.live.network.response.TsPropertyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SohoApiRepository(private val service: SohoApiServices) {

    fun signIn(loginReq: SignInRequest): Flow<ApiState<AuthResponse>> = flow {
        try {
            emit(ApiState.Loading(progressBarState = ProgressBarState.Loading))

            val apiResponse = service.login(loginReq)
            emit(ApiState.Data(data = apiResponse))

        } catch (e: Exception) {
            e.message?.let {
                emit(ApiState.Alert(alertState = AlertState.Display(AlertConfig.SIGN_IN_ERROR.apply {
                    message = it
                })))
            }
        } finally {
            emit(ApiState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }

    fun goLivePropertyListing(authToken: String): Flow<ApiState<Pair<GoLiveResponse, TsPropertyResponse>>> = flow {
        try {
            emit(ApiState.Loading(progressBarState = ProgressBarState.Loading))

            //get property id list
            val apiResponse = service.propertyListing(authToken = authToken)

            //get property info from type-sense
            apiResponse.data?.listings?.let {
                val propIdList: List<Int> = it.map { prop -> prop.id }
                //[91497, 91496, 91465, 91441, 91416, 91297]
                //val filterBy = "objectID:$propIdList"
                val filterBy = "objectID:[91497]"
                val tsReq = TsPropertyRequest(
                    "*", "address_1", filterBy, "20", "1"
                )
                val apiResponseTs = service.tsProperty(tsPropRequest = tsReq)
                val resPair = Pair(apiResponse,apiResponseTs)
                emit(ApiState.Data(data = resPair))
            } ?: run {
                emit(ApiState.Alert(alertState = AlertState.Display(AlertConfig.GO_LIVE_ERROR.apply {
                    message = "No property found"
                })))
            }

        } catch (e: Exception) {
            e.message?.let {
                emit(ApiState.Alert(alertState = AlertState.Display(AlertConfig.GO_LIVE_ERROR.apply {
                    message = it
                })))
            }
        } finally {
            emit(ApiState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}
