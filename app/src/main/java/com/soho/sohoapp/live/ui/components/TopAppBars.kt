package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.theme.AppWhite

@Composable
fun TopAppBarProfile(
    name: String,
    imageUrl: String?,
    modifier: Modifier
) {
    val imgSize = 48.dp

    Row(
        modifier = modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile image
        imageUrl?.let {
            val urlPainter = rememberAsyncImagePainter(
                model = imageUrl,
                placeholder = painterResource(id = R.drawable.profile_placeholder),
                error = painterResource(id = R.drawable.profile_placeholder)
            )

            Image(
                painter = urlPainter,
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(imgSize)
                    .clip(CircleShape)
            )
        } ?: kotlin.run {
            InitialProfileImage(name, imgSize)
        }

        SpacerSide(size = 12.dp)

        //Profile Name
        TextTopBarTitle(title = "Hi, $name")
    }
}

@Composable
fun TopAppBarActionBack(
    title: String? = null,
    rightIcon: Int? = null,
    isShowBack: Boolean = true,
    isShowBackBg: Boolean = false,
    modifier: Modifier = Modifier,
    onActionClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back Button on the left
        if (isShowBack) {
            IconButton(onClick = { onBackClick() }) {
                if (isShowBackBg) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_bg_round),
                        tint = AppWhite,
                        contentDescription = "Back"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Sharp.ArrowBackIosNew,
                        tint = AppWhite,
                        contentDescription = "Back"
                    )
                }
            }
        } else {
            //Spacer(modifier = Modifier.width(48.dp)) // Match the size of the back button for alignment
        }

        // Title in the center
        title?.let {
            TextTopBarTitle(
                title = it, modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
        }

        // Optional right action button
        rightIcon?.let {
            IconButton(onClick = { onActionClick() }) {
                Icon(
                    painter = painterResource(id = it),
                    tint = AppWhite,
                    contentDescription = "Action Button"
                )
            }
        }
    }
}

@Composable
fun TopAppBarCustomClose(
    title: String,
    rightIcon: Int,
    modifier: Modifier,
    onCloseClick: () -> Unit = {}
) {
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


@Composable
fun AppTopBarCustom(
    title: String,
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Icons.Sharp.ArrowBackIosNew,
                tint = AppWhite,
                contentDescription = "Back"
            )
        }

        TextTopBarTitle(title = title, modifier = Modifier.weight(1f))
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