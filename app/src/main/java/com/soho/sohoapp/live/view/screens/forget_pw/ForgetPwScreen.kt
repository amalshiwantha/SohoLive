package com.soho.sohoapp.live.view.screens.forget_pw

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.view.ui.components.AppTopBar
import com.soho.sohoapp.live.view.ui.components.ButtonColoured
import com.soho.sohoapp.live.view.ui.components.SpacerVertical
import com.soho.sohoapp.live.view.ui.components.TextLabelWhite14
import com.soho.sohoapp.live.view.ui.components.TextSubtitleWhite14
import com.soho.sohoapp.live.view.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.view.ui.navigation.NavigationPath
import com.soho.sohoapp.live.view.ui.theme.AppGreen

@Composable
fun ForgetPwScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.forget_pw_title),
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.Top
                ) {
                    LoginForm()
                }

                SpacerVertical(24.dp)
                BottomLoginBtn(modifier, navController)
            }
        }
    }
}


@Composable
private fun LoginForm() {
    Column {

        TextSubtitleWhite14(label = stringResource(R.string.forgot_pw_msg))

        SpacerVertical(24.dp)

        TextLabelWhite14(label = stringResource(R.string.email))
        SpacerVertical(8.dp)
        //InputWhite(txtEml)

        SpacerVertical(24.dp)
    }
}

@Composable
private fun BottomLoginBtn(modifier: Modifier, navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        ButtonColoured(text = stringResource(R.string.forget_pw_link),
            color = AppGreen,
            onBtnClick = {
                navController.navigate(NavigationPath.HOME.name) {
                    popUpTo(NavigationPath.SIGNIN.name) { inclusive = true }
                    popUpTo(NavigationPath.PRE_ACCESS.name) { inclusive = true }
                }
            })
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    ForgetPwScreen(navController = NavHostController(LocalContext.current))
}