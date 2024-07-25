package com.soho.sohoapp.live.ui.view.screens.video

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.enums.PropertyType
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.VidLibRequest
import com.soho.sohoapp.live.model.VideoAnalytics
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.network.response.VideoItem
import com.soho.sohoapp.live.ui.components.AppAlertDialog
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhite
import com.soho.sohoapp.live.ui.components.CenterMessageProgress
import com.soho.sohoapp.live.ui.components.SpacerHorizontal
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_12sp
import com.soho.sohoapp.live.ui.components.Text700_12spRight
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text800_20sp
import com.soho.sohoapp.live.ui.components.TextBadge
import com.soho.sohoapp.live.ui.components.TextBadgeDuration
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppPrimaryDark
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.BottomBarBg
import com.soho.sohoapp.live.ui.theme.BottomSheetDrag
import com.soho.sohoapp.live.ui.theme.DurationDark
import com.soho.sohoapp.live.utility.NetworkUtils
import com.soho.sohoapp.live.utility.downloadFile
import com.soho.sohoapp.live.utility.shareIntent
import com.soho.sohoapp.live.utility.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun VideoLibraryScreen(
    goLiveData: GoLiveSubmit,
    navController: NavController,
    vmVidLib: VideoLibraryViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject(),
) {
    val sVidLib = vmVidLib.mState.value
    var showAnalyticsBottomSheet by rememberSaveable { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<VideoItem?>(null) }
    var isShowAlert by remember { mutableStateOf(false) }
    var alertConfig by remember { mutableStateOf<AlertConfig?>(null) }
    var isShowProgress by remember { mutableStateOf(false) }

    //cal api
    LaunchedEffect(sVidLib.sApiResponse) {
        if (sVidLib.sApiResponse == null) {
            val req = VidLibRequest().apply {
                page = 1
                perPage = 20
                sortBy = "created_at"
                sortOrder = "desc"
            }
            vmVidLib.onTriggerEvent(VidLibEvent.CallLoadVideo(req))
        }
    }

    LaunchedEffect(sVidLib) {
        //Display alert
        if (sVidLib.alertState is AlertState.Display) {
            isShowAlert = true
            alertConfig = sVidLib.alertState.config
        } else {
            isShowAlert = false
        }

        //Show Loading view
        isShowProgress = sVidLib.loadingState == ProgressBarState.Loading
    }


    //show api error as alert
    if (isShowAlert) {
        alertConfig?.let {
            AppAlertDialog(
                alert = it,
                onConfirm = {
                    vmVidLib.onTriggerEvent(VidLibEvent.DismissAlert)
                },
                onDismiss = {
                    vmVidLib.onTriggerEvent(VidLibEvent.DismissAlert)
                })
        }
    }

    //display main content
    Content(isShowProgress, sVidLib, onManageClick = { selectedItem = it })

    //open manage screen
    LaunchedEffect(selectedItem) {
        selectedItem?.let {
            goLiveData.apply {
                this.videoItemState.value = it
            }
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

@OptIn(ExperimentalMaterial3Api::class)
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
}

@Composable
fun ViewersAnalyticsContent(data: VideoAnalytics) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text800_20sp(label = "Viewers Analytics")
        SpacerVertical(size = 24.dp)

        AnalyticsItem(label = "Total Views", value = data.getTotalPlayTime().toString())
        AnalyticsItem(label = "Facebook", value = data.fb.toString(), isSubItem = true)
        AnalyticsItem(label = "Youtube", value = data.yt.toString(), isSubItem = true)
        AnalyticsItem(label = "LinkedIn", value = data.li.toString(), isSubItem = true)
        AnalyticsItem(label = "Soho.com.au", value = data.soho.toString(), isSubItem = true)
        AnalyticsItem(label = "Average Playing Minutes", value = data.getFormattedPlayTime())
        SpacerVertical(size = 8.dp)
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

@Composable
private fun Content(
    isShowProgress: Boolean,
    sVidLib: VideoLibraryState,
    onManageClick: (VideoItem) -> Unit
) {
    var downloadStatus by rememberSaveable { mutableStateOf("") }

    //Show download status
    LaunchedEffect(downloadStatus) {
        if (downloadStatus.isNotEmpty()) {
            showToast(downloadStatus)
        }
    }

    Column(
        modifier = Modifier
            .background(brushMainGradientBg)
            .fillMaxSize()
    ) {
        if (isShowProgress) {
            CenterMessageProgress(message = sVidLib.loadingMessage)
        } else {
            val dataList = sVidLib.sApiResponse?.value?.data?.assets

            dataList?.let {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(it) { item ->
                        ListItemView(item,
                            onClickManage = { onManageClick(it) },
                            onShareVideo = { shareIntent(it) },
                            onDownloadVideo = {
                                downloadFile(it.first, it.second, onDownloadStatus = {
                                    downloadStatus = it
                                })
                            })
                    }
                }
            }
        }
    }
}

@Composable
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
}

@Composable
private fun ListItemView(
    item: VideoItem,
    onClickManage: (VideoItem) -> Unit,
    onShareVideo: (String) -> Unit,
    onDownloadVideo: (Pair<String, String>) -> Unit
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

            TextBadgeDuration(text = item.getDisplayDuration())

            Spacer(modifier = Modifier.weight(1f))
            Text700_12spRight(label = item.getDisplayDate(), txtColor = AppWhite)
        }
        SpacerVertical(size = 16.dp)

        //image title and info
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(88.dp)) {
            PropertyImageCenterPlay(item.shareableLink, onClick = {

            })
            SpacerHorizontal(size = 16.dp)
            TitleDescription(item)
        }
        SpacerVertical(size = 16.dp)

        //bottom action button list
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonOutlineWhite(text = "Manage",
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                onBtnClick = {
                    onClickManage(item)
                })
            SpacerHorizontal(size = 8.dp)

            //download btn
            ActionIconButton(R.drawable.ic_download_bold, onClickAction = {
                item.shareableLink?.let {
                    onDownloadVideo(Pair(it, item.title.orEmpty()))
                } ?: kotlin.run {
                    showToast("Not found a download link")
                }
            })
            SpacerHorizontal(size = 8.dp)

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
    Column {
        item.title?.let {
            Text700_14sp(step = it)
            SpacerVertical(size = 8.dp)
        }
        item.description?.let {
            Text400_12sp(label = it)
        }
    }
}

@Composable
fun PropertyImageCenterPlay(imageUrl: String?, onClick: () -> Unit) {
    Box(modifier = Modifier
        .size(88.dp)
        .fillMaxHeight()
        .clickable { onClick() }
        .clip(RoundedCornerShape(12.dp))) {
        val urlPainter = rememberAsyncImagePainter(
            model = imageUrl,
            placeholder = painterResource(id = R.drawable.property_placeholder),
            error = painterResource(id = R.drawable.property_placeholder)
        )

        Image(
            painter = urlPainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
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

@Preview
@Composable
private fun PreviewVidLib() {
    //Content(false,koinInject()onManageClick = {})
}