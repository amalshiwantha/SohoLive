package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soho.sohoapp.live.ui.theme.BottomBarBg
import com.soho.sohoapp.live.ui.theme.BottomSheetDrag
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectOrientationBottomSheet(onGoLive: () -> Unit, onCancel: () -> Unit) {

    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedOrientation by remember { mutableStateOf("Vertical") }

    ModalBottomSheet(
        containerColor = BottomBarBg,
        dragHandle = { DragHandle(color = BottomSheetDrag) },
        onDismissRequest = { onCancel() },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Livecast Orientation",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "This will determine the orientation for this livecast.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RadioButtonWithText(
                    selected = selectedOrientation == "Vertical",
                    text = "Vertical",
                    onClick = { selectedOrientation = "Vertical" }
                )
                RadioButtonWithText(
                    selected = selectedOrientation == "Horizontal",
                    text = "Horizontal",
                    onClick = { selectedOrientation = "Horizontal" }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            bottomSheetState.hide()
                            onCancel()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        onGoLive()
                    },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Preview Live",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Preview Live", color = Color.White)
                }
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
