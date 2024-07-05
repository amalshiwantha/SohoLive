package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.soho.sohoapp.live.ui.theme.AppGreen

@Composable
fun CenterMessageProgress(modifier: Modifier = Modifier, color: Color = AppGreen, message: String) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(modifier = modifier, color = color)
            SpacerVertical(size = 8.dp)
            TextProgress(title = message)
        }
    }
}