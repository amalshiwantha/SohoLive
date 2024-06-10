package com.soho.sohoapp.live.ui.view.screens.golive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.SearchBar
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.NextButtonBg
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

@Composable
fun GoLiveScreen(
    navController: NavController,
    goLiveVm: GoLiveViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject()
) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        val (content, submitButton) = createRefs()

        //top content
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(content) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(submitButton.top)
                    height = Dimension.fillToConstraints
                }
        ) {
            StepCountTitleInfo()
            SpacerVertical(40.dp)
            SearchBar()
            SpacerVertical(16.dp)
            ScrollContent()
        }

        // Fixed Next Button
        Box(
            modifier = Modifier
                .constrainAs(submitButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .background(NextButtonBg)
                .padding(16.dp)
        ) {
            ButtonColoured(text = "Next", color = AppGreen) {
            }
        }

    }
}

@Composable
fun ScrollContent() {
    LazyColumn(modifier = Modifier) {
        items(10) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.LightGray)
                    .padding(16.dp)
            ) {
                Text(text = "Item #$index")
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
