package com.soho.sohoapp.live.ui.view.screens.pre_rec_library

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.model.PrivateVideo
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhiteNormal
import com.soho.sohoapp.live.ui.components.CenterMessageProgress
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text400_12sp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text700_14spBold
import com.soho.sohoapp.live.ui.components.Text800_10sp
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.DurationDark
import com.soho.sohoapp.live.ui.view.screens.player.getVideoThumbnail
import com.soho.sohoapp.live.ui.view.screens.video_library.ActionIconButton
import org.koin.compose.koinInject

@Composable
fun PreRecordLibraryScreen(
    navController: NavHostController,
    vmPreRecLib: PreRecLibraryViewModel = koinInject(),
) {
    val states = vmPreRecLib.mState.value

    //load pvt video list
    LaunchedEffect(states.videoList.value) {
        if (states.videoList.value.isEmpty()) {
            vmPreRecLib.loadPvtVideo()
        }
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
                    MainContent(videoList = states.videoList.value,
                        onPlay = {
                            navController.navigate("${NavigationPath.PLAYER.name}/${Uri.encode(it.toString())}")
                        })
                }
            }
        }
    }
}

@Composable
fun MainContent(onPlay: (Uri) -> Unit, videoList: MutableList<PrivateVideo>) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (videoList.isEmpty()) {
            Text(
                text = "No Private Video",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
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
                            //onPlay(it)
                        },
                        onDeleteVideo = {},
                        onDownloadVideo = {},
                        onClickManage = {})
                    /*VideoFileItem(file, onPlay = {
                        onPlay(it)
                    })*/
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
    onPlayVideo: (String) -> Unit,
    onDownloadVideo: (Pair<String, String>) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {

        //image title and info
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(88.dp)) {
            ThumbCenterPlay(item, onClick = {
                onPlayVideo(item.filePath)
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
                    onDownloadVideo(Pair(item.filePath, item.title))
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
        if(item.dayLabel != "0D"){
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

@Composable
fun VideoFileItem(pvtVid: PrivateVideo, onPlay: (Uri) -> Unit) {
    val fileUri = Uri.parse(pvtVid.filePath)
    val thumbnail = remember { getVideoThumbnail(fileUri) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {
            onPlay(fileUri)
        }) {

        //thumbnail
        val commonMod = Modifier
            .size(100.dp)
            .background(Color.Gray, RoundedCornerShape(8.dp))

        if (thumbnail != null) {
            Image(
                bitmap = thumbnail.asImageBitmap(),
                contentDescription = "Video thumbnail",
                modifier = commonMod,
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = commonMod,
                contentAlignment = Alignment.Center
            ) {
                Text("No Thumbnail Available", color = Color.White)
            }
        }

        SpacerSide(size = 8.dp)

        Column(modifier = Modifier.weight(1f)) {
            Text700_14sp(step = fileUri.toString())
        }
    }
}



