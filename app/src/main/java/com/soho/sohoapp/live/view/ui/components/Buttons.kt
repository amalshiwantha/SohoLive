package com.soho.sohoapp.live.view.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColouredBtn(
    text: String,
    color: Color,
    onBtnClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = { onBtnClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        WhiteText_14sp(text)
    }
}

@Composable
fun OutlinedBtnWhite(text: String, onBtnClick: () -> Unit) {
    OutlinedButton(
        onClick = { onBtnClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        border = BorderStroke(2.dp, Color.White),
        shape = RoundedCornerShape(16.dp),
    ) {
        WhiteText_14sp(title = text)
    }
}

@Preview
@Composable
private fun CustomButtonPrev() {
    OutlinedBtnWhite(text = "Signup", onBtnClick = { /* Handle button click */ })
}

@Preview
@Composable
private fun ColouredButtonPrev() {
    ColouredBtn(text = "Login",
        color = Color.Red,
        onBtnClick = { /* Handle button click */ })
}