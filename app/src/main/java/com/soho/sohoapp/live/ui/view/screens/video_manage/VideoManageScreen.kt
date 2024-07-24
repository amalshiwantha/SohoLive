package com.soho.sohoapp.live.ui.view.screens.video_manage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.VideoPrivacy
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.ui.components.SpacerHorizontal
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text800_12sp
import com.soho.sohoapp.live.ui.components.TopAppBarCustomClose
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppPrimaryDark
import com.soho.sohoapp.live.ui.theme.OptionDarkBg

@Composable
fun VideoManageScreen(
    goLiveData: GoLiveSubmit,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    MainContent(onBackClick = { navController.popBackStack() })
}

@Composable
fun MainContent(onBackClick: () -> Unit = {}) {
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
        ) { item { InnerContent() } }

    }
}

@Composable
fun InnerContent() {
    Column {
        PrivacySettings()
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
fun NoDataView(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
            .padding(innerPadding)
    ) {
        Text400_14sp(info = "No Video Data", color = AppPrimaryDark)
    }
}

@Preview
@Composable
private fun PreviewVidManage() {
    MainContent()
}
