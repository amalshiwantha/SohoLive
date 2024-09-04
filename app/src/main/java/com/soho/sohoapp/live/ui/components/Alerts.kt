package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.infoText

@Composable
fun ShareableLinkDialog(
    onClickCopy: (String) -> Unit,
    onClickLive: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Title and close btn
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = null,
                    )
                    SpacerSide(size = 8.dp)
                    Text950_20sp(
                        title = "Shareable Links",
                        txtColor = infoText,
                        modifier = Modifier.weight(1f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_round_cross),
                        contentDescription = null,
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }

                // Description
                SpacerUp(size = 8.dp)
                Text400_14sp(
                    info = "Copy and share your links before you go live. This will reduce disruptions once you are live.",
                    color = infoText
                )

                //SohoLogo
                SpacerUp(size = 24.dp)
                Image(
                    painter = painterResource(id = R.drawable.logo_soho),
                    contentDescription = null,
                    modifier = Modifier.clickable { onDismiss() }
                )
                SpacerUp(size = 8.dp)
                Text400_14sp(
                    info = "Videos will be shown on the property listing.",
                    color = infoText
                )

                //Copy Btn
                SpacerUp(size = 16.dp)
                ButtonColoured(text = "Copy Listing URL", color = AppGreen, onBtnClick = {
                    onClickCopy("https://soho.com.au")
                })

                // Social Link
                SpacerUp(size = 32.dp)
                Text700_14sp(
                    step = "Social Links",
                    color = infoText
                )
                SpacerUp(size = 8.dp)
                Text400_14sp(
                    info = "You can also share watching links for your connected socials",
                    color = infoText
                )

                // Social Links
                Text(
                    text = "Social Links",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Social Media Buttons
                Column {
                    SocialLinkButton(
                        platform = "Facebook",
                        color = Color.Blue,
                        onClick = { onClickCopy("facebook.com/link") })
                    SocialLinkButton(
                        platform = "YouTube",
                        color = Color.Red,
                        onClick = { onClickCopy("youtube.com/link") })
                    SocialLinkButton(
                        platform = "LinkedIn",
                        color = Color.Cyan,
                        onClick = { onClickCopy("linkedin.com/link") })
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Go Live Now Button
                Button(
                    onClick = { onClickLive() },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Go Live Now", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun SocialLinkButton(platform: String, color: Color, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        // Icon or platform logo
        Text(
            text = platform,
            color = color,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onClick) {
            Text(text = "Copy Link")
        }
    }
}

@Composable
fun AppAlertDialog(
    alert: AlertConfig,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = { onDismiss() },
        title = { Text(text = alert.title) },
        text = { Text(text = alert.message) },
        confirmButton = {
            if (alert.isConfirm) {
                Button(onClick = {
                    onConfirm()
                }) {
                    Text(text = alert.confirmBtnText)
                }
            }
        }, dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = alert.dismissBtnText)
            }
        }, modifier = Modifier
    )
}
