package com.soho.sohoapp.live.ui.view.screens.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.view.screens.schedule.ScheduleViewModel
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

data class VideoItem(val id: Int, val name: String)

@Composable
fun VideoLibraryScreen(
    goLiveData: GoLiveSubmit,
    navController: NavController,
    viewMSchedule: ScheduleViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject(),
) {
    Column(
        modifier = Modifier
            .background(brushMainGradientBg)
            .fillMaxSize()
    ) {
        MainListView(sampleData())
    }
}

@Composable
fun MainListView(vidItems: List<VideoItem>) {
    LazyColumn(Modifier.fillMaxWidth().padding(16.dp)) {
        items(vidItems) { item ->
            ListItemView(item)
        }
    }
}

@Composable
fun ListItemView(item: VideoItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text700_14sp(step = item.name, color = AppGreen)
    }
}

fun sampleData(): List<VideoItem> {
    return List(50) { index -> VideoItem(index, "Item #$index") }
}