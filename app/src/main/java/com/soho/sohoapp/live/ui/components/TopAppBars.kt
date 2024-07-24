package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soho.sohoapp.live.ui.theme.AppWhite

@Composable
fun TopAppBarCustomClose(title: String, rightIcon: Int,modifier: Modifier, onCloseClick: () -> Unit = {}) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextTopBarTitle(title = title, modifier = Modifier.weight(1f))
        IconButton(onClick = { onCloseClick() }) {
            Icon(
                painter = painterResource(id = rightIcon),
                tint = AppWhite,
                contentDescription = "Back"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    rightIcon: Int = 0,
    isAllowBack: Boolean = true,
    onBackClick: () -> Unit,
    onRightClick: () -> Unit
) {
    TopAppBar(
        title = { TextTopBarTitle(title) },
        actions = {
            if (rightIcon != 0) {
                IconButton(onClick = { onRightClick() }) {
                    Icon(
                        painter = painterResource(id = rightIcon),
                        tint = AppWhite,
                        contentDescription = "Right Icon"
                    )
                }
            }
        },
        navigationIcon = {
            if (isAllowBack) {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.Sharp.ArrowBackIosNew,
                        tint = AppWhite,
                        contentDescription = "Back"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Preview
@Composable
private fun PreviewAppTopBar() {
    AppTopBar("Welcome Back", onBackClick = {}, onRightClick = {})
}