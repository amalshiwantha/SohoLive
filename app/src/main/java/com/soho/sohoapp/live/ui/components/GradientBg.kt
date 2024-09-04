package com.soho.sohoapp.live.ui.components

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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


val brushLiveGradientBg = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFEC3A44), // Start color (Red)
        Color(0xFF793AAB),  // End color (Purple)
        Color(0xFF793AAB)  // End color (Purple)
    )
)

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
