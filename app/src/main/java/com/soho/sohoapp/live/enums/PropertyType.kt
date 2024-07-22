package com.soho.sohoapp.live.enums

import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.ui.theme.AuctionYellow
import com.soho.sohoapp.live.ui.theme.InspectionGreen

enum class PropertyType(val bgColor: Color) {
    AUCTION(AuctionYellow),
    INSPECTION(InspectionGreen)
}