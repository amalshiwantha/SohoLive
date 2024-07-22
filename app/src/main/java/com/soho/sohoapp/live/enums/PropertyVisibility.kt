package com.soho.sohoapp.live.enums

import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.enums.PropertyType.AUCTION
import com.soho.sohoapp.live.enums.PropertyType.INSPECTION
import com.soho.sohoapp.live.ui.theme.PrivateGray
import com.soho.sohoapp.live.ui.theme.PublicGreen

enum class PropertyVisibility(val bgColor: Color,val label: String) {
    PUBLIC(PublicGreen, "PUBLIC"),
    PRIVATE(PrivateGray,"PRIVATE");

    companion object {
        fun fromString(value: Int): PropertyVisibility {
            return when (value) {
                1 -> PUBLIC
                0 -> PRIVATE
                else -> PUBLIC
            }
        }
    }
}