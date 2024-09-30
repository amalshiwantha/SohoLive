package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.DurationDark
import com.soho.sohoapp.live.ui.theme.ErrorRed
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.theme.StarYellow
import com.soho.sohoapp.live.ui.theme.logoutRed
import com.soho.sohoapp.live.ui.theme.lowGreen

@Composable
fun TextTopBarTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(950),
        color = AppWhite,
        letterSpacing = 0.24.sp,
    )
}

@Composable
fun TextWhite14Normal(modifier: Modifier = Modifier, title: String, txtColor: Color = AppWhite) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
        fontWeight = FontWeight(800),
        color = txtColor,
        textAlign = TextAlign.Left,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun TextWhite14(modifier: Modifier = Modifier, title: String, txtColor: Color = AppWhite) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(800),
        color = txtColor,
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
        color = ErrorRed,
        lineHeight = 19.6.sp,
        letterSpacing = 0.17.sp,
        fontSize = 12.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(400),
        modifier = modifier.padding(top = 4.dp)
    )
}

@Composable
fun Text950_20sp(modifier: Modifier = Modifier, title: String, txtColor: Color = AppWhite) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(950),
        color = txtColor,
        letterSpacing = 0.24.sp
    )
}

@Composable
fun Text950_20spCenter(modifier: Modifier = Modifier, title: String, txtColor: Color = AppWhite) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(950),
        color = txtColor,
        textAlign = TextAlign.Center,
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

/*@Composable
fun Text700_12sp(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        text = title,
        fontSize = 12.sp,
        lineHeight = 11.2.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(700),
        color = Color(0xFFFFFFFF),
        letterSpacing = 0.28.sp,
        modifier = modifier
    )
}*/

@Composable
fun Text700_14spLink(
    name: String,
    color: Color = AppWhite,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Text(
        textAlign = TextAlign.Left,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        text = name,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(700),
        color = color,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun Text700_14spProperty(
    modifier: Modifier = Modifier,
    step: String,
    color: Color = AppWhite,
) {
    val fontId = R.font.axiforma_regular

    Text(
        textAlign = TextAlign.Left,
        modifier = modifier,
        text = step,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(fontId)),
        fontWeight = FontWeight(700),
        color = color,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun Text700_14sp(
    modifier: Modifier = Modifier,
    step: String,
    isCenter: Boolean = false,
    color: Color = AppWhite,
    isBold: Boolean = true
) {
    val fontId = if (isBold) R.font.axiforma else R.font.axiforma_regular

    Text(
        textAlign = if (isCenter) TextAlign.Center else TextAlign.Left,
        modifier = modifier,
        text = step,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(fontId)),
        fontWeight = FontWeight(if (isBold) 700 else 400),
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
        maxLines = 2,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(700),
        color = color,
        letterSpacing = 0.19.sp
    )
}

@Composable
fun TextProgress(
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
        maxLines = 2,
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
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
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
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
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(700),
        color = StarYellow,
        letterSpacing = 0.17.sp
    )
}

@Composable
fun Text400_14sp(
    modifier: Modifier = Modifier,
    info: String,
    color: Color = AppWhite,
    txtAlign: TextAlign = TextAlign.Left
) {
    Text(
        modifier = modifier,
        text = info,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
        fontWeight = FontWeight(400),
        color = color,
        letterSpacing = 0.17.sp,
        textAlign = txtAlign
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

fun inputStyleSearch(): TextStyle {
    return TextStyle(
        color = Color.Black,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
        fontWeight = FontWeight(400)
    )
}

@Composable
fun Text700_12spNormal(modifier: Modifier = Modifier, label: String, txtColor: Color = lowGreen) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        letterSpacing = 0.14.sp,
        color = txtColor,
        fontWeight = FontWeight(700),
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
    )
}

@Composable
fun Text700_12sp(modifier: Modifier = Modifier, label: String, txtColor: Color = lowGreen) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        letterSpacing = 0.14.sp,
        color = txtColor,
        fontWeight = FontWeight(700),
        fontFamily = FontFamily(Font(R.font.axiforma)),
    )
}

@Composable
fun Text700_12spRight(modifier: Modifier = Modifier, label: String, txtColor: Color) {
    Text(
        modifier = modifier,
        text = label,
        color = txtColor,
        fontWeight = FontWeight(700),
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        textAlign = TextAlign.Right,
        letterSpacing = 0.14.sp,
    )
}

@Composable
fun Text400_10sp(modifier: Modifier = Modifier, label: String, txtColor: Color = AppWhite) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 10.sp,
        lineHeight = 12.8.sp,
        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
        fontWeight = FontWeight(400),
        color = txtColor,
        letterSpacing = 0.19.sp
    )
}

@Composable
fun Text400_12sp(modifier: Modifier = Modifier, label: String, txtColor: Color = AppWhite) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        maxLines = 2,
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
fun Text800_14sp(
    modifier: Modifier = Modifier,
    label: String,
    txtColor: Color = AppWhite,
    txtAlign: TextAlign = TextAlign.Left
) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        letterSpacing = 0.17.sp,
        color = txtColor,
        fontWeight = FontWeight(800),
        fontFamily = FontFamily(Font(R.font.axiforma)),
        textAlign = txtAlign
    )
}

@Composable
fun Text800_12sp_right(
    modifier: Modifier = Modifier,
    label: String,
    txtColor: Color = logoutRed,
    txtAlign: TextAlign = TextAlign.Right
) {
    Text(
        modifier = modifier,
        text = label,
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        letterSpacing = 0.14.sp,
        color = txtColor,
        fontWeight = FontWeight(800),
        fontFamily = FontFamily(Font(R.font.axiforma)),
        textAlign = txtAlign
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

@Composable
fun TextIconSwipeSelection(modifier: Modifier, title: String, textColor: Color, icon: Int) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(textColor)
            )
            SpacerUp(size = 8.dp)
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
}

@Composable
fun TextBadge(text: String, bgColor: Color) {
    Text(
        fontSize = 10.sp,
        lineHeight = 14.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(800),
        textAlign = TextAlign.Center,
        letterSpacing = 0.9.sp,
        text = text,
        color = AppWhite,
        modifier = Modifier
            .background(bgColor, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
}

/*@Composable
fun TextBadgeDuration(text: String) {
    Text(
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        fontFamily = FontFamily(Font(R.font.axiforma)),
        fontWeight = FontWeight(700),
        textAlign = TextAlign.Center,
        letterSpacing = 0.9.sp,
        text = text,
        color = AppWhite,
        modifier = Modifier
            .background(DurationDark, shape = RoundedCornerShape(12.dp))
            .padding(8.dp)
    )
}*/

@Preview
@Composable
private fun PreViewTextError() {
    Column {
        //TextBadgeDuration(text = "44:21")
        TextBadge(text = "PUBLIC", AppGreen)
        Text950_20sp(title = "This is Title")
        TextWhite14(title = "This is error message")
        TextLabelWhite14(label = "Label")
        TextSubtitleWhite14(label = "Label")
        TextBlue14(label = "Label")
        TextError(errorMsg = "This is error message")
    }
}