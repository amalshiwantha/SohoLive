package com.soho.sohoapp.live.ui.view.screens.golive_success

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhite
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen

@Composable
fun GoLiveOkScreen(
    goLiveData: GoLiveSubmit,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
            .padding(
                top = WindowInsets.statusBars
                    .asPaddingValues()
                    .calculateTopPadding()
            )
    ) {
        CenterImgText(modifier, goLiveData)
        BottomButtons(modifier.align(alignment = Alignment.BottomCenter), navController)
    }
}

@Composable
fun BottomButtons(modifier: Modifier, navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ButtonOutlineWhite(
            text = "Manage Scheduled livecasts",
            onBtnClick = { navController.popBackStack() })

        ButtonColoured(text = "Schedule Another livecast",
            color = AppGreen,
            onBtnClick = { navController.popBackStack() })
    }
}

@Composable
fun CenterImgText(modifier: Modifier, goLiveData: GoLiveSubmit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.success_calender),
            contentDescription = null,
            modifier = Modifier.wrapContentSize()
        )

        SpacerVertical(size = 40.dp)

        val countSlots = goLiveData.scheduleSlots.size
        val title = pluralStringResource(R.plurals.livecast,countSlots,countSlots)

        Text(
            text = title,
            fontSize = 20.sp,
            lineHeight = 28.sp,
            fontFamily = FontFamily(Font(R.font.axiforma)),
            fontWeight = FontWeight(950),
            color = Color(0xFFFFFFFF),
            textAlign = TextAlign.Center,
            letterSpacing = 0.24.sp,
        )

        SpacerVertical(size = 8.dp)

        Text(
            text = "We will promote your scheduled livecasts to prospective buyers or renters and we will notify you when it’s time to go live!",
            fontSize = 14.sp,
            lineHeight = 19.6.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFFFFFFFF),
            textAlign = TextAlign.Center,
            letterSpacing = 0.17.sp,
        )
    }
}

@Composable
fun CenterImage() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.success_calender),
            contentDescription = null,
            modifier = Modifier.wrapContentSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGoLiveOkScreen() {
    GoLiveOkScreen(
        navController = NavHostController(LocalContext.current),
        goLiveData = GoLiveSubmit()
    )
}