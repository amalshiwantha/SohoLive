package com.soho.sohoapp.live.view.screens.signin

import androidx.compose.foundation.background
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
import com.soho.sohoapp.live.view.ui.components.InputWhite
import com.soho.sohoapp.live.view.ui.components.SpacerVertical
import com.soho.sohoapp.live.view.ui.components.TextBlue14
import com.soho.sohoapp.live.view.ui.components.TextLabelWhite14
import com.soho.sohoapp.live.view.ui.components.brushMainGradientBg

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.welcome_back),
                onBackClick = { navController.popBackStack() })
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .background(brushMainGradientBg)
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {

                TextLabelWhite14(label = stringResource(R.string.email))
                SpacerVertical(8.dp)
                InputWhite()

                SpacerVertical(24.dp)

                TextLabelWhite14(label = stringResource(R.string.password))
                SpacerVertical(8.dp)
                InputWhite()

                SpacerVertical(24.dp)

                TextBlue14(
                    label = stringResource(R.string.forgot_password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(CenterHorizontally)
                )
            }
        }
    )
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SignInScreen(navController = NavHostController(LocalContext.current))
}