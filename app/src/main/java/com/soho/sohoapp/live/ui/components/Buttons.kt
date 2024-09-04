package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soho.sohoapp.live.ui.theme.AppRed
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.BorderGray

@Composable
fun ButtonOutLinedIcon(
    modifier: Modifier = Modifier,
    text: String,
    icon: Int,
    onBtnClick: () -> Unit
) {
    OutlinedButton(
        onClick = { onBtnClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        border = BorderStroke(1.dp, BorderGray),
        shape = RoundedCornerShape(16.dp),
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "null",
                contentScale = ContentScale.Crop
            )
            SpacerSide(size = 8.dp)
            Text800_12sp(label = text)
        }
    }
}


@Composable
fun ButtonColouredWrap(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    isBackButton: Boolean = false,
    onBtnClick: () -> Unit
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        onClick = { onBtnClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        if (isBackButton) {
            TextWhite14Left(title = text)
        } else {
            TextWhite14(title = text)
        }
    }
}

@Composable
fun ButtonColoured(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    isBackButton: Boolean = false,
    onBtnClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = { onBtnClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        if (isBackButton) {
            TextWhite14Left(title = text)
        } else {
            TextWhite14(title = text)
        }
    }
}

@Composable
fun ButtonColouredProgress(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    isLoading: Boolean = false,
    isBackButton: Boolean = false,
    onBtnClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            if (!isLoading) {
                onBtnClick()
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = AppRed
            )
        } else {
            if (isBackButton) {
                TextWhite14Left(title = text)
            } else {
                TextWhite14(title = text)
            }
        }
    }
}

@Composable
fun ButtonGradientIcon(
    text: String,
    gradientBrush: Brush,
    icon: Int,
    modifier: Modifier = Modifier,
    onBtnClick: () -> Unit
) {
    val radius = 16.dp
    Button(
        modifier = modifier,
        onClick = onBtnClick,
        shape = RoundedCornerShape(radius),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Transparent
        ),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = modifier
                .background(
                    brush = gradientBrush,
                    shape = RoundedCornerShape(radius)
                )
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = "",
                    tint = Color.White
                )
                SpacerSide(size = 8.dp)
                TextWhite14(title = text)
            }
        }
    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtonGradientIcon(
    text: String,
    gradientBrush: Brush,
    icon: Int,
    modifier: Modifier = Modifier,
    onBtnClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        onClick = { onBtnClick() }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(gradientBrush)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "null",
                    contentScale = ContentScale.Crop
                )
                TextWhite14(title = text)
            }
        }
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtonColoredIcon(
    title: String,
    btnColor: Color,
    icon: Int? = null,
    modifier: Modifier = Modifier,
    onBtnClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        onClick = { onBtnClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .background(btnColor)
                .padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                if (icon != null) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = "null",
                        contentScale = ContentScale.Crop
                    )
                }
                Text800_14sp(label = title)
            }
        }
    }
}

@Composable
fun ButtonConnect(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    txtColor: Color = AppWhite,
    onBtnClick: () -> Unit
) {
    Button(
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = { onBtnClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        TextWhite12(title = text, txtColor = txtColor)
    }
}

@Composable
fun ButtonOutlineWhite(
    text: String,
    modifier: Modifier = Modifier.height(48.dp),
    onBtnClick: () -> Unit
) {
    OutlinedButton(
        onClick = { onBtnClick() },
        modifier = modifier,
        border = BorderStroke(2.dp, Color.White),
        shape = RoundedCornerShape(16.dp),
    ) {
        TextWhite14(title = text)
    }
}

@Composable
fun TextButtonBlue(text: String, modifier: Modifier = Modifier, onBtnClick: () -> Unit) {
    TextButton(onClick = { onBtnClick() }, modifier = modifier) {
        TextBlue14(label = text, modifier = modifier)
    }
}

@Preview
@Composable
private fun TextButtonBluePrev() {
    TextButtonBlue(text = "Signup", onBtnClick = { /* Handle button click */ })
}

@Preview
@Composable
private fun CustomButtonPrev() {
    ButtonOutlineWhite(text = "Signup",
        modifier = Modifier.fillMaxWidth(), onBtnClick = { /* Handle button click */ })
}

@Preview
@Composable
private fun ColouredButtonPrev() {
    ButtonColoured(text = "Login",
        color = Color.Red,
        onBtnClick = { /* Handle button click */ })
}