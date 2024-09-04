package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
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

@Composable
fun CopyToClipboard(textToCopy: String) {
    LocalClipboardManager.current.setText(AnnotatedString(textToCopy))
}

@Composable
fun SpacerUp(size: Dp) {
    Spacer(modifier = Modifier.height(size))
}

@Composable
fun SpacerSide(size: Dp) {
    Spacer(modifier = Modifier.width(size))
}