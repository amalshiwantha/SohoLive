package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import com.soho.sohoapp.live.model.MainStateHolder
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.BottomBarBg
import com.soho.sohoapp.live.ui.theme.BottomBarUnselect
import com.soho.sohoapp.live.ui.theme.BottomSheetDrag
import com.soho.sohoapp.live.ui.theme.DurationDark
import com.soho.sohoapp.live.ui.view.screens.subscription.BulletPointText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageBottomSheet(onDone: () -> Unit) {

    val mState = MainStateHolder.mState
    val bottomSheetState = rememberModalBottomSheetState()
    val inAppStoreDays = mState.activePlan.value?.terms?.inAppStorageDays ?: 0

    ModalBottomSheet(
        containerColor = BottomBarBg,
        dragHandle = { DragHandle(color = BottomSheetDrag) },
        onDismissRequest = { onDone() },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text800_20sp(label = "In-App Storage")
            SpacerUp(size = 8.dp)

            Text400_14sp(info = "All our plans let you store and easily access your videos in the app for a set period.")
            SpacerUp(size = 16.dp)

            StorageCard(inAppStoreDays)

            Divider(color = BottomBarUnselect, modifier = Modifier.padding(vertical = 24.dp))

            Text700_14sp(step = "Alternative ways to keep your videos:")
            SpacerUp(size = 16.dp)

            Column(modifier = Modifier.padding(start = 8.dp)) {
                BulletPointText("Download it before it expires")
                BulletPointText("Get a multicast plan for 90 days of storage")
                BulletPointText("Access it on the social channels where you streamed")
            }

            SpacerUp(size = 24.dp)
            ButtonColoured(text = "Done", color = AppGreen, onBtnClick = {
                onDone()
            })

        }
    }
}

@Composable
fun StorageCard(inAppStoreDays: Any) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DurationDark)
    ) {
        Column(modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp)) {
            Text400_14sp(info = "Your current storage days")
            SpacerUp(size = 8.dp)
            Text950_20sp(title = "$inAppStoreDays Days")
        }
    }
}

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

            SwipeSwitchOrientation(selectedOrientation, onSwipeChange = {
                selectedOrientation.value = it
            })

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

                ButtonGradientIcon(text = "Preview",
                    icon = R.drawable.livecast_color,
                    gradientBrush = brushGradientLive,
                    modifier = Modifier.weight(1f),
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
