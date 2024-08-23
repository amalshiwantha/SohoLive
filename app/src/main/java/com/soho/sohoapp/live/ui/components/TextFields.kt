@file:OptIn(ExperimentalMaterial3Api::class)

package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soho.sohoapp.live.enums.FieldConfig
import com.soho.sohoapp.live.model.TextFiledConfig
import com.soho.sohoapp.live.ui.theme.AppPrimaryDark
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.ErrorContent
import com.soho.sohoapp.live.ui.theme.HintGray

@Composable
fun TextAreaWhite(fieldConfig: TextFiledConfig, onTextChange: (Pair<String, String>) -> Unit) {
    val maxLength = 3000
    var txtInput by rememberSaveable { mutableStateOf(fieldConfig.input) }

    OutlinedTextField(
        value = txtInput,
        onValueChange = {
            if (it.length <= maxLength) {
                txtInput = it
                onTextChange(Pair(txtInput, "${it.length}/$maxLength"))
            }
        },
        placeholder = { Text(fieldConfig.placeholder) },
        keyboardOptions = KeyboardOptions(
            imeAction = fieldConfig.imeAction,
            keyboardType = fieldConfig.keyboardType
        ),
        maxLines = 10,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = AppWhite, focusedContainerColor = AppWhite,
            unfocusedPlaceholderColor = HintGray, focusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun TextFieldWhiteIcon(
    fieldConfig: FieldConfig,
    inputTxt: String = "",
    onTextChange: (String) -> Unit,
    onClick: () -> Unit
) {
    var txtInput by rememberSaveable { mutableStateOf(inputTxt) }

    TextField(
        value = inputTxt,
        onValueChange = {
            txtInput = it
            onTextChange(txtInput)
        },
        trailingIcon = {
            fieldConfig.trailingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }
        },
        singleLine = fieldConfig.isSingleLine,
        placeholder = { Text(fieldConfig.placeholder) },
        keyboardOptions = KeyboardOptions(
            imeAction = fieldConfig.imeAction,
            keyboardType = fieldConfig.keyboardType
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                if (fieldConfig.clickable) {
                    onClick()
                }
            },
        enabled = !fieldConfig.clickable,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = AppWhite, focusedContainerColor = AppWhite,
            unfocusedPlaceholderColor = HintGray, focusedIndicatorColor = Color.Transparent,
            disabledContainerColor = AppWhite,
            disabledTrailingIconColor = AppPrimaryDark,
            disabledTextColor = AppPrimaryDark
        )
    )
}

@Composable
fun TextFieldWhite(fieldConfig: FieldConfig, onTextChange: (String) -> Unit) {
    var txtInput by rememberSaveable { mutableStateOf(fieldConfig.input) }

    OutlinedTextField(
        value = txtInput,
        onValueChange = {
            txtInput = it
            onTextChange(txtInput)
        },
        singleLine = fieldConfig.isSingleLine,
        placeholder = { Text(fieldConfig.placeholder) },
        keyboardOptions = KeyboardOptions(
            imeAction = fieldConfig.imeAction,
            keyboardType = fieldConfig.keyboardType
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        isError = fieldConfig.isError,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedPlaceholderColor = AppWhite,
            unfocusedContainerColor = AppWhite,
            focusedContainerColor = AppWhite,
            unfocusedPlaceholderColor = HintGray,
            errorContainerColor = ErrorContent,
        )
    )
}

@Composable
fun TextFieldOutlined(tfConfig: TextFiledConfig, onTextChange: (String) -> Unit) {
    var txtInput by rememberSaveable { mutableStateOf(tfConfig.input) }

    OutlinedTextField(
        value = txtInput,
        onValueChange = {
            txtInput = it
            onTextChange(txtInput)
        },
        singleLine = tfConfig.isSingleLine,
        placeholder = { Text(tfConfig.placeholder) },
        keyboardOptions = KeyboardOptions(
            imeAction = tfConfig.imeAction,
            keyboardType = tfConfig.keyboardType
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        isError = tfConfig.isError,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedPlaceholderColor = AppWhite,
            unfocusedContainerColor = AppWhite,
            focusedContainerColor = AppWhite,
            unfocusedPlaceholderColor = HintGray,
            errorContainerColor = ErrorContent,
        )
    )
}

@Composable
fun PasswordTextFieldWhite(tempPw: String = "", onTextChange: (String) -> Unit) {
    var password by rememberSaveable { mutableStateOf(tempPw) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = password,
        onValueChange = {
            password = it
            onTextChange(password)
        },
        singleLine = true,
        placeholder = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = AppWhite, focusedContainerColor = AppWhite,
            unfocusedPlaceholderColor = HintGray, focusedIndicatorColor = Color.Transparent
        )
    )
}

@Preview
@Composable
private fun PreviewInputWhite() {
    TextFieldWhite(FieldConfig.NEXT.apply { placeholder = "Test" }) {}
}