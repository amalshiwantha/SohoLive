package com.soho.sohoapp.live.ui.view.screens.video_manage

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.PropertyItem
import com.soho.sohoapp.live.network.response.Document
import com.soho.sohoapp.live.network.response.VideoItem
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.SpacerHorizontal
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_12sp
import com.soho.sohoapp.live.ui.components.Text700_8sp
import com.soho.sohoapp.live.ui.components.Text800_12sp
import com.soho.sohoapp.live.ui.components.Text950_16sp
import com.soho.sohoapp.live.ui.components.TextProgress
import com.soho.sohoapp.live.ui.components.TopAppBarCustomClose
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.OptionDarkBg
import com.soho.sohoapp.live.ui.view.screens.golive.AmenitiesView
import com.soho.sohoapp.live.ui.view.screens.golive.PropertyItemContent
import com.soho.sohoapp.live.utility.playVideo
import org.koin.compose.koinInject

@Composable
fun VideoManageScreen(
    mLiveData: GoLiveSubmit,
    viewMVidMng: VideoManageViewModel = koinInject(),
    navController: NavHostController,
) {
    val itemData = mLiveData.videoItemState.value

    MainContent(
        data = itemData,
        onBackClick = { navController.popBackStack() },
        onSaveClick = { updateVideoItem(navController, mLiveData) },
        onPlayClick = { playVideo(itemData?.downloadLink) })
}

fun updateVideoItem(navController: NavHostController, mLiveData: GoLiveSubmit) {/*
    * save updated itemData to the mLiveData videoItemState and
    * find updated the property  in  videoLibResState -> DataVidRes assets: List<VideoItem> using id
    * */
    mLiveData.videoLibResState.value?.assets?.find { it.id == mLiveData.videoItemState.value?.propertyListingId }
        ?.let {
            mLiveData.videoItemState.value?.property = it.property
        }

    navController.popBackStack()
}

@Composable
fun MainContent(
    data: VideoItem?,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onPlayClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        val (topAppBar, content, button) = createRefs()

        //action bar
        TopAppBarCustomClose(
            title = "Manage Video",
            rightIcon = R.drawable.ic_cross,
            modifier = Modifier.constrainAs(topAppBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onCloseClick = { onBackClick() })

        //content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(content) {
                    top.linkTo(topAppBar.bottom)
                    bottom.linkTo(button.top)
                    height = Dimension.fillToConstraints
                }
                .padding(16.dp)
        ) {
            data?.let {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item { InnerContent(it, onPlayClick = { onPlayClick() }) }
                }
            } ?: run {
                NoDataView(modifier = Modifier.align(Alignment.Center))
            }
        }

        //Bottom Button
        data?.let {
            ButtonColoured(
                text = "Save Changes",
                color = AppGreen,
                onBtnClick = { onSaveClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .constrainAs(button) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
private fun InnerContent(itemInfo: VideoItem, onPlayClick: () -> Unit) {
    Column {
        //privacy
        PrivacySettings(itemInfo.unlisted, onChangePrivacy = {
            itemInfo.unlisted = VideoPrivacy.toBool(it)
        })

        //property
        itemInfo.property?.let {
            SpacerVertical(size = 40.dp)
            PropertyView(it)

            //video
            SpacerVertical(size = 24.dp)
            VideoView(itemInfo, onPlayClick = { onPlayClick() })
        }
    }
}

@Composable
private fun VideoView(item: VideoItem, onPlayClick: () -> Unit) {
    Column {
        Text950_16sp(title = "Watch Video")
        SpacerVertical(size = 8.dp)
        VideoItemContent(item, onPlayClick = { onPlayClick() })
    }
}

@Composable
fun VideoItemContent(vidItem: VideoItem, onPlayClick: () -> Unit) {
    val propInfo = vidItem.property

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box {
            // Background Image
            val urlPainter = rememberAsyncImagePainter(
                model = propInfo?.thumbnailUrl(),
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
                // Top Row: Time and Date
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text700_12sp(
                        label = vidItem.getDisplayDuration(),
                        txtColor = AppWhite,
                        modifier = Modifier
                            .background(
                                Color.Black.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    )

                    Text700_12sp(
                        label = vidItem.getDisplayDate(), txtColor = AppWhite, modifier = Modifier
                            .background(
                                Color.Black.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    )
                }

                // Play Button
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onPlayClick() }
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.center_play),
                        contentDescription = "Play",
                        modifier = Modifier.size(48.dp)
                    )
                }

                // Bottom Row: Property Details
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_soho),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    SpacerHorizontal(size = 8.dp)
                    Column {
                        Text700_8sp(title = "3/19 Weeroona Avenue, Woollahra")
                        SpacerVertical(size = 2.dp)
                        propInfo?.let {
                            AmenitiesView(it, AppWhite, isCompact = true)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PropertyView(doc: Document) {
    val propItem = PropertyItem(1, doc)
    Column {
        Text950_16sp(title = "Linked Listing")
        SpacerVertical(size = 8.dp)
        PropertyItemContent(propItem, isClickable = false)
    }
}

@Composable
private fun PrivacySettings(visibility: Boolean, onChangePrivacy: (String) -> Unit) {

    val privacyItem = VideoPrivacy.fromId(visibility)
    var selectedOption by remember { mutableStateOf(privacyItem.label) }
    val pub = VideoPrivacy.PUBLIC.label
    val pvt = VideoPrivacy.PRIVATE.label

    Column(
        modifier = Modifier
            .background(OptionDarkBg, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        PrivacyOption(
            text = pub,
            description = "Show publicly on your Property Listing",
            isSelected = selectedOption == pub,
            onOptionSelected = {
                selectedOption = pub
                onChangePrivacy(selectedOption)
            }
        )
        SpacerVertical(size = 16.dp)
        PrivacyOption(
            text = pvt,
            description = "Keep as public unlisted and share the video link privately",
            isSelected = selectedOption == pvt,
            onOptionSelected = {
                selectedOption = pvt
                onChangePrivacy(selectedOption)
            }
        )
    }
}

@Composable
private fun PrivacyOption(
    text: String,
    description: String,
    isSelected: Boolean,
    onOptionSelected: () -> Unit
) {
    val privacyConfig = VideoPrivacy.fromLabel(text)

    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onOptionSelected() }
    ) {

        //Radio button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            //privacy radio button
            val radioIcon = if (isSelected)
                R.drawable.radio_active else R.drawable.radio_inactive
            Image(
                painter = painterResource(id = radioIcon),
                contentDescription = null
            )
            SpacerHorizontal(size = 8.dp)

            Column {
                //box privacy name
                Box(
                    modifier = Modifier
                        .background(privacyConfig.bgColor, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text800_12sp(label = text)
                }
            }
        }

        //info
        SpacerVertical(size = 8.dp)
        Text400_14sp(info = description, modifier = Modifier.padding(start = 24.dp))
    }
}

@Composable
fun NoDataView(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextProgress(title = "No Valid Information")
    }
}

@Preview
@Composable
private fun PreviewVidManage() {
    //MainContent(VideoItem(), onSaveClick = {}, onBackClick = {}, onPlayClick = {})
}
