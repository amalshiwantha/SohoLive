package com.soho.sohoapp.live.enums

import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.ui.theme.PrivateGray
import com.soho.sohoapp.live.ui.theme.PublicGreen

enum class VideoPrivacy(val bgColor: Color, val label: String) {
    PUBLIC(PublicGreen, "PUBLIC"),
    UNLISTED(PrivateGray, "UNLISTED");

    companion object {
        fun fromId(value: Boolean): VideoPrivacy {
            return when (value) {
                true -> PUBLIC
                false -> UNLISTED
            }
        }

        fun fromLabel(value: String): VideoPrivacy {
            return when (value) {
                PUBLIC.label -> PUBLIC
                UNLISTED.label -> UNLISTED
                else -> UNLISTED
            }
        }

        fun toBool(value: String): Boolean {
            return when (value) {
                PUBLIC.label -> true
                UNLISTED.label -> false
                else -> false
            }
        }
    }
}