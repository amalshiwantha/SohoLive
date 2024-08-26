package com.soho.sohoapp.live.ui.view.screens.golive

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.AgencyItem
import com.soho.sohoapp.live.model.ConnectedSocialProfile
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.PropertyItem
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.AgentProfileGoLive
import com.soho.sohoapp.live.network.response.DataGoLive
import com.soho.sohoapp.live.network.response.TsPropertyResponse
import io.ktor.utils.io.printStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GoLiveViewModel(
    private val apiRepo: SohoApiRepository, private val dataStore: AppDataStoreManager,
) : ViewModel() {
    val mState: MutableState<GoLiveState> = mutableStateOf(GoLiveState())
    val assetsState: MutableState<GoLiveAssets> = mutableStateOf(GoLiveAssets())

    private val _connectedProfileNames =
        MutableStateFlow<MutableList<SocialMediaInfo>>(mutableListOf())
    val connectedProfileNames = _connectedProfileNames.asStateFlow()

    fun onTriggerEvent(event: GoLiveEvent) {
        when (event) {
            GoLiveEvent.CallLoadProperties -> {
                loadProfile()
            }

            GoLiveEvent.DismissAlert -> {
                mState.value = mState.value.copy(alertState = AlertState.Idle)
            }

            is GoLiveEvent.CallSubmitGoLive -> {
                submitGoLiveData(event.submitData)
            }

            is GoLiveEvent.CallSubmitSchedule -> {
                submitGoLiveSchedule(event.submitData)
            }
        }
    }

    private fun submitGoLiveData(submitData: GoLiveSubmit) {
        mState.value = mState.value.copy(
            loadingState = ProgressBarState.Loading,
            loadingMessage = context.getString(R.string.requesting_golive)
        )

        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {
                    submitNowGoLive(it.authenticationToken, submitData)
                }
            }
        }
    }

    private fun submitGoLiveSchedule(submitData: GoLiveSubmit) {
        mState.value = mState.value.copy(
            loadingState = ProgressBarState.Loading,
            loadingMessage = context.getString(R.string.scheduling_golive)
        )

        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {
                    submitNowGoLiveSchedule(it.authenticationToken, submitData)
                }
            }
        }
    }

    private fun submitNowGoLiveSchedule(authToken: String, submitData: GoLiveSubmit) {

        apiRepo.submitGoLiveSchedule(authToken, submitData).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {

                    apiState.data?.let { result ->

                        val isSuccess = !result.responseType.equals("error")
                        val errorMsg = result.response
                        val res = result.data

                        if (isSuccess) {
                            mState.value = mState.value.copy(goLiveResults = res)
                        } else {
                            mState.value =
                                mState.value.copy(alertState = AlertState.Display(AlertConfig.GO_LIVE_SUBMIT_ERROR.apply {
                                    message = errorMsg.orEmpty()
                                }))
                        }
                    }
                }

                is ApiState.Loading -> {
                    mState.value = mState.value.copy(loadingState = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    mState.value = mState.value.copy(alertState = apiState.alertState)
                }
            }
        }.launchIn(viewModelScope)
    }

    //same submitNowGoLive() has ScheduleViewModel
    private fun submitNowGoLive(authToken: String, submitData: GoLiveSubmit) {

        apiRepo.submitGoLive(authToken, submitData).onEach { apiState ->

            when (apiState) {

                is ApiState.Data -> {

                    apiState.data?.let { result ->

                        val isSuccess = !result.responseType.equals("error")
                        val errorMsg = result.response
                        val res = result.data

                        if (isSuccess) {
                            //resetSMState()
                            mState.value = mState.value.copy(goLiveResults = res)
                        } else {
                            mState.value =
                                mState.value.copy(alertState = AlertState.Display(AlertConfig.GO_LIVE_SUBMIT_ERROR.apply {
                                    message = errorMsg.orEmpty()
                                }))
                        }
                    }
                }

                is ApiState.Loading -> {
                    mState.value = mState.value.copy(loadingState = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    mState.value = mState.value.copy(alertState = apiState.alertState)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadProfile() {
        mState.value = mState.value.copy(loadingState = ProgressBarState.Loading)

        viewModelScope.launch {
            dataStore.userProfile.collect { profile ->
                profile?.let {
                    loadPropertyListing(it.authenticationToken)
                }
            }
        }
    }

    fun updateAssetsState(
        savedTsResults: TsPropertyResponse?,
        savedApiResults: DataGoLive,
        savedAssets: GoLiveAssets?
    ) {
        savedTsResults?.let {
            //update property list
            val selectedProperty = savedAssets?.propertyListState?.value?.find { it.isChecked }

            val foundPropList = savedTsResults.propertyList.map {
                PropertyItem(
                    id = it.document.propertyId,
                    propInfo = it.document,
                    isChecked = getCheckedStatus(selectedProperty, it.document.propertyId)
                )
            }

            assetsState.value =
                assetsState.value.copy(
                    propertyListState = mutableStateOf(
                        foundPropList
                    )
                )

            //update agentList
            val agentProfiles = savedApiResults.agentProfiles
            val selectedAgent = savedAssets?.agencyListState?.value?.find { it.isChecked }

            agentProfiles.let { agent ->
                val selectedAgentId =
                    foundPropList.filter { it.isChecked }.map { it.propInfo.apAgentsIds }
                val agentLst = getAgencyItemsById(agent, selectedAgentId, selectedAgent)
                assetsState.value =
                    assetsState.value.copy(agencyListState = mutableStateOf(agentLst))
            }

            if (mState.value.tsResults == null) {
                mState.value = mState.value.copy(tsResults = savedTsResults)
            }

            mState.value = mState.value.copy(isSuccess = true)
        }
    }

    private fun getCheckedStatus(selectedProperty: PropertyItem?, propertyId: Int): Boolean {
        selectedProperty?.let {
            if (propertyId == selectedProperty.id) {
                return selectedProperty.isChecked
            } else {
                return false
            }
        } ?: run {
            return false
        }
    }

    private fun resetPropSelections() {
        assetsState.value.propertyListState?.value =
            assetsState.value.propertyListState?.value?.map { item ->
                if (item.isChecked) item.copy(isChecked = false) else item
            } ?: listOf()
    }

    fun updatePropertyList(updatedItem: PropertyItem) {
        resetPropSelections()

        val propList = assetsState.value.propertyListState?.value

        propList?.let { listItem ->
            assetsState.value.propertyListState?.value =
                listItem.map { item ->
                    if (item.id == updatedItem.id) {
                        item.copy(isChecked = updatedItem.isChecked)
                    } else {
                        item
                    }
                }

            updateAgentList(listItem, updatedItem)
        }
    }

    fun unSelectAgentSelectionList() {
        val agentList = assetsState.value.agencyListState

        agentList?.value = agentList?.value?.map { item ->
            item.copy(isChecked = false)
        } ?: emptyList()
    }

    fun updateAgentSelectionList(updatedAgent: AgencyItem) {
        val agentList = assetsState.value.agencyListState?.value

        agentList?.let {
            it.map { prop ->
                prop.copy(
                    id = updatedAgent.id,
                    agentProfile = updatedAgent.agentProfile,
                    isChecked = updatedAgent.isChecked
                )
            }

            assetsState.value = assetsState.value.copy(agencyListState = mutableStateOf(agentList))
        }
    }

    private fun updateAgentList(propertyList: List<PropertyItem>, updatedItem: PropertyItem) {

        val agentProfiles = mState.value.apiResults?.agentProfiles

        agentProfiles?.let { agent ->
            val selectedAgentId =
                propertyList.filter { it.id == updatedItem.id }.map { it.propInfo.apAgentsIds }
            val agentLst = getAgencyItemsById(agent, selectedAgentId, null)
            assetsState.value = assetsState.value.copy(agencyListState = mutableStateOf(agentLst))
        }
    }

    private fun getAgencyItemsById(
        agentProfiles: List<AgentProfileGoLive>,
        listIds: List<List<Int>>?,
        selectedAgent: AgencyItem?
    ): List<AgencyItem> {
        return listIds?.let { lists ->
            try {
                if (lists.isNotEmpty()) {
                    val id = lists[0][0]
                    agentProfiles.filter { it.id == id }.map {

                        val isChecked = selectedAgent?.let { selAgent ->
                            it.id == selAgent.id
                        } ?: kotlin.run {
                            false
                        }

                        AgencyItem(id = it.id, agentProfile = it, isChecked = isChecked)
                    }
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStack()
                Log.e("agentFind", "Error : ${e.message}")
                emptyList()
            }

        } ?: run {
            emptyList()
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
                                    id = it.document.propertyId, propInfo = it.document
                                )
                            }

                            mState.value = mState.value.copy(apiResults = listingData.data)
                            mState.value = mState.value.copy(tsResults = tsData)
                            assetsState.value =
                                assetsState.value.copy(
                                    propertyListState = mutableStateOf(
                                        foundPropList
                                    )
                                )
                            mState.value = mState.value.copy(isSuccess = true)

                        } else {
                            mState.value =
                                mState.value.copy(alertState = AlertState.Display(AlertConfig.GO_LIVE_ERROR.apply {
                                    listingData.response?.let {
                                        message = it
                                    }
                                }))
                        }
                    }
                }

                is ApiState.Loading -> {
                    mState.value = mState.value.copy(loadingState = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    mState.value = mState.value.copy(alertState = apiState.alertState)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun loadConnectedSMList() {
        var currentList: MutableList<SocialMediaInfo>

        viewModelScope.launch {

            dataStore.getSMProfileList()?.let { profileList ->
                currentList = profileList.smProfileList.map { it.smInfo }.toMutableList()

                currentList.forEachIndexed { index, socialMediaInfo ->
                    val smProfile =
                        profileList.smProfileList.find { it.profile.type == socialMediaInfo }

                    currentList[index] = socialMediaInfo.apply {
                        accessToken = smProfile?.profile?.token
                        isConnect = smProfile?.profile?.isConnected ?: false
                    }
                }

                _connectedProfileNames.value = currentList
            }
        }
    }

    /*
    * after goLive APi is success then set itemCheck state false state from connectedSM list
    * to reopen when next
    * */
    private fun resetSMState() {
        viewModelScope.launch {
            val currentList =
                dataStore.getSMProfileList() ?: ConnectedSocialProfile(mutableListOf())

            currentList.smProfileList.map { item ->
                item.smInfo.isItemChecked = false
            }

            // Save the updated profile list
            dataStore.saveSMProfileList(currentList)
        }
    }

    fun showAlert(config: AlertConfig) {
        mState.value = mState.value.copy(alertState = AlertState.Display(config))
    }

}