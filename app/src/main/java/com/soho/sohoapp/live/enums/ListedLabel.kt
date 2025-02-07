package com.soho.sohoapp.live.enums

import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.R

enum class ListedLabel(
    val title: String,
    var icon: Int,
    var bgColor: Color
) {
    PUBLIC(title = "public", icon = R.drawable.ic_eye_vec, bgColor = Color(0xFF05867F)),
    PRIVATE(title = "private", icon = R.drawable.ic_clock, bgColor = Color(0xFFB8B7BB)),
    UNLISTED(title = "unlisted", icon = R.drawable.ic_hide_eye, bgColor = Color(0xFF8628FF))
}
