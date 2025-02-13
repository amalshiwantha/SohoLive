package com.soho.sohoapp.live.ui.view.screens.forget_pw

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.TopAppBarCustomClose
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.BgGradientPurpleLight

@Composable
fun ForgetPwSentScreen(
    modifier: Modifier = Modifier, navController: NavHostController
) {
    val scrollState = rememberScrollState()

    Scaffold(containerColor = BgGradientPurpleLight, modifier = modifier.fillMaxSize(), topBar = {
        TopAppBarCustomClose(title = "",
            rightIcon = R.drawable.circle_close,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 38.dp),
            onCloseClick = {
                navController.popBackStack()
            })

    }) { innerPadding ->

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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                        .verticalScroll(scrollState), verticalArrangement = Arrangement.Top
                ) {
                    Content()
                }

                SpacerUp(24.dp)
                BtnOpenEmailBtn(modifier, onSendClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("mailto:") // This will open Gmail or any email client
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Add this flag to avoid the exception
                    }

                    // Try to open Gmail directly by checking the package name
                    val packageManager = context.packageManager
                    val gmailIntent = packageManager.getLaunchIntentForPackage("com.google.android.gm")

                    if (gmailIntent != null) {
                        // If Gmail is installed, launch it
                        context.startActivity(gmailIntent)
                    } else {
                        // Fallback to opening the Play Store page if Gmail is not installed
                        val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gm"))
                        playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Add the flag here
                        context.startActivity(playStoreIntent)
                    }
                    navController.popBackStack()
                })
            }
        }
    }
}


@Composable
private fun Content() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.fpw_sent), contentDescription = "")
        SpacerUp(40.dp)
        Text950_20sp(
            title = "Click on the link we just emailed you to access your account",
            modifier = Modifier.fillMaxWidth(),
            isCenter = true
        )
    }
}

@Composable
private fun BtnOpenEmailBtn(modifier: Modifier, onSendClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        ButtonColoured(text = "Open Email", color = AppGreen, onBtnClick = { onSendClick() })
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    ForgetPwSentScreen(navController = NavHostController(LocalContext.current))
}