package com.soho.sohoapp.live.view.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InputWhite(onTextChange: (String) -> Unit) {
    var txtInput by rememberSaveable { mutableStateOf("") }

    TextField(
        value = txtInput,
        onValueChange = {
            txtInput = it
            onTextChange(txtInput)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.White)
    )
}

@Preview
@Composable
private fun PreviewInputWhite() {
    InputWhite() {}
}