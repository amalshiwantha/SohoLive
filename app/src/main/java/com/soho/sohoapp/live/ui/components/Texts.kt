package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.theme.AppWhite

@Composable
fun TextWhite14(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(800),
        color = AppWhite,
        textAlign = TextAlign.Center,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun TextLabelWhite14(modifier: Modifier = Modifier, label: String) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(700),
        color = AppWhite,
        textAlign = TextAlign.Center,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun TextSubtitleWhite14(modifier: Modifier = Modifier, label: String) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
        fontWeight = FontWeight(400),
        color = AppWhite,
        letterSpacing = 0.17.sp,
    )
}

@Composable
fun TextBlue14(modifier: Modifier = Modifier, label: String) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(700),
        color = Color(0xFF0DAEE1),
        textAlign = TextAlign.Center,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun TextError(modifier: Modifier = Modifier, errorMsg: String) {
    Text(
        text = errorMsg,
        color = Color(0xFFEE21C1),
        lineHeight = 19.6.sp,
        letterSpacing = 0.17.sp,
        fontSize = 12.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(400),
        modifier = modifier.padding(top = 4.dp)
    )
}
