package com.soho.sohoapp.live.enums

import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.ui.theme.PrivateGray
import com.soho.sohoapp.live.ui.theme.PublicGreen

enum class PropertyVisibility(val bgColor: Color) {
    PUBLIC(PublicGreen),
    PRIVATE(PrivateGray)
}