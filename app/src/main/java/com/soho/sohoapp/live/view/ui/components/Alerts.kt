package com.soho.sohoapp.live.view.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.soho.sohoapp.live.utility.AlertDialogConfig


@Composable
fun AppAlertDialog(
    alert: AlertDialogConfig,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isShowDialog: Boolean = true
) {
    var showDialog by remember { mutableStateOf(isShowDialog) }
    println("alertTypeState "+showDialog)
    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = !showDialog },
            title = { Text(text = alert.title) },
            text = { Text(text = alert.message) },
            confirmButton = {
                if (alert.isConfirm) {
                    Button(onClick = {
                        onConfirm()
                        showDialog = false
                    }) {
                        Text(text = alert.confirmBtnText)
                    }
                }
            }, dismissButton = {
                Button(onClick = {
                    onDismiss()
                    showDialog = false
                }) {
                    Text(text = alert.dismissBtnText)
                }
            }, modifier = Modifier
        )
    }
}
