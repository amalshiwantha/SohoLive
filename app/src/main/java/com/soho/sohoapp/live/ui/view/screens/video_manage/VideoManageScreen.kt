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
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.PropertyItem
import com.soho.sohoapp.live.model.VideoItem
import com.soho.sohoapp.live.network.response.Document
import com.soho.sohoapp.live.ui.components.SpacerHorizontal
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_12sp
import com.soho.sohoapp.live.ui.components.Text700_8sp
import com.soho.sohoapp.live.ui.components.Text800_12sp
import com.soho.sohoapp.live.ui.components.Text950_16sp
import com.soho.sohoapp.live.ui.components.TopAppBarCustomClose
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.OptionDarkBg
import com.soho.sohoapp.live.ui.view.screens.golive.PropertyItemContent

@Composable
fun VideoManageScreen(
    mLiveData: GoLiveSubmit,
    navController: NavHostController,
) {
    mLiveData.videoItemState.value?.let {
        MainContent(data = it, onBackClick = { navController.popBackStack() })
    } ?: run {
        NoDataView()
    }
}

@Composable
fun MainContent(data: VideoItem, onBackClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        //action bar
        TopAppBarCustomClose(
            title = "Manage Video",
            rightIcon = R.drawable.ic_cross,
            onCloseClick = {
                onBackClick()
            })

        //content
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                )
        ) { item { InnerContent(data) } }

    }
}

@Composable
private fun InnerContent(data: VideoItem) {
    Column {
        //privacy
        PrivacySettings()

        //property
        data.property?.let {
            SpacerVertical(size = 40.dp)
            PropertyView(it)
        }

        //video
        data.downloadLink?.let {
            SpacerVertical(size = 24.dp)
            VideoView()
        }
    }
}

@Composable
private fun VideoView() {
    Column {
        Text950_16sp(title = "Watch Video")
        SpacerVertical(size = 8.dp)
        VideoItemContent()
    }
}

@Composable
fun VideoItemContent() {
    PropertyCard()
}

@Composable
fun PropertyCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box {
            // Background Image
            Image(
                painter = rememberImagePainter(data = "https://www.investopedia.com/thmb/bfHtdFUQrl7jJ_z-utfh8w1TMNA=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/houses_and_land-5bfc3326c9e77c0051812eb3.jpg"),
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
                        label = "41:33", txtColor = AppWhite, modifier = Modifier
                            .background(
                                Color.Black.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    )

                    Text700_12sp(
                        label = "22 May 2024", txtColor = AppWhite, modifier = Modifier
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
                        //AmenitiesView(property, AppWhite, isCompact = true)
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
private fun PrivacySettings() {
    val pub = VideoPrivacy.PUBLIC.label
    val pvt = VideoPrivacy.PRIVATE.label

    var selectedOption by remember { mutableStateOf(pub) }

    Column(
        modifier = Modifier
            .background(OptionDarkBg, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        PrivacyOption(
            text = pub,
            description = "Show publicly on your Property Listing",
            isSelected = selectedOption == pub,
            onOptionSelected = { selectedOption = pub }
        )
        SpacerVertical(size = 16.dp)
        PrivacyOption(
            text = pvt,
            description = "Keep as public unlisted and share the video link privately",
            isSelected = selectedOption == pvt,
            onOptionSelected = { selectedOption = pvt }
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
                        .background(privacyConfig.bgColor, shape = RoundedCornerShape(4.dp))
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
fun NoDataView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        Text400_14sp(info = "No Video Data", color = Color.White)
    }
}

@Preview
@Composable
private fun PreviewVidManage() {
    //MainContent(VideoItem())
    VideoItemContent()
}
