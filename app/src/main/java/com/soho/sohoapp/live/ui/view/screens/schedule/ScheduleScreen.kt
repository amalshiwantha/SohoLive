package com.soho.sohoapp.live.ui.view.screens.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.FieldConfig
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.ButtonOutLinedIcon
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text950_16sp
import com.soho.sohoapp.live.ui.components.TextFieldWhiteIcon
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.HintGray

@Composable
fun ScheduleScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.date_time_title),
                onBackClick = { navController.popBackStack() })
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

                //Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text700_14sp(step = "Choose Date & Time")
                    SpacerVertical(size = 8.dp)

                    TextFieldWhiteIcon(
                        fieldConfig = FieldConfig.DONE.apply {
                            placeholder = ""
                            keyboardType = KeyboardType.Text
                            trailingIcon = Icons.Filled.ArrowDropDown
                            clickable = true
                        },
                        onTextChange = {}, onClick = {})
                    SpacerVertical(size = 24.dp)

                    ButtonOutLinedIcon(
                        text = "Add to Schedule",
                        icon = R.drawable.ic_add,
                        onBtnClick = {
                        })
                    SpacerVertical(size = 40.dp)

                    Text950_16sp(title = "Your Scheduled Livestreams")
                    SpacerVertical(size = 16.dp)

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text400_14sp(
                            info = "No scheduled livecast streams yet",
                            color = HintGray,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun ScheduleScreenPreview() {
    ScheduleScreen(navController = NavHostController(LocalContext.current))
}