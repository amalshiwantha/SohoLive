package com.soho.sohoapp.live.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.view.nav.NavigationPath
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val isSplashVisible = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(5000)
        isSplashVisible.value = false

        // Navigate to the HomeScreen
        navController.navigate(NavigationPath.HOME.name)
    }

    if (isSplashVisible.value) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2B0235),
                            Color(0xFF32003E)
                        )
                    )
                ),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.soho_white),
                    contentDescription = null,
                    modifier = Modifier.wrapContentSize()
                )
                Image(
                    painter = painterResource(id = R.drawable.live_icon),
                    contentDescription = null,
                    modifier = Modifier.wrapContentSize()
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSplash() {
    SplashScreen(navController = NavHostController(LocalContext.current))
}