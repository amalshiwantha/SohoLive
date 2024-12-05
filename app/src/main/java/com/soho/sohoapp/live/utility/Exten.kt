package com.soho.sohoapp.live.utility

import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.db.AgentProperty
import com.soho.sohoapp.live.network.response.Document
import java.text.DecimalFormat
import java.util.Locale

fun String.toCapsFirstLetter(): String {
    return this.replaceFirstChar { it.uppercase() }
}


fun Int.formatNumber(): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(this)
}

fun Document.toAgentProperty(): AgentProperty {
    return AgentProperty(
        propertyId = this.propertyId,
        address = this.fullAddress(),
        bedrooms = this.bedroomCount,
        bathrooms = this.bathroomCount,
        parking = this.carspotCount,
        areaSize = this.areaSize(),
        agent = if (this.getAgents().isNotEmpty()) this.getAgents()[0] else null
    )
}

fun String.hexToColor(): Color {
    try {
        val hexColor = this.removePrefix("#")
        val colorLong = hexColor.toLong(16)
        val alpha = 0xFF000000
        val colorWithAlpha = colorLong or alpha
        return Color(colorWithAlpha)
    } catch (e: Exception) {
        return Color.White
    }
}

fun String.toFileName(): String {
    return this.replace(" ", "_")
        .replace("/", "_")
        .lowercase()
}

fun String.toUppercaseFirst(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault()) else it.toString()
    }
}

fun Double.visibleValue(): String? {
    val value = this.toInt()
    return if (value > 0) {
        value.toString()
    } else {
        null
    }
}

