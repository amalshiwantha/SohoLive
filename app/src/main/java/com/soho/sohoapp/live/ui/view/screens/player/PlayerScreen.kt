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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.db.AgentProperty
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.ui.components.AppAlertDialog
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhiteNormal
import com.soho.sohoapp.live.ui.components.InitialProfileImage
import com.soho.sohoapp.live.ui.components.PlayerPropInfoGradient
import com.soho.sohoapp.live.ui.components.PreRecVidSuccessView
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text700_12sp
import com.soho.sohoapp.live.ui.components.Text700_12spRight
import com.soho.sohoapp.live.ui.components.Text700_12sp_reg
import com.soho.sohoapp.live.ui.components.TopAppBarBackAction
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.TextDark
import com.soho.sohoapp.live.ui.view.screens.golive.AmenitiesIcon
import com.soho.sohoapp.live.ui.view.screens.video_edit_details.VidEditDetailsViewModel
import com.soho.sohoapp.live.utility.visibleValue
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import java.io.File
import kotlin.math.roundToInt

@Composable
fun PlayerScreen(
    mGState: GlobalState,
    navController: NavHostController,
    fileUri: Uri,
    agentProperty: AgentProperty?,
    vmVidEdit: VidEditDetailsViewModel = koinInject(),
    onNextClick: () -> Unit = {}
) {

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = false

    // Store the previous system UI colors
    val previousStatusBarColor = remember { systemUiController.statusBarDarkContentEnabled }
    val previousNavigationBarColor = remember { systemUiController.navigationBarDarkContentEnabled }

    // Set the system UI colors to black when the screen is composed
    DisposableEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = Color.Black,
            darkIcons = useDarkIcons
        )

        // Restore the previous system UI colors when the screen is disposed
        onDispose {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = previousStatusBarColor
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent,
                darkIcons = previousNavigationBarColor
            )
        }
    }

    val states = vmVidEdit.mState.value
    val pvtVidId = mGState.privateVideoId.value
    var showSuccessMessage by rememberSaveable { mutableStateOf(true) }
    var isShowAlert by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var isShowPlayer by remember { mutableStateOf(true) }
    var isLandscape by remember { mutableStateOf(false) }
    val thumbnail: Bitmap? = remember(fileUri) {
        createVideoThumbnail(
            fileUri.path ?: "", MediaStore.Images.Thumbnails.MINI_KIND
        )
    }

    // Reset showSuccessMessage when navigating away or on screen change
    DisposableEffect(Unit) {
        onDispose {
            showSuccessMessage = false
        }
    }

    //get latest item
    LaunchedEffect("get_latest") {
        vmVidEdit.getLatestItem(pvtVidId)
    }

    //show confirmation to delete video
    if (isShowAlert) {
        AppAlertDialog(alert = AlertConfig.DELETE_ALERT.apply {
            isConfirm = true
        }, onConfirm = {
            deleteFileFromUri(fileUri)
            navController.popBackStack()
            isShowAlert = false
        }, onDismiss = {
            isShowAlert = false
        })
    }

    //Main Content
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        val (actionBar, content, bottomButton) = createRefs()

        //TopActionBar
        /*TopAppBarActionBack(isShowBack = if (mGState.isEditVideoData.value) true else false,
            rightIcon = R.drawable.ic_trash,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(actionBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            onBackClick = {
                isShowPlayer = false
                navController.popBackStack()
            },
            onActionClick = { isShowAlert = true })*/
        TopAppBarBackAction(
            leftIcon = R.drawable.back_bg_round,
            rightIcon = R.drawable.ic_trash,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(actionBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            onBackClick = {
                isShowPlayer = false
                navController.popBackStack()
            },
            onActionClick = {
                isShowAlert = true
            }
        )

        //set dynamically position
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp

        //Dynamic % of screen height
        val videoHeight = (screenHeight * 0.3f).value.roundToInt().dp
        val paddingTop = (screenHeight * if (isLandscape) 0.04f else 0.03f).value.roundToInt().dp
        val paddingStart = (screenWidth * 0.04f).value.roundToInt().dp

        states.privateVideo.value?.let { pvtVid ->
            val orientation = pvtVid.videoInfo?.orientation
            isLandscape = orientation == "landscape"
        }

        //Change the player size according to the video orintation
        var customModifier = Modifier.fillMaxSize()
        if (isLandscape) {
            customModifier = Modifier.height(videoHeight)
        }

        //Center Player
        Column(modifier = Modifier
            .wrapContentHeight()
            .constrainAs(content) {
                top.linkTo(actionBar.bottom)
                bottom.linkTo(bottomButton.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
            .padding(24.dp)) {
            Box(modifier = customModifier) {
                //Player
                if (isShowPlayer) {
                    AndroidView(modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
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
                        })
                }

                //Video Thumbnail
                if (!isPlaying) {
                    thumbnail?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Video Thumbnail",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Play IconButton in the center
                if (!isPlaying) {
                    Image(modifier = Modifier
                        .clickable {
                            isPlaying = true
                        }
                        .align(Alignment.Center)
                        .size(56.dp),
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "Play")
                }

                //If record done then show a message
                if (!mGState.isEditVideoData.value) {
                    if (showSuccessMessage) {
                        PreRecVidSuccessView(modifier = Modifier.align(Alignment.Center),
                            onDismiss = {
                                showSuccessMessage = false
                            })
                        LaunchedEffect(Unit) {
                            delay(5000)
                            showSuccessMessage = false
                        }
                    }
                }

                //Soho Overlay
                Image(
                    painter = painterResource(id = R.drawable.soho_watermark),
                    contentDescription = "watermark",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = paddingStart, top = paddingTop)
                )

                //Agent & Property Overlay
                states.privateVideo.value?.let { pvtVid ->
                    val isTemplated = pvtVid.videoInfo?.isEnableTemplate ?: false

                    if (isTemplated) {
                        agentProperty?.let {

                            val coroutineScope = rememberCoroutineScope()
                            val graphicsLayer = rememberGraphicsLayer()

                            val mod = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                            AgentPropertyInfo(it, mod, pvtVid.isHideAgent)
                        }
                    }
                }
            }
        }

        //Bottom Button
        BottomButton(modifier = Modifier.constrainAs(bottomButton) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, onNextClick = { onNextClick() }, onEditClick = {
            navController.navigate(NavigationPath.VIDEO_EDIT_DETAILS.name)
        })
    }
}

@Composable
fun AgentPropertyInfo(agProp: AgentProperty, boxMod: Modifier, isHideAgent: Boolean) {
    Column(boxMod) {

        //Property Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = PlayerPropInfoGradient)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                agProp.address?.let {
                    Text700_12sp(label = it, txtColor = AppWhite, isSingleLine = true)
                }
                SpacerUp(size = 4.dp)
                AmenitiesViewSmall(agProp, AppWhite)
                SpacerUp(size = 16.dp)
            }
        }

        //Agent Info
        agProp.agent?.let { agent ->
            val profImgSize = 32.dp
            val mod = if (isHideAgent) Modifier.height(profImgSize) else Modifier
            Row(
                modifier = mod
                    .background(agent.agencyBgColor)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //profile image and name
                if (!isHideAgent) {
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
                        Text700_12sp(label = agent.full_name, txtColor = TextDark)
                    }
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
                } ?: kotlin.run {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 8.dp)
                    ) {
                        Text700_12spRight(
                            label = agent.agencyName, txtColor = agent.agencyNameTextColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AmenitiesViewSmall(ag: AgentProperty, textColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        ag.bedrooms.visibleValue()?.let {
            Text700_12sp_reg(label = it, txtColor = textColor)
            AmenitiesIcon(icon = R.drawable.ic_bedroom, iconColor = textColor, isCompact = false)
        }

        ag.bathrooms.visibleValue()?.let {
            Text700_12sp_reg(label = it, txtColor = textColor)
            AmenitiesIcon(icon = R.drawable.ic_bathroom, iconColor = textColor, isCompact = false)
        }

        ag.parking.visibleValue()?.let {
            Text700_12sp_reg(label = it, txtColor = textColor)
            AmenitiesIcon(icon = R.drawable.ic_car_park, iconColor = textColor, isCompact = false)
        }

        ag.areaSize?.let {
            Text700_12sp_reg(label = it.first, txtColor = textColor)
            AmenitiesIcon(icon = it.second, iconColor = textColor, isCompact = false)
        }
    }
}

/*@Composable
fun AgentPropertyInfoNoPadding(agProp: AgentProperty, boxMod: Modifier) {
    Column(boxMod) {

        //Property Info
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            agProp.address?.let {
                Text700_14spProperty(step = it, color = AppWhite, isSingleLine = true)
            }
            SpacerUp(size = 8.dp)
            AmenitiesViewSmall(agProp, AppWhite)
            SpacerUp(size = 16.dp)
        }

        //Agent Info
        agProp.agent?.let { agent ->
            val profImgSize = 40.dp
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
}*/

@Composable
fun BottomButton(modifier: Modifier, onNextClick: () -> Unit, onEditClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ButtonOutlineWhiteNormal(
            text = "Edit Details", onBtnClick = {
                onEditClick()
            }, modifier = Modifier.weight(1f)
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

fun deleteFileFromUri(fileUri: Uri? = null, vidFile: File? = null): Boolean {
    return try {
        val file = vidFile ?: File(fileUri?.path)
        if (file.exists()) {
            file.delete()
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
        File(videoUri.path).toString(), MediaStore.Images.Thumbnails.MINI_KIND
    )
}
