package com.soho.sohoapp.live.ui.view.screens.video_library

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.enums.PropertyType
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.enums.VideoStatus
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.model.MainStateHolder
import com.soho.sohoapp.live.model.VidLibRequest
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.VideoItem
import com.soho.sohoapp.live.ui.components.AppAlertDialog
import com.soho.sohoapp.live.ui.components.ButtonColoredIcon
import com.soho.sohoapp.live.ui.components.ButtonOutLinedIcon
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhiteNormal
import com.soho.sohoapp.live.ui.components.CenterMessageProgress
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.StorageBottomSheet
import com.soho.sohoapp.live.ui.components.Text400_12sp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_10sp
import com.soho.sohoapp.live.ui.components.Text700_12spNormal
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text700_14spBold
import com.soho.sohoapp.live.ui.components.Text800_10sp
import com.soho.sohoapp.live.ui.components.Text800_14sp
import com.soho.sohoapp.live.ui.components.TextBadge
import com.soho.sohoapp.live.ui.components.TextUploadComplete
import com.soho.sohoapp.live.ui.components.TextUploadErr
import com.soho.sohoapp.live.ui.components.TextWhite14Normal
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppPrimaryDark
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.DurationDark
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import com.soho.sohoapp.live.ui.theme.LinkTxtColor
import com.soho.sohoapp.live.ui.theme.OverageRed
import com.soho.sohoapp.live.utility.NetworkUtils
import com.soho.sohoapp.live.utility.downloadFile
import com.soho.sohoapp.live.utility.getThumbUrl
import com.soho.sohoapp.live.utility.shareIntent
import com.soho.sohoapp.live.utility.showToast
import org.koin.compose.koinInject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun VideoLibraryScreen(
    mGState: GlobalState,
    navController: NavController,
    vmVidLib: VideoLibraryViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject(),
) {
    val states = vmVidLib.mState.value
    val sLiveData = mGState.videoLibResState.value
    var showAnalyticsBottomSheet by rememberSaveable { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<VideoItem?>(null) }
    var isShowAlert by remember { mutableStateOf(false) }
    var alertConfig by remember { mutableStateOf<AlertConfig?>(null) }
    var isShowProgress by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    var playVideoUrl by remember { mutableStateOf("") }
    var isShowStorageModel by remember { mutableStateOf(false) }

    //Reload the list once upload done
    LaunchedEffect(mGState.uploadStatus.value) {
        mGState.uploadStatus.value?.let {
            if (it == "done") {
                vmVidLib.reLoadData()
            }
        }
    }

    //open video player
    LaunchedEffect(playVideoUrl) {
        if (playVideoUrl.isNotEmpty()) {
            val title = "Video Player"
            val encodeUrl = URLEncoder.encode(playVideoUrl, StandardCharsets.UTF_8.toString())
            navController.navigate("${NavigationPath.VIDEO_PLAYER.name}/$title/$encodeUrl")
        }
    }

    //cal api
    LaunchedEffect(sLiveData) {
        if (sLiveData == null) {
            vmVidLib.onTriggerEvent(VidLibEvent.CallLoadVideo(VidLibRequest()))
        }
    }

    //save api response in globally
    LaunchedEffect(states.isSuccess) {
        if (states.isSuccess) {
            mGState.apply {
                videoLibResState.value = states.sApiResponse?.value
            }
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

    //show api error as alert
    if (isShowAlert) {
        alertConfig?.let {
            AppAlertDialog(alert = it, onConfirm = {
                vmVidLib.onTriggerEvent(VidLibEvent.DismissAlert)
            }, onDismiss = {
                vmVidLib.onTriggerEvent(VidLibEvent.DismissAlert)
            })
        }
    }

    //If PvtVid Record is done then auto open pvtVidLib screen
    LaunchedEffect(mGState.isOpenPvtVidLib.value) {
        if (mGState.isOpenPvtVidLib.value) {
            navController.navigate(NavigationPath.PRE_RECODED_LIST.name)
            mGState.isOpenPvtVidLib.value = false
        }
    }

    if (isShowStorageModel) {
        StorageBottomSheet(onDone = {
            isShowStorageModel = false
        })
    }

    //display main content with pull to refresh
    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        vmVidLib.reLoadData()
    }) {
        Content(isShowProgress,
            states,
            mGState,
            onManageClick = { selectedItem = it },
            onPlayVid = {
                playVideoUrl = it
            },
            onClickReloadVideoList = {
                vmVidLib.reLoadData()
            },
            onShowPvtVideo = {
                navController.navigate(NavigationPath.PRE_RECODED_LIST.name)
            },
            onStorageClick = {
                isShowStorageModel = true
            })
    }


    //open manage screen
    LaunchedEffect(selectedItem) {
        selectedItem?.let {
            mGState.apply { videoItemState.value = it }
            navController.navigate(NavigationPath.VIDEO_MANAGE.name)
        }
    }

    //show analytics data in a bottomSheet view
    /*selectedItem?.analytics?.let { analytics ->
        AnalyticsBottomSheet(showAnalyticsBottomSheet, analytics, onVisibility = {
            showAnalyticsBottomSheet = it
        })
    }*/
}

@Composable
private fun Content(
    isShowProgress: Boolean,
    state: VideoLibraryState,
    mGState: GlobalState,
    onManageClick: (VideoItem) -> Unit,
    onPlayVid: (String) -> Unit,
    onClickReloadVideoList: () -> Unit,
    onShowPvtVideo: () -> Unit,
    onStorageClick: () -> Unit
) {
    var downloadStatus by remember { mutableStateOf("") }

    //if video delete then reload the list
    LaunchedEffect(mGState.isDeletedVideo.value) {
        if (mGState.isDeletedVideo.value) {
            onClickReloadVideoList()
        }
    }

    //Show download status
    LaunchedEffect(downloadStatus) {
        if (downloadStatus.isNotEmpty()) {
            showToast(downloadStatus)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        //Private Video Button
        ButtonOutLinedIcon(text = "Private Videos", icon = R.drawable.ic_pvt_video, onBtnClick = {
            onShowPvtVideo()
        }, modifier = Modifier.padding(16.dp))

        //Show Upload Progress
        if (mGState.uploadStatus.value != "hide") {
            UploadProgressStatusView(mGState)
        }

        //Live Video List
        if (isShowProgress) {
            CenterMessageProgress(message = state.loadingMessage)
        } else {
            val dataList = mGState.videoLibResState.value?.assets?.filter {
                ((it.status == VideoStatus.READY.status && it.downloadLink != null) || it.status == VideoStatus.IN_PROG.status)
            }

            if (dataList.isNullOrEmpty()) {
                if (mGState.uploadStatus.value != "uploading") {
                    NoDataScreen(onClick = {
                        onClickReloadVideoList()
                    })
                }
            } else {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(dataList) { item ->
                        when (item.status) {
                            VideoStatus.IN_PROG.status -> {
                                if (mGState.uploadStatus.value != "uploading") {
                                    InProgItemView()
                                }
                            }

                            VideoStatus.READY.status -> {
                                ListItemView(item,
                                    onClickManage = { onManageClick(it) },
                                    onShareVideo = { shareIntent(it) },
                                    onPlayVideo = { onPlayVid(it) },
                                    onDownloadVideo = {
                                        downloadFile(it.first, it.second, onDownloadStatus = {
                                            downloadStatus = it
                                        })
                                    },
                                    onStorageClick = {
                                        onStorageClick()
                                    })
                            }
                        }
                    }
                    item {
                        NoMoreVideoView()
                    }
                }
            }
        }
    }
}

@Composable
fun NoMoreVideoView() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        SpacerUp(size = 24.dp)
        Image(painter = painterResource(id = R.drawable.vertical_line), contentDescription = "")
        SpacerSide(size = 8.dp)
        Text400_12sp(label = "No more videos to show", txtColor = HintGray)
        SpacerSide(size = 8.dp)
        Image(painter = painterResource(id = R.drawable.vertical_line), contentDescription = "")
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UploadProgressStatusView(gState: GlobalState) {
    val prog = gState.uploadProgress.value
    val status = gState.uploadStatus.value

    Card(
        modifier = Modifier.padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
        colors = CardDefaults.cardColors(containerColor = ItemCardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text700_14sp(step = "Your pre-recorded video is being uploaded")
            SpacerUp(size = 16.dp)
            LinearProgressIndicator(
                progress = prog.toFloat() / 100, modifier = Modifier.fillMaxWidth()
            )
            SpacerUp(size = 10.dp)
            // Row for progress text and cancel button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Progress & Status text
                when (status) {
                    "uploading" -> {
                        Text700_10sp(title = "$prog%")
                    }

                    "completed" -> {
                        TextUploadComplete()
                    }

                    "failed" -> {
                        TextUploadErr()
                    }
                }

                // Cancel & Dismiss button
                when (status) {
                    "uploading" -> {
                        Text700_10sp(title = "Cancel Upload",
                            color = LinkTxtColor,
                            modifier = Modifier.clickable {
                                //doCancelUpload
                            })
                    }

                    "failed" -> {
                        Text700_10sp(title = "Dismiss",
                            color = LinkTxtColor,
                            modifier = Modifier.clickable {
                                gState.uploadStatus.value = "hide"
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun InProgItemView() {
    Card(
        modifier = Modifier.padding(bottom = 24.dp),
        colors = CardDefaults.cardColors(containerColor = ItemCardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextWhite14Normal(
                title = "Your latest VOD asset is still being prepared." + " Please refresh shortly by using the pull-to-refresh"
            )
        }
    }
}

@Composable
fun CenteredScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C003E))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_videos_play_list), // replace with your icon resource
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Watch all your past livecasts",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "You can watch and manage your past livecasts here. Remember to download it before it’s deleted!",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 24.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            ButtonColoredIcon(title = "Schedule a livecast",
                btnColor = AppGreen,
                icon = R.drawable.ic_calender,
                onBtnClick = { })
        }
    }
}

@Composable
private fun NoDataScreen(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_videos_play_list),
                contentDescription = ""
            )
            SpacerUp(size = 40.dp)
            Text800_14sp(
                label = stringResource(R.string.no_data_title), txtAlign = TextAlign.Center
            )
            SpacerUp(size = 8.dp)
            Text400_14sp(
                info = stringResource(R.string.no_data_msg), txtAlign = TextAlign.Center
            )

            SpacerUp(size = 40.dp)
            ButtonColoredIcon(title = "Schedule a livecast",
                btnColor = AppGreen,
                icon = R.drawable.ic_calender,
                onBtnClick = { onClick() })
        }
    }
}

/*@Composable
fun VideoUploadProgress() {
    var progress by remember { mutableStateOf(0f) }
    var isUploading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            while (progress < 1f) {
                delay(100)
                progress += 0.01f
            }
            isUploading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .background(DurationDark),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            //property image
            Image(
                painter = rememberImagePainter("https://www.investopedia.com/thmb/bfHtdFUQrl7jJ_z-utfh8w1TMNA=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/houses_and_land-5bfc3326c9e77c0051812eb3.jpg"),
                contentDescription = "Video Thumbnail",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )

            //center status
            Spacer(modifier = Modifier.width(16.dp))
            Text700_14sp(step = "Your video is uploading")

            //upload progress and tick mark
            Spacer(modifier = Modifier.weight(1f))
            if (isUploading) {
                //upload progress
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = progress,
                        color = Color.White,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "${(progress * 100).toInt()}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                //TODO after few second needs to show shimmer view and then hide it
                //tick mark
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Upload Complete",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}*/

@Composable
private fun ListItemView(
    item: VideoItem,
    onClickManage: (VideoItem) -> Unit,
    onShareVideo: (String) -> Unit,
    onPlayVideo: (String) -> Unit,
    onDownloadVideo: (Pair<String, String>) -> Unit,
    onStorageClick: () -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {

        //badge time date
        Row(verticalAlignment = Alignment.CenterVertically) {

            val strm = item.streamType
            TextBadge(text = strm.uppercase(), bgColor = PropertyType.fromString(strm).bgColor)
            Spacer(modifier = Modifier.width(8.dp))

            val visiItem = VideoPrivacy.fromId(item.unlisted)
            TextBadge(text = visiItem.label, bgColor = visiItem.bgColor)
            Spacer(modifier = Modifier.width(8.dp))

            TextBadge(text = item.getDisplayDuration(), bgColor = DurationDark)

            Spacer(modifier = Modifier.weight(1f))

            StorageLabel(item.getRemainingDays(), onStorageClick = {
                onStorageClick()
            })
        }
        SpacerUp(size = 16.dp)

        //image title and info
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(88.dp)) {
            val playBackId = item.playbackIds[0]
            PropertyImageCenterPlay(playBackId, onClick = {
                //playVideo(item.downloadLink)
                item.downloadLink?.let { onPlayVideo(it) }
            })
            SpacerSide(size = 16.dp)
            TitleDescription(item)
        }
        SpacerUp(size = 16.dp)

        //bottom action button list
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonOutlineWhiteNormal(
                text = "Manage",
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                onBtnClick = {
                    onClickManage(item)
                })
            SpacerSide(size = 8.dp)

            //download btn
            ActionIconButton(R.drawable.ic_download_bold, onClickAction = {
                item.downloadLink?.let {
                    onDownloadVideo(Pair(it, item.title.orEmpty()))
                } ?: kotlin.run {
                    showToast("Not found a download link")
                }
            })
            SpacerSide(size = 8.dp)

            //chart btn
            /*ActionIconButton(R.drawable.ic_chart, onClickAction = {
                item.analytics?.let {
                    val analysisClick = Pair(true, item)
                    onClickAnalytics(analysisClick)
                } ?: kotlin.run {
                    showToast("Not found analytics data")
                }
            })
            SpacerHorizontal(size = 8.dp)*/

            //share btn
            ActionIconButton(R.drawable.ic_share, onClickAction = {
                item.shareableLink?.let {
                    onShareVideo(it)
                } ?: kotlin.run {
                    showToast("Not found a shareable link")
                }
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageLabel(displayDate: Int, onStorageClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        onClick = { onStorageClick() },
        colors = CardDefaults.cardColors(containerColor = DurationDark)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text800_10sp(label = "${displayDate}D STORAGE", txtColor = OverageRed)
        }
    }
}

fun getRemainingDays(createdAt: String, maxDays: Int): Int {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")

    // Parse the created date
    val videoCreatedDate: Date = dateFormat.parse(createdAt) ?: return 0

    // Get the current date
    val currentDate = Calendar.getInstance()

    // Get the created date in Calendar
    val createdCalendar = Calendar.getInstance().apply {
        time = videoCreatedDate
    }

    // Calculate the difference in milliseconds
    val differenceInMillis = currentDate.timeInMillis - createdCalendar.timeInMillis

    // Convert milliseconds to days
    val diffDays = (differenceInMillis / (1000 * 60 * 60 * 24)).toInt()

    return if (diffDays >= maxDays) {
        0
    } else {
        maxDays - diffDays
    }
}

@Composable
fun ActionIconButton(btnIcon: Int, onClickAction: () -> Unit) {
    IconButton(
        onClick = { onClickAction() }, modifier = Modifier
            .background(
                AppWhite, shape = RoundedCornerShape(16.dp)
            )
            .size(40.dp)
    ) {
        Icon(
            painter = painterResource(id = btnIcon),
            contentDescription = "Icon",
            tint = AppPrimaryDark,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun TitleDescription(item: VideoItem) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text700_12spNormal(
            label = item.getDisplayDate(),
            txtColor = HintGray,
            modifier = Modifier.fillMaxWidth()
        )

        item.title?.let {
            SpacerUp(size = 8.dp)
            Text700_14spBold(step = it, modifier = Modifier.fillMaxWidth(), isSingleLine = true)
        }

        item.description?.let {
            SpacerUp(size = 8.dp)
            Text400_12sp(label = it, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun PropertyImageCenterPlay(playBackId: String?, onClick: () -> Unit) {
    Box(modifier = Modifier
        .size(88.dp)
        .fillMaxHeight()
        .clickable { onClick() }
        .clip(RoundedCornerShape(12.dp))) {


        val imgUrl = getThumbUrl(playBackId.orEmpty())
        val urlPainter = rememberAsyncImagePainter(
            model = imgUrl,
            placeholder = painterResource(id = R.drawable.property_placeholder),
            error = painterResource(id = R.drawable.property_placeholder)
        )

        Image(
            painter = urlPainter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Icon(
            painter = painterResource(id = R.drawable.center_play),
            contentDescription = "Play",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
        )
    }
}


/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsBottomSheet(
    showBottomSheet: Boolean, analyticsData: VideoAnalytics, onVisibility: (Boolean) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()

    if (showBottomSheet) {
        ModalBottomSheet(
            containerColor = BottomBarBg,
            dragHandle = { DragHandle(color = BottomSheetDrag) },
            onDismissRequest = { onVisibility(false) },
            sheetState = bottomSheetState
        ) {
            ViewersAnalyticsContent(analyticsData)
        }
    }
}*/

/*
@Composable
fun ViewersAnalyticsContent(data: VideoAnalytics) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text800_20sp(label = "Viewers Analytics")
        SpacerUp(size = 24.dp)

        AnalyticsItem(label = "Total Views", value = data.getTotalPlayTime().toString())
        AnalyticsItem(label = "Facebook", value = data.fb.toString(), isSubItem = true)
        AnalyticsItem(label = "Youtube", value = data.yt.toString(), isSubItem = true)
        AnalyticsItem(label = "LinkedIn", value = data.li.toString(), isSubItem = true)
        AnalyticsItem(label = "Soho.com.au", value = data.soho.toString(), isSubItem = true)
        AnalyticsItem(label = "Average Playing Minutes", value = data.getFormattedPlayTime())
        SpacerUp(size = 8.dp)
    }
}

@Composable
fun AnalyticsItem(label: String, value: String, isSubItem: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text700_14sp(
            step = label,
            isBold = false,
            modifier = Modifier.padding(start = if (isSubItem) 30.dp else 0.dp)
        )
        Text700_14sp(step = value)
    }
}
*/

@Preview
@Composable
private fun PreviewVidLib() {
    //Content(false,koinInject()onManageClick = {})
}