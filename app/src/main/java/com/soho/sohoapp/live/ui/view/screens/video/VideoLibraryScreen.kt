package com.soho.sohoapp.live.ui.view.screens.video

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.PropertyType
import com.soho.sohoapp.live.enums.PropertyVisibility
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.model.VideoAnalytics
import com.soho.sohoapp.live.model.VideoItem
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhite
import com.soho.sohoapp.live.ui.components.SpacerHorizontal
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_12sp
import com.soho.sohoapp.live.ui.components.Text700_12spRight
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.TextBadge
import com.soho.sohoapp.live.ui.components.TextBadgeDuration
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppPrimaryDark
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

@Composable
fun VideoLibraryScreen(
    goLiveData: GoLiveSubmit,
    navController: NavController,
    vmVidLib: VideoLibraryViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject(),
) {
    Content()
}

@Composable
private fun Content() {
    Column(
        modifier = Modifier
            .background(brushMainGradientBg)
            .fillMaxSize()
    ) {
        val dataList = sampleData()

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(dataList) { item ->
                ListItemView(item)
            }
        }
    }
}

@Composable
private fun ListItemView(item: VideoItem) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {

        //badge time date
        Row(verticalAlignment = Alignment.CenterVertically) {

            item.propertyType?.let {
                TextBadge(text = it.uppercase(), bgColor = PropertyType.fromString(it).bgColor)
                Spacer(modifier = Modifier.width(8.dp))
            }

            val visiItem = PropertyVisibility.fromString(item.visibility)
            TextBadge(text = visiItem.label, bgColor = visiItem.bgColor)
            Spacer(modifier = Modifier.width(8.dp))

            item.duration?.let {
                TextBadgeDuration(text = it)
            }

            item.date?.let {
                Spacer(modifier = Modifier.weight(1f))
                Text700_12spRight(label = it, txtColor = AppWhite)
            }
        }
        SpacerVertical(size = 16.dp)

        //image title and info
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(88.dp)) {

            PropertyImageCenterPlay()
            SpacerHorizontal(size = 16.dp)
            TitleDescription()
        }
        SpacerVertical(size = 16.dp)

        //bottom action button list
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonOutlineWhite(
                text = "Manage",
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                onBtnClick = {})
            SpacerHorizontal(size = 8.dp)

            //download btn
            ActionIconButton(R.drawable.ic_download_bold, onClickAction = {})
            SpacerHorizontal(size = 8.dp)

            //chart btn
            ActionIconButton(R.drawable.ic_chart, onClickAction = {})
            SpacerHorizontal(size = 8.dp)

            //share btn
            ActionIconButton(R.drawable.ic_share, onClickAction = {})
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
fun TitleDescription() {
    Column {
        Text700_14sp(step = "1002/6 Little Hay Street, Sydney NSW 2000")
        SpacerVertical(size = 8.dp)
        Text400_12sp(label = "Live Auction in 1002/6 Little Hay Street, Sydney NSW 2000, 4/11/17 - 11:00")
    }
}

@Composable
fun PropertyImageCenterPlay() {
    Box(
        modifier = Modifier
            .size(88.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = rememberImagePainter(data = "https://cdn.britannica.com/05/157305-004-53D5D212.jpg"),
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

private fun sampleData(): List<VideoItem> {
    val myItem = VideoItem(
        propertyType = "action",
        visibility = 1,
        duration = "22:31",
        date = "22 may 2024",
        title = "1002/6 Little Hay Street, Sydney NSW 2000",
        info = "Live Auction in 1002/6 Little Hay Street, Sydney NSW 2000, 4/11/17 - 11:00",
        imageUrl = "https://cdn.britannica.com/05/157305-004-53D5D212.jpg",
        analytics = VideoAnalytics(
            fb = 10,
            yt = 20,
            li = 30,
            soho = 40,
            play_min = 120
        ),
        shareableLink = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
        downloadLink = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
    )
    val myTestItem = VideoItem(
        propertyType = "inspection",
        visibility = 0,
        imageUrl =
        "https://www.investopedia.com/thmb/bfHtdFUQrl7jJ_z-utfh8w1TMNA=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/houses_and_land-5bfc3326c9e77c0051812eb3.jpg",
        duration = "11:31",
        date = "11 may 2022",
        title = "11/6 Little Hay Street, Sydney NSW 1111",
        info = "11 Live Auction in 1002/6 Little Hay Street, ",
        analytics = VideoAnalytics(
            fb = 1,
            yt = 2,
            li = 3,
            soho = 3,
            play_min = 40
        ),
        shareableLink = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
        downloadLink = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
    )

    return List(50) { index ->
        if (index == 1) {
            myTestItem
        } else {
            myItem
        }

    }
}

@Preview
@Composable
private fun PreviewVidLib() {
    Content()
}