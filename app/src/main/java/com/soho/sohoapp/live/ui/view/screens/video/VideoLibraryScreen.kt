package com.soho.sohoapp.live.ui.view.screens.video

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

data class VideoItem(val id: Int, val name: String)

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
            Badge(text = "AUCTION", bgColor = Color(0xFFF9D881))
            Spacer(modifier = Modifier.width(8.dp))
            Badge(text = "PUBLIC", bgColor = Color(0xFF00B3A6))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "41:33",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .background(Color.Black, shape = RoundedCornerShape(8.dp))
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "22 May 2024",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        //image title and info
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberImagePainter(data = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQP_F72CpKKW2UA8BfcMF4Pw0L4wQ1Wgu203Q&s"),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "1002/6 Little Hay Street, Sydney NSW 2000",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Live Auction in 1002/6 Little Hay Street, Sydney NSW 2000, 4/11/17 – 11:00 ... More",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        //bottom action button list
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(text = "Manage", color = Color.White)
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download",
                    tint = Color.White,
                    modifier = Modifier.background(Color.Black, shape = CircleShape)
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Statistics",
                    tint = Color.White,
                    modifier = Modifier.background(Color.Black, shape = CircleShape)
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.background(Color.Black, shape = CircleShape)
                )
            }
        }
    }
}

@Composable
private fun Badge(text: String, bgColor: Color) {
    Text(
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(800),
        textAlign = TextAlign.Center,
        letterSpacing = 0.9.sp,
        text = text,
        color = AppWhite,
        modifier = Modifier
            .background(bgColor, shape = RoundedCornerShape(12.dp))
            .padding(8.dp)
    )
}

private fun sampleData(): List<VideoItem> {
    return List(50) { index -> VideoItem(index, "Item #$index") }
}

@Preview
@Composable
private fun PreviewVidLib() {
    Content()
}