package com.soho.sohoapp.live.enums

import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.ui.theme.AuctionYellow
import com.soho.sohoapp.live.ui.theme.InspectionGreen

enum class PropertyType(val bgColor: Color) {
    AUCTION(AuctionYellow),
    INSPECTION(InspectionGreen);

    companion object {
        fun fromString(value: String): PropertyType {
            return when (value.uppercase()) {
                "AUCTION" -> AUCTION
                "INSPECTION" -> INSPECTION
                else -> AUCTION
            }
        }
    }
}