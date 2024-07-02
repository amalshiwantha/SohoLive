package com.soho.sohoapp.live.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

enum class FieldConfig(
    var placeholder: String = "",
    var keyboardType: KeyboardType = KeyboardType.Text,
    var imeAction: ImeAction = ImeAction.Next,
    var isSingleLine: Boolean = true,
    var trailingIcon: ImageVector? = null,
    var clickable: Boolean = false
) {
    NEXT(imeAction = ImeAction.Next),
    DONE(imeAction = ImeAction.Done)
}