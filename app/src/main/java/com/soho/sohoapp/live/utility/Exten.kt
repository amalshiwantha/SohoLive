package com.soho.sohoapp.live.utility

import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.db.AgentProperty
import com.soho.sohoapp.live.network.response.Document
import java.text.DecimalFormat
import java.util.Locale

fun String.isMulticast(): Boolean {
    return this.contains("Multicast")
}

fun String.toErrorCode(): Int {
    return if (this.contains("404")) 404 else if (this.contains("403")) 403 else 500
}

fun String.toCapsFirstLetter(): String {
    return this.replaceFirstChar { it.uppercase() }
}

fun Int.formatNumber(): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(this)
}

fun Document.toAgentProperty(): AgentProperty {
    val myDoc = this
    return AgentProperty(
        propertyId = myDoc.propertyId,
        address = myDoc.fullAddress(),
        bedrooms = myDoc.bedroomCount,
        bathrooms = myDoc.bathroomCount,
        parking = myDoc.carspotCount,
        areaSize = myDoc.areaSize(),
        agent = if (myDoc.getAgents().isNotEmpty()) myDoc.getAgents()[0] else null
    ).apply {
        this.agent?.agency_name = myDoc.agencyName
    }
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

