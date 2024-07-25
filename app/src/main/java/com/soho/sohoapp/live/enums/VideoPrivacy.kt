package com.soho.sohoapp.live.enums

import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.ui.theme.PrivateGray
import com.soho.sohoapp.live.ui.theme.PublicGreen

enum class VideoPrivacy(val bgColor: Color, val label: String) {
    PUBLIC(PublicGreen, "PUBLIC"),
    PRIVATE(PrivateGray, "PRIVATE");

    companion object {
        fun fromId(value: Boolean): VideoPrivacy {
            return when (value) {
                true -> PUBLIC
                false -> PRIVATE
            }
        }

        fun fromLabel(value: String): VideoPrivacy {
            return when (value) {
                PUBLIC.label -> PUBLIC
                PRIVATE.label -> PRIVATE
                else -> PRIVATE
            }
        }
    }
}