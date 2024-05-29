package com.soho.sohoapp.live.view.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import com.soho.sohoapp.live.view.ui.theme.BgGradientPurpleDark
import com.soho.sohoapp.live.view.ui.theme.BgGradientPurpleLight

val brushMainGradientBg = Brush.verticalGradient(
    colors = listOf(BgGradientPurpleLight, BgGradientPurpleDark)
)

@Composable
fun SpacerVertical(size: Dp) {
    Spacer(modifier = Modifier.height(size))
}

@Composable
fun SpacerHorizontal(size: Dp) {
    Spacer(modifier = Modifier.width(size))
}