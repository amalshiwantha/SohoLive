package com.soho.sohoapp.live.ui.view.screens.golive

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GoLiveViewModel(
    private val apiRepo: SohoApiRepository,
    private val userPref: AppDataStoreManager
) : ViewModel() {

    val mState: MutableState<GoLiveState> = mutableStateOf(GoLiveState())

    fun onTriggerEvent(event: GoLiveEvent) {
        when (event) {
            GoLiveEvent.CallLoadProperties -> {
                loadProfile()
            }

            GoLiveEvent.DismissAlert -> {}
        }
    }

    private fun loadProfile() {
        mState.value =
            mState.value.copy(loadingState = ProgressBarState.Loading)

        viewModelScope.launch {
            userPref.userProfile.collect { profile ->
                profile?.let {
                    loadPropertyListing(it.authenticationToken)
                }
            }
        }
    }

    fun updatePropertyList(updatedItem: PropertyItem) {
        val propertyList = mState.value.propertyListState?.value

        propertyList?.let {
            propertyList.map { prop ->
                prop.copy(
                    id = updatedItem.id,
                    propInfo = updatedItem.propInfo,
                    isChecked = !updatedItem.isChecked
                )
            }

            mState.value =
                mState.value.copy(propertyListState = mutableStateOf(propertyList))
        }


    }

    private fun loadPropertyListing(authToken: String) {

        apiRepo.goLivePropertyListing(authToken).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {
                    apiState.data?.let { result ->
                        val listingData = result.first
                        val tsData = result.second
                        val isSuccess = !listingData.equals("error")

                        if (isSuccess) {
                            val foundPropList = tsData.propertyList.map {
                                PropertyItem(
                                    id = it.document.propertyId,
                                    propInfo = it.document
                                )
                            }

                            mState.value = mState.value.copy(isSuccess = true)
                            mState.value = mState.value.copy(apiResults = listingData.data)
                            mState.value = mState.value.copy(tsResults = tsData)
                            mState.value =
                                mState.value.copy(propertyListState = mutableStateOf(foundPropList))
                        } else {
                            mState.value =
                                mState.value.copy(
                                    alertState = AlertState.Display(
                                        AlertConfig.GO_LIVE_ERROR.apply {
                                            listingData.response?.let {
                                                message = it
                                            }
                                        }
                                    )
                                )
                        }
                    }
                }

                is ApiState.Loading -> {
                    mState.value =
                        mState.value.copy(loadingState = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    mState.value = mState.value.copy(alertState = apiState.alertState)
                }
            }
        }.launchIn(viewModelScope)
    }


}