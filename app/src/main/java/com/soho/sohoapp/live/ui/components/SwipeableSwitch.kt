package com.soho.sohoapp.live.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.Orientation
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.AppWhiteGray
import com.soho.sohoapp.live.ui.theme.BorderGray
import com.soho.sohoapp.live.ui.theme.TextDark

@Composable
fun SwipeSwitchOrientation(selectedVal: MutableState<String>, onSwipeChange: (String) -> Unit) {
    val isPort = selectedVal.value == Orientation.PORT.name
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = 16.dp
    val availableWidth = screenWidth - (horizontalPadding * 2)
    val indicatorWidth = availableWidth / 2
    val indicatorOffset by animateDpAsState(
        targetValue = if (isPort) 0.dp else indicatorWidth, label = "animateToMove"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .border(width = 1.dp, color = BorderGray, shape = RoundedCornerShape(16.dp))
            .background(Color.Transparent)
    ) {
        //move selection
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(indicatorWidth)
                .padding(4.dp)
                .fillMaxHeight()
                .background(AppWhiteGray, shape = RoundedCornerShape(14.dp))
        )

        //two options
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextIconSwipeSelection(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSwipeChange(Orientation.PORT.name) },
                title = "Vertical",
                icon = R.drawable.ori_port,
                textColor = if (isPort) TextDark else AppWhite
            )

            TextIconSwipeSelection(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSwipeChange(Orientation.LAND.name) },
                title = "Horizontal",
                icon = R.drawable.ori_land,
                textColor = if (!isPort) TextDark else AppWhite
            )
        }
    }
}