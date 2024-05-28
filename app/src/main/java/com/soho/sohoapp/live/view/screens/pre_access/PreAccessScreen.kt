package com.soho.sohoapp.live.view.screens.pre_access

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.soho.sohoapp.live.view.ui.components.ColouredBtn
import com.soho.sohoapp.live.view.ui.components.OutlinedBtnWhite
import com.soho.sohoapp.live.view.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.view.ui.navigation.NavigationPath
import com.soho.sohoapp.live.view.ui.theme.AppGreen
import com.soho.sohoapp.live.view.ui.theme.AppWhite

@Composable
fun PreAccessScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brushMainGradientBg),
        contentAlignment = Alignment.Center,
    ) {
        CenterImgText(modifier)
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
        ColouredBtn(text = stringResource(R.string.log_in),
            color = AppGreen,
            onBtnClick = { navController.navigate(NavigationPath.SIGNIN.name) })

        OutlinedBtnWhite(
            text = stringResource(R.string.sign_up),
            onBtnClick = { navController.navigate(NavigationPath.SIGNUP.name) })
    }
}

@Composable
fun CenterImgText(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CenterImage()

        Text(
            text = stringResource(R.string.pre_access_msg),
            fontFamily = FontFamily(Font(R.font.axiforma)),
            fontWeight = FontWeight(950),
            color = AppWhite,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            lineHeight = 33.6.sp,
            letterSpacing = 0.28.sp
        )
    }
}

@Composable
fun CenterImage() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.pre_access_img),
            contentDescription = null,
            modifier = Modifier.wrapContentSize()
        )
        Image(
            painter = painterResource(id = R.drawable.girl_stand),
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .align(alignment = Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPreAccessScreen() {
    PreAccessScreen(navController = NavHostController(LocalContext.current))
}