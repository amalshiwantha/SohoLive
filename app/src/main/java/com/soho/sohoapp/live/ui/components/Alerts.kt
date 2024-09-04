package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.BottomBarUnselect
import com.soho.sohoapp.live.ui.theme.infoText
import com.soho.sohoapp.live.ui.view.screens.golive.getImageWidth

@Composable
fun ShareableLinkDialog(
    onClickCopy: (String) -> Unit,
    onClickLive: () -> Unit,
    onDismiss: () -> Unit,
    isHasSMLinks: Boolean = false
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
        ) {
            Column {

                // Title and close btn
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                ) {
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
                        onClickCopy(SocialMediaInfo.SOHO.name)
                    })
                }

                SpacerUp(size = 16.dp)
                Divider(color = BottomBarUnselect)
                SpacerUp(size = 16.dp)

                // Social Link
                if (isHasSMLinks) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text700_14sp(
                            step = "Social Links",
                            color = infoText
                        )
                        SpacerUp(size = 8.dp)
                        Text400_14sp(
                            info = "You can also share watching links for your connected socials",
                            color = infoText
                        )

                        // Social Media Buttons
                        SpacerUp(size = 24.dp)
                        Column {
                            SocialLinkButton(
                                smItem = SocialMediaInfo.FACEBOOK,
                                onClick = { onClickCopy(SocialMediaInfo.FACEBOOK.name) })
                            SpacerUp(size = 16.dp)
                            SocialLinkButton(
                                smItem = SocialMediaInfo.YOUTUBE,
                                onClick = { onClickCopy(SocialMediaInfo.YOUTUBE.name) })
                            SpacerUp(size = 16.dp)
                            SocialLinkButton(
                                smItem = SocialMediaInfo.LINKEDIN,
                                onClick = { onClickCopy(SocialMediaInfo.LINKEDIN.name) })
                        }
                    }

                    SpacerUp(size = 24.dp)
                    Divider(color = BottomBarUnselect)
                    SpacerUp(size = 8.dp)
                }

                // Go Live Now Button
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    ButtonGradientIcon(
                        text = "Go Live Now",
                        modifier = Modifier.fillMaxWidth(),
                        gradientBrush = brushGradientLive,
                        icon = R.drawable.livecast_color,
                        onBtnClick = {
                            onClickLive()
                        })
                }
            }
        }
    }
}

@Composable
private fun SocialLinkButton(smItem: SocialMediaInfo, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        val imgSize = getImageWidth(smItem.icon)
        Image(
            painter = painterResource(id = smItem.icon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = if (imgSize.width == 0) {
                Modifier
            } else {
                Modifier.size(imgSize.width.dp, imgSize.height.dp)
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        ButtonConnect(
            color = smItem.btnColor,
            text = "Copy Link",
            onBtnClick = { onClick() }
        )
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
