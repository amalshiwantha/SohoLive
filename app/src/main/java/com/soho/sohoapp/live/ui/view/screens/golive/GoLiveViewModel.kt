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
import com.soho.sohoapp.live.model.MainStateHolder
import com.soho.sohoapp.live.model.PropertyItem
import com.soho.sohoapp.live.network.api.soho.SohoApiRepository
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ApiState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.AgentProfileGoLive
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

    val mState = MainStateHolder.mState
    val liveState: MutableState<GoLiveState> = mutableStateOf(GoLiveState())
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
                liveState.value = liveState.value.copy(alertState = AlertState.Idle)
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
        liveState.value = liveState.value.copy(
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
        liveState.value = liveState.value.copy(
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
                            liveState.value = liveState.value.copy(goLiveResults = res)
                        } else {
                            liveState.value =
                                liveState.value.copy(alertState = AlertState.Display(AlertConfig.GO_LIVE_SUBMIT_ERROR.apply {
                                    message = errorMsg.orEmpty()
                                }))
                        }
                    }
                }

                is ApiState.Loading -> {
                    liveState.value = liveState.value.copy(loadingState = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    liveState.value = liveState.value.copy(alertState = apiState.alertState)
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
                            liveState.value = liveState.value.copy(goLiveResults = res)
                        } else {
                            liveState.value =
                                liveState.value.copy(alertState = AlertState.Display(AlertConfig.GO_LIVE_SUBMIT_ERROR.apply {
                                    message = errorMsg.orEmpty()
                                }))
                        }
                    }
                }

                is ApiState.Loading -> {
                    liveState.value = liveState.value.copy(loadingState = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    liveState.value = liveState.value.copy(alertState = apiState.alertState)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadProfile() {
        liveState.value = liveState.value.copy(loadingState = ProgressBarState.Loading)

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
        savedAssets: GoLiveAssets?
    ) {
        savedTsResults?.let {
            //update property list
            val selectedProperty = savedAssets?.propertyListState?.value?.find { it.isChecked }

            val foundPropList = savedTsResults.propertyList.map {
                PropertyItem(
                    id = it.document.objectId?.toInt() ?: 0,
                    propInfo = it.document,
                    isChecked = getCheckedStatus(
                        selectedProperty,
                        it.document.objectId?.toInt() ?: 0
                    )
                )
            }

            assetsState.value =
                assetsState.value.copy(
                    propertyListState = mutableStateOf(
                        foundPropList
                    )
                )

            //update agentList
            val selectedAgent = savedAssets?.agencyListState?.value?.find { it.isChecked }

            val checkedAgents = foundPropList.filter { it.isChecked }
            if (!checkedAgents.isNullOrEmpty()) {
                loadAgentList(checkedAgents[0], selectedAgent)
            }

            //save other live Data
            if (liveState.value.tsResults == null) {
                liveState.value = liveState.value.copy(tsResults = savedTsResults)
            }

            liveState.value = liveState.value.copy(isSuccess = true)
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

            loadAgentList(updatedItem, null)
        }
    }

    fun unSelectAgentSelectionList() {
        val agentList = assetsState.value.agencyListState

        agentList?.value = agentList?.value?.map { item ->
            item.copy(isChecked = false)
        } ?: emptyList()
    }

    fun updateAgentSelectionList(selectedAgent: AgencyItem) {
        val agentList = assetsState.value.agencyListState?.value

        agentList?.let { list ->
            // Create a new list where all items are unchecked first
            val updatedList = list.map { prop ->
                prop.copy(isChecked = false)
            }.map { prop ->
                // Then mark only the selected item as checked
                if (prop.id == selectedAgent.id) {
                    prop.copy(
                        id = selectedAgent.id,
                        agentProfile = selectedAgent.agentProfile,
                        isChecked = selectedAgent.isChecked
                    )
                } else {
                    prop // Return the item unchanged
                }
            }

            // Update the state with the new agent list
            assetsState.value =
                assetsState.value.copy(agencyListState = mutableStateOf(updatedList))
        }
    }

    /*
    * load agent list from the selected property item
    * */
    private fun loadAgentList(updatedProp: PropertyItem, selectedAgent: AgencyItem?) {

        val agentProfiles = mState.goLiveApiRes?.agentProfiles
        val propAgents =
            mState.goLiveApiRes?.listings?.find { it.id == updatedProp.id }?.agentProfileIds

        propAgents?.let { ids ->
            val filteredAgentProfiles = agentProfiles?.filter { agentProfile ->
                ids.contains(agentProfile.id)
            }
            val agentLst = getAgentList(filteredAgentProfiles, selectedAgent)

            assetsState.value = assetsState.value.copy(agencyListState = mutableStateOf(agentLst))
        } ?: kotlin.run {
            assetsState.value =
                assetsState.value.copy(agencyListState = mutableStateOf(emptyList()))
        }
    }

    private fun getAgentList(
        filteredAgentProfiles: List<AgentProfileGoLive>?,
        selectedAgent: AgencyItem?
    ): MutableList<AgencyItem> {
        val agentItems = mutableListOf<AgencyItem>()

        filteredAgentProfiles?.forEach {
            val isChecked = selectedAgent?.let { seleAgent ->
                if (it.id == seleAgent.id) {
                    seleAgent.isChecked
                } else {
                    false
                }
            } ?: kotlin.run {
                false
            }

            agentItems.add(AgencyItem(id = it.id, agentProfile = it, isChecked = isChecked))
        }
        return agentItems
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
                                    id = it.document.objectId?.toInt() ?: 0, propInfo = it.document
                                )
                            }

                            //save in global
                            mState.goLiveApiRes = listingData.data
                            mState.propertyTsRes = tsData
                            mState.sPropList = mutableStateOf(foundPropList)

                            liveState.value = liveState.value.copy(apiResults = listingData.data)
                            liveState.value = liveState.value.copy(tsResults = tsData)
                            assetsState.value =
                                assetsState.value.copy(
                                    propertyListState = mutableStateOf(
                                        foundPropList
                                    )
                                )
                            liveState.value = liveState.value.copy(isSuccess = true)

                        } else {
                            liveState.value =
                                liveState.value.copy(alertState = AlertState.Display(AlertConfig.GO_LIVE_ERROR.apply {
                                    listingData.response?.let {
                                        message = it
                                    }
                                }))
                        }
                    }
                }

                is ApiState.Loading -> {
                    liveState.value = liveState.value.copy(loadingState = apiState.progressBarState)
                }

                is ApiState.Alert -> {
                    liveState.value = liveState.value.copy(alertState = apiState.alertState)
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
        liveState.value = liveState.value.copy(alertState = AlertState.Display(config))
    }

}