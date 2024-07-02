package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.theme.StarYellow

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
        textAlign = TextAlign.Left,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun TextWhite12(modifier: Modifier = Modifier, title: String, txtColor: Color = AppWhite) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(800),
        color = txtColor,
        textAlign = TextAlign.Center,
        letterSpacing = 0.14.sp
    )
}

@Composable
fun TextWhite14Left(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(800),
        color = AppWhite,
        textAlign = TextAlign.Left,
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

@Composable
fun Text950_20sp(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(950),
        color = AppWhite,
        letterSpacing = 0.24.sp
    )
}

@Composable
fun Text950_16sp(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = 20.sp,
        lineHeight = 25.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(950),
        color = AppWhite,
        letterSpacing = 0.19.sp
    )
}

@Composable
fun Text700_14sp(
    modifier: Modifier = Modifier,
    step: String,
    isCenter: Boolean = false,
    color: Color = AppWhite
) {
    Text(
        textAlign = if (isCenter) TextAlign.Center else TextAlign.Left,
        modifier = modifier,
        text = step,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
        fontWeight = FontWeight(700),
        color = color,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun Text700_16sp(
    modifier: Modifier = Modifier,
    title: String,
    isCenter: Boolean = false,
    color: Color = AppWhite
) {
    Text(
        textAlign = if (isCenter) TextAlign.Center else TextAlign.Left,
        modifier = modifier,
        text = title,
        fontSize = 16.sp,
        lineHeight = 25.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(700),
        color = color,
        letterSpacing = 0.19.sp
    )
}

@Composable
fun Text700_14spBold(modifier: Modifier = Modifier, step: String, txtColor: Color = AppWhite) {
    Text(
        modifier = modifier,
        text = step,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(700),
        color = txtColor,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun TextStarRating(modifier: Modifier = Modifier, rate: String) {
    Text(
        modifier = modifier,
        text = rate,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
        fontWeight = FontWeight(700),
        color = StarYellow,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun Text400_14sp(modifier: Modifier = Modifier, info: String, color: Color = AppWhite) {
    Text(
        modifier = modifier,
        text = info,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
        fontWeight = FontWeight(400),
        color = color,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun TextPlaceHolder(modifier: Modifier = Modifier, label: String) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
        fontWeight = FontWeight(400),
        color = HintGray
    )
}

@Composable
fun Text700_12sp(modifier: Modifier = Modifier, label: String) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        letterSpacing = 0.14.sp,
        color = Color(0xFF00BFA8),
        fontWeight = FontWeight(700),
        fontFamily = FontFamily(Font(R.font.axiforma)),
    )
}

@Composable
fun Text400_12sp(modifier: Modifier = Modifier, label: String, txtColor: Color = AppWhite) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        letterSpacing = 0.14.sp,
        color = txtColor,
        fontWeight = FontWeight(400),
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
    )
}

@Composable
fun Text800_12sp(modifier: Modifier = Modifier, label: String) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        letterSpacing = 0.14.sp,
        color = Color(0xFFFFFFFF),
        fontWeight = FontWeight(800),
        fontFamily = FontFamily(Font(R.font.axiforma)),
    )
}

@Composable
fun Text800_14sp(modifier: Modifier = Modifier, label: String) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        letterSpacing = 0.17.sp,
        color = Color(0xFFFFFFFF),
        fontWeight = FontWeight(800),
        fontFamily = FontFamily(Font(R.font.axiforma)),
    )
}

@Composable
fun Text800_20sp(modifier: Modifier = Modifier, label: String) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 20.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.19.sp,
        color = Color(0xFFFFFFFF),
        fontWeight = FontWeight(800),
        fontFamily = FontFamily(Font(R.font.axiforma)),
    )
}

@Composable
fun TextSwipeSelection(modifier: Modifier, title: String, textColor: Color) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            fontSize = 12.sp,
            lineHeight = 16.8.sp,
            fontFamily = FontFamily(Font(R.font.axiforma)),
            fontWeight = FontWeight(800),
            color = textColor,
            textAlign = TextAlign.Center,
            letterSpacing = 0.14.sp
        )
    }
}


@Preview
@Composable
private fun PreViewTextError() {
    Column {
        Text950_20sp(title = "This is Title")
        TextWhite14(title = "This is error message")
        TextLabelWhite14(label = "Label")
        TextSubtitleWhite14(label = "Label")
        TextBlue14(label = "Label")
        TextError(errorMsg = "This is error message")
    }
}