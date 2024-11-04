package com.soho.sohoapp.live.ui.view.screens.pre_rec_library

import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.db.AgentProperty
import com.soho.sohoapp.live.db.PrivateVideo
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.ui.components.AppAlertDialog
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhiteNormal
import com.soho.sohoapp.live.ui.components.CenterMessageProgress
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text400_12sp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_14spBold
import com.soho.sohoapp.live.ui.components.Text800_10sp
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.DurationDark
import com.soho.sohoapp.live.ui.view.screens.player.deleteFileFromUri
import com.soho.sohoapp.live.ui.view.screens.player.getVideoThumbnail
import com.soho.sohoapp.live.ui.view.screens.video_library.ActionIconButton
import com.soho.sohoapp.live.ui.view.screens.video_manage.NoDataView
import com.soho.sohoapp.live.utility.showToast
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreRecordLibraryScreen(
    mGState: GlobalState,
    navController: NavHostController,
    vmPreRecLib: PreRecLibraryViewModel = koinInject(),
) {
    val states = vmPreRecLib.mState.value
    var isShowAlert by remember { mutableStateOf(false) }
    var actionFile by remember { mutableStateOf(Uri.parse("")) }
    val uploadProgress by vmPreRecLib.uploadProgress.collectAsState()

    //clear upload state
    if (states.isUploading.value) {
        if (uploadProgress == 100) {
            mGState.uploadUrl.value = null
            mGState.videoFilePath.value = null
        }
    }

    //load pvt video list
    LaunchedEffect(states.videoList.value) {
        if (states.videoList.value.isEmpty()) {
            mGState.isEditVideoData.value = true
            vmPreRecLib.loadPvtVideo()
        }
    }

    LaunchedEffect(mGState.uploadUrl) {
        mGState.uploadUrl.value?.let {
            val fileVid = File(mGState.videoFilePath.value)
            vmPreRecLib.uploadVideo(fileVid, it)
        }
    }

    //show confirmation to delete video
    if (isShowAlert) {
        AppAlertDialog(
            alert = AlertConfig.DELETE_ALERT.apply {
                isConfirm = true
            },
            onConfirm = {
                actionFile?.let {
                    deleteFileFromUri(it).also { isDeleted ->
                        if (isDeleted) {
                            showToast("Private Video Deleted")
                        }
                    }
                }
                vmPreRecLib.loadPvtVideo()
                isShowAlert = false
            },
            onDismiss = {
                isShowAlert = false
            })
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = "Private Videos",
                isAllowBack = false,
                rightIcon = R.drawable.ic_close_circle,
                onBackClick = { }, onRightClick = { navController.popBackStack() })
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
            ) {
                if (states.isLoading.value) {
                    CenterMessageProgress(message = "Loading Private Video...")
                } else {

                    //display upload progress
                    if (states.isUploading.value) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LinearProgressIndicator(progress = uploadProgress / 100f)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text400_14sp(info = "$uploadProgress%")
                        }
                    }

                    MainContent(videoList = states.videoList.value,
                        onPlay = { pvtItem ->
                            openPlayEditor(navController, pvtItem, mGState)
                        },
                        onDelete = {
                            actionFile = it
                            isShowAlert = true
                        },
                        onEditPublish = { pvtItem ->
                            openPlayEditor(navController, pvtItem, mGState)
                        },
                        onUpload = {
                            //vmPreRecLib.uploadVideoOLD("authToken", File(it.path))
                        })
                }
            }
        }
    }
}

fun openPlayEditor(navController: NavHostController, pvtItem: PrivateVideo, mGState: GlobalState) {
    mGState.apply {
        privateVideoId = mutableLongStateOf(pvtItem.id.toLong())
    }

    pvtItem.agentProperty?.let {
        navigateToPlayerScreen(
            navController,
            Uri.encode(
                pvtItem.filePath
            ), it
        )
    }
}

@Composable
fun MainContent(
    onPlay: (PrivateVideo) -> Unit,
    onDelete: (Uri) -> Unit,
    onUpload: (Uri) -> Unit,
    onEditPublish: (PrivateVideo) -> Unit,
    videoList: MutableList<PrivateVideo>
) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (videoList.isEmpty()) {
            NoDataView(modifier = Modifier.fillMaxSize(), "No Private Videos")
        } else {
            //delete info
            Text400_14sp(info = "Drafts will be permanently deleted after 7 days. After that, you won’t be able to access them.")
            SpacerUp(size = 16.dp)

            //Main List
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(videoList) { pvtVid ->
                    PvtVidItemView(
                        pvtVid,
                        onPlayVideo = {
                            onPlay(it)
                        },
                        onDeleteVideo = {
                            onDelete(Uri.parse(it))
                        },
                        onDownloadVideo = {
                            onUpload(Uri.parse(it))
                        },
                        onClickManage = {
                            onEditPublish(it)
                        })
                }
            }
        }
    }
}

@Composable
private fun PvtVidItemView(
    item: PrivateVideo,
    onClickManage: (PrivateVideo) -> Unit,
    onDeleteVideo: (String) -> Unit,
    onPlayVideo: (PrivateVideo) -> Unit,
    onDownloadVideo: (String) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {

        //image title and info
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(88.dp)) {
            ThumbCenterPlay(item, onClick = {
                onPlayVideo(item)
            })
            SpacerSide(size = 16.dp)

            //Title and Description
            Column {
                Text700_14spBold(step = item.title)
                SpacerUp(size = 8.dp)
                item.description?.let {
                    Text400_12sp(label = it)
                }
            }
        }
        SpacerUp(size = 16.dp)

        //bottom action button list
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonOutlineWhiteNormal(text = "Edit & Publish",
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                onBtnClick = {
                    onClickManage(item)
                })
            SpacerSide(size = 8.dp)

            //Delete and Download btns
            Row {
                ActionIconButton(R.drawable.ic_download_bold, onClickAction = {
                    onDownloadVideo(item.filePath)
                })
                SpacerSide(size = 8.dp)
                ActionIconButton(R.drawable.ic_trash, onClickAction = {
                    onDeleteVideo(item.filePath)
                })
            }
        }
    }
}

@Composable
fun ThumbCenterPlay(item: PrivateVideo, onClick: () -> Unit) {
    Box(modifier = Modifier
        .size(88.dp)
        .fillMaxHeight()
        .clickable { onClick() }
        .clip(RoundedCornerShape(12.dp))) {

        val fileUri = Uri.parse(item.filePath)
        val thumbnail = remember { getVideoThumbnail(fileUri) }

        //thumb image
        thumbnail?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
        } ?: run {
            val placeholder = painterResource(id = R.drawable.property_placeholder)
            Image(
                painter = placeholder,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
        }

        //center play icon
        Icon(
            painter = painterResource(id = R.drawable.center_play),
            contentDescription = "Play",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
        )

        // day Label
        if (item.dayLabel != "0D") {
            Card(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(2.dp),
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = DurationDark)
            ) {
                Text800_10sp(
                    label = item.dayLabel,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

    }
}

fun navigateToPlayerScreen(
    navController: NavController,
    videoUri: String,
    agentProperty: AgentProperty
) {
    val agentPropertyJson = Json.encodeToString(agentProperty)
    val encodedAgentProperty = Uri.encode(agentPropertyJson)

    navController.navigate("${NavigationPath.PLAYER.name}/$videoUri/$encodedAgentProperty")
}