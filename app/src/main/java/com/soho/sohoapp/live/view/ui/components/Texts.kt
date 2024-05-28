package com.soho.sohoapp.live.view.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.soho.sohoapp.live.R

@Composable
fun TextWhite14(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(800),
        color = Color(0xFFFFFFFF),
        textAlign = TextAlign.Center,
        letterSpacing = 0.17.sp
    )
}