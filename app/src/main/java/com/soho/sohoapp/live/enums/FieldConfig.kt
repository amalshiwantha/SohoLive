package com.soho.sohoapp.live.enums

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

enum class FieldConfig(
    var placeholder: String = "",
    val keyboardType: KeyboardType = KeyboardType.Text,
    val imeAction: ImeAction = ImeAction.Next,
    var isSingleLine: Boolean = true
) {
    NEXT(imeAction = ImeAction.Next),
    DONE(imeAction = ImeAction.Done)
}