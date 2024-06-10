package com.soho.sohoapp.live.ui.view.screens.golive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.test.bottom
import androidx.compose.ui.test.top
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.SearchBar
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.components.brushMainGradientTransBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

@Composable
fun GoLiveScreen(
    navController: NavController,
    goLiveVm: GoLiveViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        ScreenContent()
        BottomNextButton()
    }
}

/*@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        val (topBar, content, submitButton) = FocusRequester.createRefs()

        // Top action bar
        TopAppBar(
            title = { Text("Home") },
            modifier = Modifier
                .constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        // Middle content list
        LazyColumn(
            modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(submitButton.top)
                }
        ) {
            // Add your content list items here
        }

        // Submit button
        Button(
            onClick = { *//* Handle submit button click *//* },
            modifier = Modifier
                .constrainAs(submitButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text("Submit")
        }
    }
}*/


@Composable
fun BottomNextButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brushMainGradientTransBg)
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        ButtonColoured(text = "Next", color = AppGreen) {

        }
    }
}

@Composable
private fun ScreenContent() {
    Column(
        modifier = Modifier.padding(
            horizontal = 16.dp, vertical = 24.dp
        )
    ) {
        StepCountTitleInfo()
        SpacerVertical(40.dp)
        SearchBar()
        SpacerVertical(16.dp)
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
