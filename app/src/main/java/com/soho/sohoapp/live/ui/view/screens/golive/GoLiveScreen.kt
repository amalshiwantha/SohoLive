package com.soho.sohoapp.live.ui.view.screens.golive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.SearchBar
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.brushBottomGradientBg
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

@Composable
fun GoLiveScreen(
    navController: NavController,
    goLiveVm: GoLiveViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject()
) {
    Box(modifier = Modifier.background(brushMainGradientBg)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            StepCountTitleInfo()
            SpacerVertical(40.dp)
            SearchBar()
            SpacerVertical(16.dp)
            ScrollableContent()
        }

        FixedNextButton(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun FixedNextButton(modifier: Modifier) {
    Box(
        modifier = modifier.background(brushBottomGradientBg)
    ) {
        ButtonColoured(
            text = "Next", color = AppGreen,
            modifier = Modifier.padding(16.dp)
        ) {

        }
    }
}


@Composable
fun ScrollableContent() {
    LazyColumn {
        items(50) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.Gray)
                    .padding(16.dp)
            ) {
                Text700_14sp(step = "Item #$index")
            }
        }
    }
}


@Composable
fun StepCountTitleInfo() {
    Text700_14sp(step = "Step 1 of 4")
    SpacerVertical(8.dp)
    Text950_20sp(title = "Link livestream to your property")
    SpacerVertical(8.dp)
    Text400_14sp(info = "Prospect interested in your listing will be notified of your scheduled livestream and will be livecasted on your property listing page.")
}

@Preview
@Composable
private fun PreviewGoLiveScreen() {
    Box(modifier = Modifier.background(brushMainGradientBg)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            StepCountTitleInfo()
            SpacerVertical(40.dp)
            SearchBar()
            SpacerVertical(16.dp)
            ScrollableContent()
        }

        FixedNextButton(modifier = Modifier.align(Alignment.BottomCenter))
    }
}
