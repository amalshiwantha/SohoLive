package com.soho.sohoapp.live.view.screens.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.view.ui.components.AppTopBar
import com.soho.sohoapp.live.view.ui.components.TextWhite14
import com.soho.sohoapp.live.view.ui.components.brushMainGradientBg

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { AppTopBar(title = "Log In", onBackClick = { navController.popBackStack() }) },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .background(brushMainGradientBg)
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                repeat(50) { index ->
                    TextWhite14(title = "Item $index")
                }
            }
        }
    )
}