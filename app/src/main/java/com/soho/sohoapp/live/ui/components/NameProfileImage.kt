package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.soho.sohoapp.live.utility.getInitialBg

@Composable
fun InitialProfileImage(name: String, imgSize: Dp) {
    val nameInitial = name.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .joinToString("")

    val initialBg = getInitialBg(nameInitial.lowercase())

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.size(imgSize)
    ) {
        // Display the circular image
        Image(
            painter = painterResource(id = initialBg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(imgSize)
                .clip(CircleShape)
        )

        // Overlay the initials at the center top
        Text(
            text = nameInitial,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}