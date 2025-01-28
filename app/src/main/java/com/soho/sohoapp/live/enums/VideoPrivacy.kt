package com.soho.sohoapp.live.enums

import androidx.compose.ui.graphics.Color
import com.soho.sohoapp.live.ui.theme.PinkUnlisted
import com.soho.sohoapp.live.ui.theme.PrivateGray
import com.soho.sohoapp.live.ui.theme.PublicGreen
import kotlinx.serialization.Serializable

@Serializable
enum class VideoPrivacy(val bgColor: Color, val label: String) {
    PUBLIC(PublicGreen, "PUBLIC"),
    UNLISTED(PinkUnlisted, "UNLISTED"),
    PRIVATE(PrivateGray, "PRIVATE");

    companion object {
        fun fromId(value: Boolean): VideoPrivacy {
            return when (value) {
                true -> UNLISTED
                false -> PUBLIC
            }
        }

        fun fromLabel(value: String): VideoPrivacy {
            return when (value) {
                PUBLIC.label -> PUBLIC
                UNLISTED.label -> UNLISTED
                PRIVATE.label -> PRIVATE
                else -> UNLISTED
            }
        }

        fun toBool(value: String): Boolean {
            return when (value) {
                PUBLIC.label -> false
                UNLISTED.label -> true
                else -> false
            }
        }
    }
}