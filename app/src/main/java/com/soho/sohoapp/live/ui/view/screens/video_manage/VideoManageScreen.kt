package com.soho.sohoapp.live.ui.view.screens.video_manage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.model.MainStateHolder
import com.soho.sohoapp.live.model.PropertyItem
import com.soho.sohoapp.live.model.TextFiledConfig
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.Document
import com.soho.sohoapp.live.network.response.VidPrivacyRequest
import com.soho.sohoapp.live.network.response.VideoDeleteReq
import com.soho.sohoapp.live.network.response.VideoItem
import com.soho.sohoapp.live.ui.components.ButtonColoredIcon
import com.soho.sohoapp.live.ui.components.ButtonColouredProgress
import com.soho.sohoapp.live.ui.components.ButtonOutLinedIcon
import com.soho.sohoapp.live.ui.components.ButtonText
import com.soho.sohoapp.live.ui.components.ConfirmAlert
import com.soho.sohoapp.live.ui.components.DropDownWhatForLiveStream
import com.soho.sohoapp.live.ui.components.InitialProfileImage
import com.soho.sohoapp.live.ui.components.PlayerPropInfoGradient
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_12sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text700_14spProperty
import com.soho.sohoapp.live.ui.components.Text800_12sp
import com.soho.sohoapp.live.ui.components.Text950_14sp
import com.soho.sohoapp.live.ui.components.Text950_16sp
import com.soho.sohoapp.live.ui.components.TextAreaWhite
import com.soho.sohoapp.live.ui.components.TextFieldOutlined
import com.soho.sohoapp.live.ui.components.TextProgress
import com.soho.sohoapp.live.ui.components.TopAppBarCustomClose
import com.soho.sohoapp.live.ui.components.brushLiveGradientBg
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.AppWhiteGray
import com.soho.sohoapp.live.ui.theme.DurationDark
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import com.soho.sohoapp.live.ui.theme.OptionDarkBg
import com.soho.sohoapp.live.ui.theme.OverageRed
import com.soho.sohoapp.live.ui.theme.TextDark
import com.soho.sohoapp.live.ui.theme.infoGray
import com.soho.sohoapp.live.ui.theme.infoText
import com.soho.sohoapp.live.ui.view.screens.golive.AmenitiesView
import com.soho.sohoapp.live.ui.view.screens.golive.TypeAndCheckBox
import com.soho.sohoapp.live.ui.view.screens.subscription.BulletText
import com.soho.sohoapp.live.ui.view.screens.video_library.VidLibEvent
import com.soho.sohoapp.live.ui.view.screens.video_library.getRemainingDays
import com.soho.sohoapp.live.utility.TrackAssetManageVideo
import com.soho.sohoapp.live.utility.TrackAssetUpdate
import com.soho.sohoapp.live.utility.getThumbUrl
import com.soho.sohoapp.live.utility.hexToColor
import com.soho.sohoapp.live.utility.shareIntent
import com.soho.sohoapp.live.utility.showToast
import org.koin.compose.koinInject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun VideoManageScreen(
    mGState: GlobalState,
    vmVidManage: VideoManageViewModel = koinInject(),
    navController: NavHostController,
) {

    val states = vmVidManage.mState.value
    val itemData = mGState.videoItemState.value?.copy()
    var isShowProgress by remember { mutableStateOf(false) }
    var isShowAlert by remember { mutableStateOf(false) }
    var alertConfig by remember { mutableStateOf<AlertConfig?>(null) }
    var playVideoUrl by remember { mutableStateOf("") }
    var isShowConfirmAlert by remember { mutableStateOf(false) }

    //confirmation to delete time slot
    if (isShowConfirmAlert) {
        ConfirmAlert(
            title = "Confirm Delete",
            message = "Are you sure you want to delete this video item?",
            isShowDialog = isShowConfirmAlert,
            onDismiss = { isShowConfirmAlert = it }, onConfirm = {
                itemData?.id?.let { callDeleteApi(it, vmVidManage) }
            }
        )
    }

    //open video player
    LaunchedEffect(playVideoUrl) {
        if (playVideoUrl.isNotEmpty()) {
            val title = "Video Player"
            val encodeUrl = URLEncoder.encode(playVideoUrl, StandardCharsets.UTF_8.toString())
            navController.navigate("${NavigationPath.VIDEO_PLAYER.name}/$title/$encodeUrl")
        }
    }

    //save updated itemData to the mLiveData videoItemState
    LaunchedEffect(states.isSuccess) {
        if (states.isSuccess) {
            showToast("Video Data Updated")
            states.updatedPrivacy.value?.let {
                mGState.videoItemState.value?.apply {
                    unlisted = it.unlisted
                    streamType = it.streamType
                    title = it.title
                    description = it.description
                }
            }
            navController.popBackStack()
        }
    }

    //success delete
    LaunchedEffect(states.isSuccessDelete) {
        if (states.isSuccessDelete) {
            mGState.isDeletedVideo.value = true
            showToast("Video Deleted! Reloading...")
            navController.popBackStack()
        }
    }

    //Show Loading view
    LaunchedEffect(states.loadingState) {
        isShowProgress = states.loadingState == ProgressBarState.Loading
    }

    //Display alert
    LaunchedEffect(states.alertState) {
        if (states.alertState is AlertState.Display) {
            isShowAlert = true
            alertConfig = states.alertState.config
        } else {
            isShowAlert = false
        }
    }

    MainContent(data = itemData,
        isShowProgress = isShowProgress,
        onBackClick = { navController.popBackStack() },
        onSaveClick = { updateVideoItem(it, vmVidManage) },
        onPlayClick = {
            playVideoUrl = itemData?.downloadLink ?: ""
        },
        onDeleteClick = {
            isShowConfirmAlert = true
        })
}

private fun callDeleteApi(itemId: Int, vmVidManage: VideoManageViewModel) {
    vmVidManage.onTriggerEvent(
        VidLibEvent.CallDeleteVideo(VideoDeleteReq(id = itemId))
    )
}

private fun updateVideoItem(copyVidItem: VideoItem?, vmVidManage: VideoManageViewModel) {
    copyVidItem?.let {
        vmVidManage.onTriggerEvent(
            VidLibEvent.CallUpdateVideo(
                VidPrivacyRequest(
                    id = it.id,
                    status = it.unlisted,
                    streamType = it.streamType.lowercase(),
                    title = it.title.orEmpty(),
                    description = it.description.orEmpty()
                )
            )
        )
    } ?: run {
        showToast("Video ID not found")
    }
}

@Composable
private fun MainContent(
    data: VideoItem?,
    isShowProgress: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: (VideoItem) -> Unit,
    onPlayClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var visibleData by remember { mutableStateOf(data) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        val (topAppBar, content, button) = createRefs()

        //action bar
        TopAppBarCustomClose(title = "Manage Video",
            rightIcon = R.drawable.ic_cross,
            modifier = Modifier.constrainAs(topAppBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onCloseClick = { onBackClick() })

        //content
        Box(modifier = Modifier
            .fillMaxSize()
            .constrainAs(content) {
                top.linkTo(topAppBar.bottom)
                bottom.linkTo(button.top)
                height = Dimension.fillToConstraints
            }
            .padding(16.dp)) {
            visibleData?.let {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        InnerContent(it, onDataUpdate = {
                            visibleData = it
                        }, onPlayClick = { onPlayClick() })
                    }
                    item {
                        StorageLeftCard(videoItem = it)
                    }
                    item {
                        DeleteBtnView(onDeleteClick = {
                            onDeleteClick()
                        })
                    }
                }
            } ?: run {
                NoDataView(modifier = Modifier.align(Alignment.Center))
            }
        }

        //Bottom Button
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            //Share Button
            ButtonOutLinedIcon(icon = R.drawable.ic_share_white, modifier = Modifier.size(48.dp),
                onBtnClick = {
                    visibleData?.shareableLink?.let {
                        shareIntent(it)
                    }
                })

            SpacerSide(size = 8.dp)

            //Save Button
            visibleData?.let {
                ButtonColouredProgress(
                    text = "Save Changes",
                    isLoading = isShowProgress,
                    isTxtBold = true,
                    color = AppGreen,
                    onBtnClick = {
                        TrackAssetUpdate(it.id)
                        onSaveClick(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }

    }
}

@Composable
fun DeleteBtnView(onDeleteClick: () -> Unit) {
    SpacerUp(size = 16.dp)
    Box {
        ButtonText(
            text = "Delete Video",
            onBtnClick = { onDeleteClick() },
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        )
    }
}

/*@Composable
fun StorageLeftCard(videoItem: VideoItem) {
    val activePln = MainStateHolder.mState.activePlan.value
    activePln?.let { plan ->
        Column(modifier = Modifier.fillMaxWidth()) {
            SpacerUp(size = 24.dp)
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = DurationDark)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {

                    val maxDays = plan.terms.inAppStorageDays
                    val leftDays = maxDays.toInt() - getRemainingDays(videoItem.startedAt)

                    //Storage Left Days
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text700_14sp(step = "Storage Left")
                        Spacer(modifier = Modifier.weight(1f))
                        Text950_14sp(title = "$leftDays/$maxDays Days Left", txtColor = OverageRed)
                    }

                    //Info Card
                    SpacerUp(size = 16.dp)
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = AppWhiteGray.copy(alpha = 0.2f))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painter = painterResource(id = R.drawable.light),
                                    contentDescription = ""
                                )
                                SpacerSide(size = 8.dp)
                                Text700_14sp(step = "Ways to keep your videos", color = AppWhite)
                            }

                            SpacerUp(size = 16.dp)

                            Column(modifier = Modifier.padding(start = 4.dp)) {
                                BulletText(value = "Download it before it expires")
                                BulletText(value = "Get a multicast plan for 90 days of storage")
                                BulletText(value = "Access it on the social channels where you streamed")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteBtnView(onDeleteClick: () -> Unit) {
    SpacerUp(size = 24.dp)
    Box {
        ButtonText(
            text = "Delete Video",
            onBtnClick = { onDeleteClick() },
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        )
    }
}*/

@Composable
fun StorageLeftCard(videoItem: VideoItem) {
    val activePln = MainStateHolder.mState.activePlan.value
    activePln?.let { plan ->
        Column(modifier = Modifier.fillMaxWidth()) {
            SpacerUp(size = 24.dp)
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = DurationDark)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {

                    val maxDays = plan.terms.inAppStorageDays
                    val leftDays = getRemainingDays(videoItem.startedAt, maxDays.toInt())

                    //Storage Left Days
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text400_14sp(info = "Storage Left")
                        Spacer(modifier = Modifier.weight(1f))
                        Text950_14sp(title = "$leftDays/$maxDays Days Left", txtColor = OverageRed)
                    }

                    //Info Card
                    SpacerUp(size = 16.dp)
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = AppWhiteGray.copy(alpha = 0.2f))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painter = painterResource(id = R.drawable.light),
                                    contentDescription = ""
                                )
                                SpacerSide(size = 8.dp)
                                Text700_14sp(step = "Ways to keep your videos", color = AppWhite)
                            }

                            SpacerUp(size = 16.dp)

                            Column(modifier = Modifier.padding(start = 4.dp)) {
                                BulletText(value = "Download it before it expires")
                                BulletText(value = "Get a multicast plan for 90 days of storage")
                                BulletText(value = "Access it on the social channels where you streamed")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InnerContent(
    itemInfo: VideoItem,
    onDataUpdate: (VideoItem) -> Unit,
    onPlayClick: () -> Unit
) {
    Column {
        //privacy
        Text950_16sp(title = "Visibility Settings")
        SpacerUp(size = 8.dp)
        PrivacySettings(itemInfo.unlisted,
            onChangePrivacy = {
                itemInfo.unlisted = VideoPrivacy.toBool(it)
                onDataUpdate(itemInfo)
            },
            onShare = {
                itemInfo.shareableLink?.let {
                    shareIntent(it)
                }
            })

        //property
        itemInfo.property?.let {
            //Property info
            SpacerUp(size = 40.dp)
            PropertyView(it)

            //Update Info
            SpacerUp(size = 8.dp) //PropertyView has 16 bottom
            Text950_16sp(title = "Update Video Details")
            SpacerUp(size = 8.dp)
            UpdateForm(itemInfo, onInfoUpdate = { updated ->
                onDataUpdate(updated)
            })

            //watch video
            SpacerUp(size = 24.dp)
            VideoView(itemInfo, onPlayClick = { onPlayClick() })
        }
    }
}

@Composable
fun UpdateForm(item: VideoItem, onInfoUpdate: (VideoItem) -> Unit) {
    val optionList = mutableListOf("Inspection", "Auction")
    var txtCounter by rememberSaveable { mutableStateOf("0/3000") }

    val configPurpose = TextFiledConfig(
        input = item.streamType.replaceFirstChar { it.uppercaseChar() },
        placeholder = "Enter Purpose"
    )

    val configTitle = TextFiledConfig(
        input = item.title.orEmpty(),
        placeholder = "Address or title for your livecast",
    )

    val configDesc = TextFiledConfig(
        input = item.description.orEmpty(),
        placeholder = "Let viewers know more about what you are streaming. E.g. Property description, address, etc.",
        imeAction = ImeAction.Done
    )

    //save default value
    configPurpose.input = item.streamType.replaceFirstChar { it.uppercaseChar() }

    Text700_14sp(step = stringResource(R.string.what_livecast), isBold = false)
    DropDownWhatForLiveStream(
        options = optionList, placeHolder = "Select an option", onValueChangedEvent = {
            item.streamType = it.lowercase()
            onInfoUpdate(item)
        }, fieldConfig = configPurpose
    )

    //title
    SpacerUp(size = 8.dp)
    Text700_14sp(step = "Stream title", isBold = false)
    TextFieldOutlined(tfConfig = configTitle, onTextChange = {
        item.title = it
        onInfoUpdate(item)
    })

    //description
    SpacerUp(size = 8.dp)
    Row {
        Text700_14sp(step = "Description", modifier = Modifier.weight(1f), isBold = false)
        Text700_14sp(step = txtCounter, isBold = false)
    }
    TextAreaWhite(fieldConfig = configDesc, onTextChange = {
        item.description = it.first
        txtCounter = it.second
        onInfoUpdate(item)
    })
}

@Composable
private fun VideoView(item: VideoItem, onPlayClick: () -> Unit) {
    Column {
        Text950_16sp(title = "Watch Video")
        SpacerUp(size = 8.dp)
        VideoItemContent(item, onPlayClick = { onPlayClick() })
    }
}

@Composable
fun VideoItemContent(vidItem: VideoItem, onPlayClick: () -> Unit) {
    val propInfo = vidItem.property

    Card(
        shape = RoundedCornerShape(16.dp), modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Box {
            // Background Image
            val playBackId = vidItem.playbackIds[0]
            val imgUrl = getThumbUrl(playBackId)
            val urlPainter = rememberAsyncImagePainter(
                model = imgUrl,
                placeholder = painterResource(id = R.drawable.property_placeholder),
                error = painterResource(id = R.drawable.property_placeholder)
            )

            Image(
                painter = urlPainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Overlay content
            Column(modifier = Modifier.fillMaxSize()) {
                //Watermark and Duration
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.watermark_logo),
                        contentDescription = "",
                    )
                    Text800_12sp(
                        label = vidItem.getDisplayDuration(),
                        modifier = Modifier
                            .background(
                                Color.Black.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    )
                }

                // Play Button
                Box(modifier = Modifier
                    .fillMaxSize()
                    .clickable { onPlayClick() }
                    .weight(1f),
                    contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_play_video),
                        contentDescription = "Play",
                        modifier = Modifier.size(48.dp)
                    )
                }

                // Bottom Row: Property & Agent
                Column {
                    //Property and Ameths Info
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(brush = PlayerPropInfoGradient)
                            .padding(8.dp)
                    ) {
                        Text700_12sp(
                            label = vidItem.property?.fullAddress().orEmpty(), txtColor = AppWhite
                        )
                        SpacerUp(size = 2.dp)
                        propInfo?.let {
                            AmenitiesView(it, AppWhite, isCompact = true)
                        }
                    }

                    //Agent Info
                    vidItem.getAgent()?.let { agent ->
                        val profImgSize = 32.dp
                        Row(
                            modifier = Modifier
                                .background(agent.agencyBgColor)
                                .fillMaxWidth()
                        ) {
                            //profile image and name
                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                //profile image
                                agent.avatar_url?.let {
                                    val urlPainter = rememberAsyncImagePainter(
                                        model = it,
                                        placeholder = painterResource(id = R.drawable.profile_placeholder),
                                        error = painterResource(id = R.drawable.profile_placeholder)
                                    )

                                    Image(
                                        painter = urlPainter,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(profImgSize)
                                            .clip(CircleShape)
                                    )
                                } ?: kotlin.run {
                                    InitialProfileImage(
                                        agent.full_name,
                                        profImgSize,
                                        isSmall = true
                                    )
                                }

                                SpacerSide(size = 8.dp)

                                //name
                                Text700_12sp(label = agent.full_name, txtColor = TextDark)
                            }

                            //agency logo
                            agent.banner_image?.let {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(end = 8.dp)
                                ) {
                                    val imgUrl = rememberAsyncImagePainter(model = it)

                                    Image(
                                        painter = imgUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.FillWidth,
                                        modifier = Modifier.size(
                                            width = profImgSize * 2,
                                            height = profImgSize
                                        )
                                    )
                                }
                            }
                        }
                    }
                    //end agent info
                }


                /*Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.1f),
                                    Color.Black.copy(alpha = 0.6f),
                                    Color.Black
                                )
                            )
                        )
                        .padding(8.dp)
                ) {
                    val agent = vidItem.getAgent()
                    val agentLogoUrl = agent?.banner_image ?: ""
                    val agentColor = getAgentColor(agent?.agent_bg_colour)

                    val logoAgent = rememberAsyncImagePainter(
                        model = agentLogoUrl,
                        placeholder = painterResource(id = R.drawable.empty_photo),
                        error = painterResource(id = R.drawable.empty_photo)
                    )

                    Image(
                        painter = logoAgent,
                        contentDescription = null,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(agentColor)
                    )
                    SpacerSide(size = 8.dp)

                }*/
            }
        }
    }
}

private fun getAgentColor(agentBgColour: String?): Color {
    return agentBgColour?.let {
        if (it.isNotEmpty()) {
            it.hexToColor()
        } else {
            Color.White
        }
    } ?: run {
        Color.White
    }
}

@Composable
private fun PropertyView(doc: Document) {
    val propItem = PropertyItem(1, doc)
    Column {
        Text950_16sp(title = "Linked Listing")
        SpacerUp(size = 8.dp)
        PropertyItemContent(propItem, isClickable = false)
    }
}

@Composable
fun PrivacySettings(
    visibility: Boolean,
    isWhiteTheme: Boolean = false,
    onChangePrivacy: (String) -> Unit,
    onShare: () -> Unit = {},
) {

    val privacyItem = VideoPrivacy.fromId(visibility)
    var selectedOption by remember { mutableStateOf(privacyItem.label) }
    val pub = VideoPrivacy.PUBLIC.label
    val pvt = VideoPrivacy.UNLISTED.label

    val bgColor = if (isWhiteTheme) AppWhite else OptionDarkBg
    val txtColor = if (isWhiteTheme) OptionDarkBg else AppWhite

    Column(
        modifier = Modifier.background(bgColor, shape = RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(if (isWhiteTheme) 0.dp else 16.dp)
                .fillMaxWidth()
        ) {
            PrivacyOption(text = pvt,
                isWhiteTheme = isWhiteTheme,
                description = "Your video won’t be publicly visible on your listing. Anyone with the direct share link can still view it.",
                eyeImgId = R.drawable.ic_hide_eye,
                isSelected = selectedOption == pvt,
                txtColor = txtColor,
                onOptionSelected = {
                    selectedOption = pvt
                    onChangePrivacy(selectedOption)
                })

            SpacerUp(size = 16.dp)

            PrivacyOption(isWhiteTheme = isWhiteTheme,
                text = pub,
                description = "Your video will be publicly visible on your property listing.",
                eyeImgId = R.drawable.ic_view_eye,
                isSelected = selectedOption == pub,
                txtColor = txtColor,
                onOptionSelected = {
                    selectedOption = pub
                    onChangePrivacy(selectedOption)
                })


            if (isWhiteTheme) {
                SpacerUp(size = 16.dp)
                VisibleInfoView()
            } else {
                SpacerUp(size = 16.dp)
                ButtonColoredIcon(
                    title = "Share Video Link",
                    btnColor = AppGreen,
                    icon = R.drawable.ic_share_white,
                    onBtnClick = { onShare() }
                )
            }
        }
    }
}

@Composable
fun VisibleInfoView() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = infoGray)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(16.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_info), contentDescription = "")
            SpacerSide(size = 8.dp)
            Text400_14sp(
                info = "You can change the video status or delete the video after going live.",
                color = infoText
            )
        }
    }
}

@Composable
fun PrivacyOption(
    text: String,
    description: String,
    isSelected: Boolean,
    eyeImgId: Int,
    onOptionSelected: () -> Unit,
    txtColor: Color,
    isWhiteTheme: Boolean,
    isShowSelection: Boolean = true,
    modifier: Modifier = Modifier
) {
    val privacyConfig = VideoPrivacy.fromLabel(text)

    Column(modifier = modifier
        .fillMaxWidth()
        .clickable { onOptionSelected() }) {

        //Radio button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            //privacy radio button
            if (isShowSelection) {
                val radioIcon =
                    if (isSelected) R.drawable.radio_active else R.drawable.radio_inactive
                Image(
                    painter = painterResource(id = radioIcon), contentDescription = null
                )
                SpacerSide(size = 8.dp)
            }

            //labels public, unlisted and live
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //label privacy name
                Box(
                    modifier = Modifier
                        .background(
                            privacyConfig.bgColor, shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = eyeImgId),
                            contentDescription = "",
                            modifier = Modifier.size(12.dp)
                        )
                        SpacerSide(size = 4.dp)
                        Text800_12sp(label = text)
                    }
                }

                //label live
                if (text == VideoPrivacy.PUBLIC.label && isWhiteTheme) {
                    SpacerSide(size = 8.dp)
                    Box(
                        modifier = Modifier
                            .background(
                                brush = brushLiveGradientBg, shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_lightning),
                                contentDescription = ""
                            )
                            SpacerSide(size = 4.dp)
                            Text800_12sp(label = "LIVE INSTANTLY")
                        }
                    }
                }

            }
        }

        //info
        val startPadding = if (isShowSelection) 24.dp else 0.dp
        SpacerUp(size = 8.dp)
        Text400_14sp(
            info = description, modifier = Modifier.padding(start = startPadding), color = txtColor
        )
    }
}

@Composable
fun NoDataView(modifier: Modifier, title: String = "No Valid Information") {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextProgress(title = title, color = AppWhite)
    }
}

@Composable
fun PropertyItemContent(
    item: PropertyItem, isClickable: Boolean = true, onItemClicked: (PropertyItem) -> Unit = {}
) {
    val cardBgColor = if (item.isChecked) AppWhite else ItemCardBg
    val textColor = if (item.isChecked) ItemCardBg else AppWhite
    val property = item.propInfo

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onItemClicked(item.apply { isChecked = !isChecked }) },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp)) {
            //image
            val urlPainter = rememberAsyncImagePainter(
                model = property.thumbnailUrl(),
                placeholder = painterResource(id = R.drawable.property_placeholder),
                error = painterResource(id = R.drawable.property_placeholder)
            )

            Image(
                painter = urlPainter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(width = 70.dp, height = 68.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            //info
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .fillMaxWidth()
            ) {

                TypeAndCheckBox(
                    item.isChecked,
                    isClickable,
                    property,
                    txtColor = textColor,
                    onCheckedChange = {
                        onItemClicked(item.apply { isChecked = !isChecked })
                    })
                SpacerUp(size = 8.dp)
                Text700_14spProperty(step = property.fullAddress(), color = textColor)
                SpacerUp(size = 8.dp)
                if (false) Text400_14sp(info = "3 scheduled livestream", color = textColor)
                SpacerUp(size = 8.dp)
                AmenitiesView(property, textColor)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewVidManage() {
    //VideoManageScreen(mGState = GlobleState(), navController = NavHostController(LocalContext.current))
}
