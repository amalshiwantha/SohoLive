package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soho.sohoapp.live.enums.FieldConfig
import com.soho.sohoapp.live.model.TextFiledConfig
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.ErrorContent
import com.soho.sohoapp.live.ui.theme.HintGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownWhatForLiveStream(
    options: MutableList<String>,
    placeHolder: String,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier,
    fieldConfig: TextFiledConfig
) {
    var expanded by remember { mutableStateOf(false) }
    var inputVal by remember { mutableStateOf(fieldConfig.input) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier
    ) {

        //TextField
        OutlinedTextField(
            readOnly = true,
            value = inputVal,
            onValueChange = {},
            placeholder = { TextPlaceHolder(label = placeHolder) },
            textStyle = inputStyleSearch(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = AppWhite,
                focusedContainerColor = AppWhite,
                unfocusedPlaceholderColor = HintGray,
                errorContainerColor = ErrorContent,
            ),
            isError = fieldConfig.isError,
            modifier = Modifier
                .menuAnchor()
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )

        //DropDown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(AppWhite)
                .exposedDropdownSize()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        inputVal = option
                        onValueChangedEvent(option)
                    },
                    text = {
                        Text(option)
                    }
                )
            }
        }

    }
}