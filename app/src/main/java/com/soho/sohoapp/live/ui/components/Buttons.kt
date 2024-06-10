package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ButtonColoured(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    isBackButton: Boolean = false,
    onBtnClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = { onBtnClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        if (isBackButton) {
            TextWhite14Left(title = text)
        } else {
            TextWhite14(title = text)
        }
    }
}

@Composable
fun ButtonOutlineWhite(text: String, onBtnClick: () -> Unit) {
    OutlinedButton(
        onClick = { onBtnClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        border = BorderStroke(2.dp, Color.White),
        shape = RoundedCornerShape(16.dp),
    ) {
        TextWhite14(title = text)
    }
}

@Composable
fun TextButtonBlue(text: String, modifier: Modifier = Modifier, onBtnClick: () -> Unit) {
    TextButton(onClick = { onBtnClick() }, modifier = modifier) {
        TextBlue14(label = text, modifier = modifier)
    }
}

@Preview
@Composable
private fun TextButtonBluePrev() {
    TextButtonBlue(text = "Signup", onBtnClick = { /* Handle button click */ })
}

@Preview
@Composable
private fun CustomButtonPrev() {
    ButtonOutlineWhite(text = "Signup", onBtnClick = { /* Handle button click */ })
}

@Preview
@Composable
private fun ColouredButtonPrev() {
    ButtonColoured(text = "Login",
        color = Color.Red,
        onBtnClick = { /* Handle button click */ })
}