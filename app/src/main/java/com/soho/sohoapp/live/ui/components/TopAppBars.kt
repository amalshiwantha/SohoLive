package com.soho.sohoapp.live.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.theme.AppWhite

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
        title = {
            Text(
                text = title,
                fontSize = 24.sp,
                lineHeight = 33.6.sp,
                fontFamily = FontFamily(Font(R.font.axiforma)),
                fontWeight = FontWeight(950),
                color = AppWhite,
                letterSpacing = 0.28.sp,
            )
        },
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