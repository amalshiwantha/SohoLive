package com.soho.sohoapp.live.ui.view.screens.video

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.view.screens.schedule.ScheduleViewModel
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

@Composable
fun VideoLibraryScreen(
    goLiveData: GoLiveSubmit,
    navController: NavController,
    viewMSchedule: ScheduleViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject(),
) {

    Column {
        Text400_14sp(info = "This is Video Library", color = AppGreen)
    }

}