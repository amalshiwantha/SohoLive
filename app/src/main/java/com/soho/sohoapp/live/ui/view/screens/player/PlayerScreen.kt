package com.soho.sohoapp.live.ui.view.screens.player

import android.graphics.Bitmap
import android.media.ThumbnailUtils.createVideoThumbnail
import android.net.Uri
import android.provider.MediaStore
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.db.AgentProperty
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.ui.components.AppAlertDialog
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhiteNormal
import com.soho.sohoapp.live.ui.components.InitialProfileImage
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.TextDark
import java.io.File

@Composable
fun PlayerScreen(
    mGState: GlobalState,
    navController: NavHostController,
    fileUri: Uri,
    agentProperty: AgentProperty?,
    onNextClick: () -> Unit = {}
) {

    var isShowAlert by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    val thumbnail: Bitmap? = remember(fileUri) {
        createVideoThumbnail(
            fileUri.path ?: "",
            MediaStore.Images.Thumbnails.MINI_KIND
        )
    }

    //show confirmation to delete video
    if (isShowAlert) {
        AppAlertDialog(
            alert = AlertConfig.DELETE_ALERT.apply {
                isConfirm = true
            },
            onConfirm = {
                deleteFileFromUri(fileUri)
                navController.popBackStack()
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
                title = "",
                rightIcon = R.drawable.ic_trash,
                isAllowBack = mGState.isEditVideoData.value,
                onBackClick = { navController.popBackStack() }, onRightClick = {
                    //show confirmation to remove
                    isShowAlert = true
                })
        },
        bottomBar = {
            BottomButton(onNextClick = { onNextClick() }, onEditClick = {
                navController.navigate(NavigationPath.VIDEO_EDIT_DETAILS.name)
            })
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brushMainGradientBg)
                .padding(innerPadding)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                //Player
                AndroidView(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxSize(),
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            //set mediaController
                            val mediaController = MediaController(ctx)
                            mediaController.setAnchorView(this)
                            setMediaController(mediaController)

                            //set video path
                            setVideoURI(fileUri)
                            setOnPreparedListener { mediaPlayer ->
                                if (isPlaying) mediaPlayer.start()
                            }
                        }
                    },
                    update = { videoView ->
                        if (isPlaying) {
                            videoView.start()
                        } else {
                            videoView.pause()
                        }
                    }
                )

                //Video Thumbnail
                if (!isPlaying) {
                    thumbnail?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Video Thumbnail",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 16.dp)
                        )
                    }
                }

                // Play IconButton in the center
                if (!isPlaying) {
                    Image(
                        modifier = Modifier
                            .clickable {
                                isPlaying = true
                            }
                            .align(Alignment.Center)
                            .size(56.dp),
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "Play"
                    )
                }

                //Soho Overlay
                Image(
                    painter = painterResource(id = R.drawable.soho_watermark),
                    contentDescription = "watermark",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 52.dp, top = 8.dp)
                )

                //Agent & Property Overlay
                agentProperty?.let {
                    val mod = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(start = 43.dp, end = 43.dp, bottom = 16.dp)
                    AgentPropertyInfo(it, mod)
                }
            }
        }
    }
}

@Composable
fun AgentPropertyInfo(agProp: AgentProperty, modifier: Modifier) {
    val profImgSize = 40.dp
    agProp.agent?.let { agent ->
        Row(modifier = modifier.background(agent.agencyBgColor)) {
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
                    InitialProfileImage(agent.full_name, profImgSize, isSmall = true)
                }

                SpacerSide(size = 8.dp)

                //name
                Text700_14sp(step = agent.full_name, color = TextDark)
            }

            //agency logo
            agent.banner_image?.let {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                ) {
                    val urlPainter = rememberAsyncImagePainter(model = it)

                    Image(
                        painter = urlPainter,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.size(width = profImgSize * 2, height = profImgSize)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomButton(onNextClick: () -> Unit, onEditClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ButtonOutlineWhiteNormal(
            text = "Edit Details",
            onBtnClick = {
                onEditClick()
            },
            modifier = Modifier.weight(1f)
        )
        SpacerSide(size = 16.dp)
        ButtonColoured(
            text = "Next",
            onBtnClick = { onNextClick() },
            color = AppGreen,
            modifier = Modifier.weight(1f)
        )
    }
}

fun deleteFileFromUri(fileUri: Uri): Boolean {
    return try {
        val file = File(fileUri.path) // Convert Uri to File
        if (file.exists()) {
            file.delete() // Delete the file
        } else {
            false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun getVideoThumbnail(videoUri: Uri): Bitmap? {
    return createVideoThumbnail(
        File(videoUri.path).toString(),
        MediaStore.Images.Thumbnails.MINI_KIND
    )
}
