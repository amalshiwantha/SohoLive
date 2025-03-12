package com.soho.sohoapp.live.ui.view.screens.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.MainStateHolder
import com.soho.sohoapp.live.model.UploadData
import com.soho.sohoapp.live.ui.components.ButtonColouredProgress
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.TopAppBarActionBack
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.OptionDarkBg
import com.soho.sohoapp.live.ui.view.screens.golive.InfoCard
import com.soho.sohoapp.live.ui.view.screens.video_manage.PrivacyOption
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import com.soho.sohoapp.live.utility.TrackPreRecordSaved
import com.soho.sohoapp.live.utility.TrackPreRecordStarted
import org.koin.compose.koinInject

@Composable
fun ReviewScreen(
    mGState: GlobalState,
    mGoLiveSubmit: GoLiveSubmit,
    vmReview: ReviewViewModel = koinInject(),
    navController: NavHostController,
    onDoneClick: () -> Unit = {},
    onBackListing: () -> Unit = {}
) {
    val states = vmReview.mState.value
    val pvtVidId = mGState.privateVideoId.value
    var selectedOption by remember { mutableStateOf(VideoPrivacy.PRIVATE.label) }

    //if privacy changed as private then back to pre rec library
    LaunchedEffect(states.isDone) {
        if (states.isDone) {
            doNavigate(mGState, onBackListing = {
                onBackListing()
            }, onDoneClick = {
                onDoneClick()
            })
        }
    }

    //if get upload url api call success then star the upload
    LaunchedEffect(states.isSuccess) {
        if (states.isSuccess) {

            AppEventBus.sendEvent(
                AppEvent.UploadVideo(
                    UploadData(
                        states.uploadUrl, states.fileUrl
                    )
                )
            )

            mGState.privateVideoId.value = -1
            onDoneClick()
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

    //Main Content
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(brushMainGradientBg)
    ) {
        val (actionBar, content, bottomButton) = createRefs()

        //TopActionBar
        TopAppBarActionBack(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(actionBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            onBackClick = {
                navController.popBackStack()
            },
            isShowBackBg = true
        )

        //Center Content
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(content) {
                top.linkTo(actionBar.bottom)
                bottom.linkTo(bottomButton.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height =
                    Dimension.fillToConstraints
            }) {
            item {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
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
                    }

                    //Option Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(containerColor = OptionDarkBg)
                    ) {
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
                                description = "Publish as Unlisted. Your video won’t be publicly visible on your listing. Anyone with the direct share link can still view it.",
                                eyeImgId = R.drawable.ic_hide_eye,
                                isSelected = selectedOption == VideoPrivacy.UNLISTED.label,
                                txtColor = AppWhite,
                                onOptionSelected = {
                                    selectedOption = VideoPrivacy.UNLISTED.label
                                })

                            SpacerUp(size = 16.dp)

                            PrivacyOption(text = VideoPrivacy.PUBLIC.label,
                                isWhiteTheme = false,
                                description = "Publish as Public. Your video will be publicly visible on your property listing.",
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

        //Bottom Button
        ButtonColouredProgress(text = "Done",
            isLoading = states.isUploading.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .constrainAs(bottomButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            color = AppGreen,
            onBtnClick = {
                val updatedItem = states.privateVideo.value?.copy(privacy = selectedOption)

                if (mGState.isEditVideoData.value) {
                    vmReview.updateUpload(updatedItem, null)
                } else {
                    val isPvt = selectedOption == VideoPrivacy.PRIVATE.label
                    TrackPreRecordSaved(mGoLiveSubmit.propertyId,isPvt)

                    vmReview.updateUpload(updatedItem, mGoLiveSubmit)
                }
            })
    }
}

fun doNavigate(
    mGState: GlobalState,
    onDoneClick: () -> Unit,
    onBackListing: () -> Unit
) {
    if (mGState.isEditVideoData.value) {
        onBackListing()
    } else {
        mGState.privateVideoId.value = -1
        onDoneClick()
    }
}
