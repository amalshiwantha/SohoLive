package com.soho.sohoapp.live.ui.view.screens.video_edit_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.db.PrivateVideo
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.model.TextFiledConfig
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.DropDownWhatForLiveStream
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.TextAreaWhite
import com.soho.sohoapp.live.ui.components.TextFieldOutlined
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.view.screens.golive.InfoCard
import org.koin.compose.koinInject

@Composable
fun VidEditDetailsScreen(
    mGState: GlobalState,
    navController: NavHostController,
    vmVidEdit: VidEditDetailsViewModel = koinInject(),
) {
    val states = vmVidEdit.mState.value
    val pvtVidId = mGState.privateVideoId.value

    //get latest item
    LaunchedEffect("get_latest") {
        vmVidEdit.getLatestItem(pvtVidId)
    }

    //if success update then close the screen
    LaunchedEffect(states.isSuccess) {
        if (states.isSuccess) {
            navController.popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = "Video Details",
                isAllowBack = false,
                rightIcon = R.drawable.ic_close_circle,
                onBackClick = { }, onRightClick = { navController.popBackStack() })
        },
        bottomBar = {
            ButtonColoured(
                text = "Update",
                color = AppGreen,
                onBtnClick = {
                    vmVidEdit.updateDetails(states.privateVideo.value)
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brushMainGradientBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    states.privateVideo.value?.let {
                        EditForm(it)
                    }
                }
            }
        }
    }
}

@Composable
fun EditForm(itemData: PrivateVideo) {
    val optionList = mutableListOf("Inspection", "Auction", "Other")
    var txtCounter by rememberSaveable { mutableStateOf("0/3000") }

    val configPurpose = TextFiledConfig(
        input = itemData.castFor,
        placeholder = "Enter Purpose"
    )

    val configTitle = TextFiledConfig(
        input = itemData.title,
        placeholder = "Address or title for your livecast",
    )

    val configDesc = TextFiledConfig(
        input = itemData.description.orEmpty(),
        placeholder = "Let viewers know more about what you are streaming. E.g. Property description, address, etc.",
        imeAction = ImeAction.Done
    )

    //save default value
    configPurpose.input = itemData.castFor

    Text700_14sp(step = "What is this livestream for?", isBold = false)
    DropDownWhatForLiveStream(
        options = optionList, placeHolder = "Select an option", onValueChangedEvent = {
            itemData.castFor = it
        }, fieldConfig = configPurpose
    )

    //title
    SpacerUp(size = 8.dp)
    Text700_14sp(step = "Stream title", isBold = false)
    TextFieldOutlined(tfConfig = configTitle, onTextChange = {
        itemData.title = it
    })

    //description
    SpacerUp(size = 8.dp)
    Row {
        Text700_14sp(step = "Description", modifier = Modifier.weight(1f), isBold = false)
        Text700_14sp(step = txtCounter, isBold = false)
    }
    TextAreaWhite(fieldConfig = configDesc, onTextChange = {
        itemData.description = it.first
        txtCounter = it.second
    })

    SpacerUp(size = 8.dp)
    InfoCard(message = "You can still change your video details later if needed.")
}


