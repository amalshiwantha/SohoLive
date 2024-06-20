package com.soho.sohoapp.live.ui.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.ui.theme.AppGreen

@Composable
fun CenterProgress(modifier: Modifier, color: Color = AppGreen) {
    CircularProgressIndicator(modifier = modifier, color = color)
}