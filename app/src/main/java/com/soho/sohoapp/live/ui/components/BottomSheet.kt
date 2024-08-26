package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.Orientation
import com.soho.sohoapp.live.model.MainStateHolder
import com.soho.sohoapp.live.ui.theme.BottomBarBg
import com.soho.sohoapp.live.ui.theme.BottomSheetDrag
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectOrientationBottomSheet(onGoLive: () -> Unit, onCancel: () -> Unit) {

    val mState = MainStateHolder.mState
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val selectedOrientation by remember { mutableStateOf(mState.liveOrientation) }

    ModalBottomSheet(
        containerColor = BottomBarBg,
        dragHandle = { DragHandle(color = BottomSheetDrag) },
        onDismissRequest = { onCancel() },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text800_20sp(label = "Select Livecast Orientation")
            SpacerUp(size = 8.dp)

            Text400_14sp(info = "This will determine the orientation for this livecast.")
            SpacerUp(size = 24.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RadioButtonWithText(
                    selected = selectedOrientation.value == Orientation.PORT.name,
                    text = "Vertical",
                    onClick = { selectedOrientation.value = Orientation.PORT.name }
                )
                RadioButtonWithText(
                    selected = selectedOrientation.value == Orientation.LAND.name,
                    text = "Horizontal",
                    onClick = { selectedOrientation.value = Orientation.LAND.name }
                )
            }

            SpacerUp(size = 24.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ButtonOutlineWhite(
                    text = "Cancel",
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onBtnClick = {
                        scope.launch {
                            bottomSheetState.hide()
                            onCancel()
                        }
                    })

                SpacerSide(size = 8.dp)

                ButtonGradientIcon(text = "Preview Live",
                    icon = R.drawable.livecast_color,
                    gradientBrush = brushGradientLive,
                    onBtnClick = {
                        onGoLive()
                    })

            }
        }
    }
}

@Composable
fun RadioButtonWithText(selected: Boolean, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
