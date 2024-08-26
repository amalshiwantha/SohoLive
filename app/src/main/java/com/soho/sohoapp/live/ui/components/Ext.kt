package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import com.soho.sohoapp.live.ui.theme.BgGradientPurpleDark
import com.soho.sohoapp.live.ui.theme.BgGradientPurpleLight
import com.soho.sohoapp.live.ui.theme.LiveBtnGradientStart
import com.soho.sohoapp.live.ui.theme.LiveDateBtnGradientEnd
import com.soho.sohoapp.live.ui.theme.NextButtonBg
import com.soho.sohoapp.live.ui.theme.NextButtonBgTrans
import com.soho.sohoapp.live.ui.theme.PlanBtnGradientEnd
import com.soho.sohoapp.live.ui.theme.PlanBtnGradientStart
import com.soho.sohoapp.live.ui.theme.SetDateBtnGradientEnd
import com.soho.sohoapp.live.ui.theme.SetDateBtnGradientStart

val brushMainGradientBg = Brush.verticalGradient(
    colors = listOf(BgGradientPurpleLight, BgGradientPurpleDark)
)

val brushBottomGradientBg = Brush.verticalGradient(
    colors = listOf(NextButtonBgTrans, NextButtonBg)
)

val brushPlanBtnGradientBg = Brush.horizontalGradient(
    colors = listOf(PlanBtnGradientStart, PlanBtnGradientEnd)
)

val brushGradientSetDateTime = Brush.horizontalGradient(
    colors = listOf(SetDateBtnGradientStart, SetDateBtnGradientEnd)
)

val brushGradientLive = Brush.horizontalGradient(
    colors = listOf(LiveBtnGradientStart, LiveDateBtnGradientEnd)
)

@Composable
fun SpacerUp(size: Dp) {
    Spacer(modifier = Modifier.height(size))
}

@Composable
fun SpacerSide(size: Dp) {
    Spacer(modifier = Modifier.width(size))
}