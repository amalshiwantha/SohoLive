package com.soho.sohoapp.live.utility

import androidx.compose.ui.graphics.Color
import java.util.Locale

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

