package com.soho.sohoapp.live.ui.view.screens.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.OptionDarkBg
import com.soho.sohoapp.live.ui.view.screens.golive.InfoCard
import com.soho.sohoapp.live.ui.view.screens.video_manage.PrivacyOption
import org.koin.compose.koinInject

@Composable
fun ReviewScreen(
    mGoLiveSubmit: GoLiveSubmit,
    vmReview: ReviewViewModel = koinInject(),
    mGState: GlobalState, navController: NavHostController, onDoneClick: () -> Unit = {}
) {
    val states = vmReview.mState.value
    val pvtVidId = mGState.privateVideoId.value
    var selectedOption by remember { mutableStateOf(VideoPrivacy.PRIVATE.label) }

    LaunchedEffect(states.isSuccess) {
        if (states.isSuccess) {
            if (mGState.isEditVideoData.value) {
                navController.popBackStack()
            } else {
                mGState.privateVideoId.value = -1
                onDoneClick()
            }
        }
    }

    //Load Completed
    LaunchedEffect(states.isLoadedItem) {
        if (states.isLoadedItem) {
            states.privateVideo.value?.let {
                selectedOption = it.privacy
            }
        }
    }

    //get latest item
    LaunchedEffect("get_latest") {
        vmReview.getLatestItem(pvtVidId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = "",
                onBackClick = { navController.popBackStack() }, onRightClick = { })
        },
        bottomBar = {
            ButtonColoured(
                text = "Done",
                onBtnClick = {
                    val updatedItem = states.privateVideo.value?.copy(privacy = selectedOption)
                    vmReview.updateUpload(updatedItem, mGoLiveSubmit)
                },
                color = AppGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally // Centers content horizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_done_circle),
                    contentDescription = ""
                )
                SpacerUp(size = 24.dp)
                Text950_20sp(title = "Review or publish it now")
                SpacerUp(size = 8.dp)
                Text400_14sp(
                    info = "Video will be available in your video gallery. You can publish it as unlisted or public.",
                    txtAlign = TextAlign.Center
                )
                SpacerUp(size = 24.dp)

                //Option Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = MaterialTheme.shapes.small,
                    colors = CardDefaults.cardColors(containerColor = OptionDarkBg)
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        PrivacyOption(text = VideoPrivacy.PRIVATE.label,
                            isWhiteTheme = false,
                            description = "Video will only be visible to you. You can choose to publish it as unlisted or public when ready.",
                            eyeImgId = R.drawable.ic_time,
                            isSelected = selectedOption == VideoPrivacy.PRIVATE.label,
                            txtColor = AppWhite,
                            onOptionSelected = {
                                selectedOption = VideoPrivacy.PRIVATE.label
                            })

                        SpacerUp(size = 16.dp)

                        PrivacyOption(text = VideoPrivacy.UNLISTED.label,
                            isWhiteTheme = false,
                            description = "Video won’t be publicly visible on your listing. Anyone with the direct share link can still view it.",
                            eyeImgId = R.drawable.ic_hide_eye,
                            isSelected = selectedOption == VideoPrivacy.UNLISTED.label,
                            txtColor = AppWhite,
                            onOptionSelected = {
                                selectedOption = VideoPrivacy.UNLISTED.label
                            })

                        SpacerUp(size = 16.dp)

                        PrivacyOption(text = VideoPrivacy.PUBLIC.label,
                            isWhiteTheme = false,
                            description = "Video will be publicly visible on your property listing.",
                            eyeImgId = R.drawable.ic_view_eye,
                            isSelected = selectedOption == VideoPrivacy.PUBLIC.label,
                            txtColor = AppWhite,
                            onOptionSelected = {
                                selectedOption = VideoPrivacy.PUBLIC.label
                            })

                        SpacerUp(size = 16.dp)

                        //Bottom Info Card
                        InfoCard(message = "Once video is published, it can no longer be made private.")
                    }
                }
            }
        }
    }
}
