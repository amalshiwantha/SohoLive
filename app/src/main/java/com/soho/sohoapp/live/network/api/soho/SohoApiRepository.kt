package com.soho.sohoapp.live.network.api.soho

import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.model.TsPropertyRequest
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.AuthResponse
import com.soho.sohoapp.live.network.response.GoLiveResponse
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

    fun goLivePropertyListing(authToken: String): Flow<ApiState<GoLiveResponse>> = flow {
        try {
            emit(ApiState.Loading(progressBarState = ProgressBarState.Loading))

            //get property id list
            val apiResponse = service.propertyListing(authToken = authToken)

            //get property info from type-sense
            apiResponse.data?.listings.let {
                val propIdList: List<Int> = it?.map { prop -> prop.id } ?: listOf()
                val filterBy = "objectID:$propIdList"
                val tsReq = TsPropertyRequest(
                    "*", "address_1", filterBy, "20", "1"
                )
                val apiResponseTs = service.tsProperty(tsPropRequest = tsReq)
            }

            emit(ApiState.Data(data = apiResponse))

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
