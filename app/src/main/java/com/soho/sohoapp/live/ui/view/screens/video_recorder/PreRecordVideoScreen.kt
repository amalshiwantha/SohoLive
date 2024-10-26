package com.soho.sohoapp.live.ui.view.screens.video_recorder

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text700_14spBold
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import java.io.File

@Composable
fun PreRecordScreen(navController: NavHostController) {
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
                MainContent(onPlay = {
                    navController.navigate("${NavigationPath.PLAYER.name}/${Uri.encode(it.toString())}")
                })
            }
        }
    }
}

@Composable
fun MainContent(onPlay: (Uri) -> Unit) {
    val videoFiles = remember { getAllRecordedVideos() }
    Column {
        if (videoFiles.isEmpty()) {
            Text(
                text = "No videos recorded",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(videoFiles) { file ->
                    VideoFileItem(file, onPlay = {
                        onPlay(it)
                    })
                }
            }
        }
    }
}

@Composable
fun VideoFileItem(file: File, onPlay: (Uri) -> Unit) {
    val thumbnail = remember { getVideoThumbnail(file) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {
            onPlay(Uri.fromFile(file))
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
            Text700_14sp(step = file.name)
            Text700_14spBold(step = "Size: ${file.length() / (1024 * 1024)} MB")
        }
    }
}

fun getAllRecordedVideos(): List<File> {
    val movieDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
    val customDir = File(movieDir, "SohoPreRecord")
    return if (customDir.exists()) {
        customDir.listFiles()?.filter { it.extension == "mp4" } ?: emptyList()
    } else {
        emptyList()
    }
}

fun getVideoThumbnail(file: File): Bitmap? {
    return ThumbnailUtils.createVideoThumbnail(file.path, MediaStore.Images.Thumbnails.MINI_KIND)
}

