package com.soho.sohoapp.live.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.enums.SocialMedia
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.SmBtn
import com.soho.sohoapp.live.network.response.LiveRequest
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppGreenDark
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.BottomBarUnselect
import com.soho.sohoapp.live.ui.theme.FacebookBlue
import com.soho.sohoapp.live.ui.theme.FacebookBlueDark
import com.soho.sohoapp.live.ui.theme.LinkedInBlue
import com.soho.sohoapp.live.ui.theme.LinkedInBlueDark
import com.soho.sohoapp.live.ui.theme.YoutubeRed
import com.soho.sohoapp.live.ui.theme.YoutubeRedDark
import com.soho.sohoapp.live.ui.theme.infoText
import com.soho.sohoapp.live.ui.view.screens.golive.getImageWidth
import kotlinx.coroutines.delay

@Composable
fun NotEnableStreamAlert(
    onDismiss: () -> Unit,
    onVerifyClick: () -> Unit,
    onEnableClick: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Error connecting to YouTube",
                    color = Color(0xFF4C197D), // Your purple title color
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Subtitle / Message
                Text(
                    text = "The YouTube account you selected does not have live streaming enabled.",
                    color = Color.Black, // Body color
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Instruction
                Text(
                    text = "Before trying to connect again, please ensure you have:",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Verify Link
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable { onVerifyClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "• ",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Verify your YouTube account",
                        color = Color(0xFF4C197D), // Link color
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Enable Link
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEnableClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "• ",
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Enable live streaming on your YouTube channel",
                        color = Color(0xFF4C197D),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Action Button
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color(0xFF53D3B8) // Your button background color
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Back",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}


@Composable
fun ShareableLinkDialog(
    btnTxt: String,
    reqLive: LiveRequest,
    onClickCopy: (String) -> Unit,
    onClickLive: () -> Unit,
    onDismiss: () -> Unit,
    isHasSMLinks: Boolean = true,
    isShowLiveBtn: Boolean = true
) {
    //same in LiveStreamAct..
    fun isHasShareLink(smName: SocialMedia): Boolean {
        return reqLive.simulcastTargets.filter {
            it.platform == (smName.name.lowercase())
        }.isNotEmpty()
    }

    val timeOutReset = 3000L
    val txtCopy = "Copy Link"
    val txtCopied = "Copied"
    var isCopiedSoho by remember { mutableStateOf(false) }
    var isCopiedFacebook by remember { mutableStateOf(false) }
    var isCopiedYoutube by remember { mutableStateOf(false) }
    var isCopiedLinkedIn by remember { mutableStateOf(false) }

    //reset state copy
    LaunchedEffect(isCopiedSoho) {
        if (isCopiedSoho) {
            delay(timeOutReset)
            isCopiedSoho = false
        }
    }

    LaunchedEffect(isCopiedFacebook) {
        if (isCopiedFacebook) {
            delay(timeOutReset)
            isCopiedFacebook = false
        }
    }

    LaunchedEffect(isCopiedYoutube) {
        if (isCopiedYoutube) {
            delay(timeOutReset)
            isCopiedYoutube = false
        }
    }

    LaunchedEffect(isCopiedLinkedIn) {
        if (isCopiedLinkedIn) {
            delay(timeOutReset)
            isCopiedLinkedIn = false
        }
    }

    var smBtnSoho by remember {
        mutableStateOf(
            SmBtn(AppGreen, "Copy Listing URL", null)
        )
    }
    var smBtnFb by remember {
        mutableStateOf(
            SmBtn(FacebookBlue, txtCopy, null)
        )
    }
    var smBtnYt by remember {
        mutableStateOf(
            SmBtn(YoutubeRed, txtCopy, null)
        )
    }
    var smBtnLi by remember {
        mutableStateOf(
            SmBtn(LinkedInBlue, txtCopy, null)
        )
    }

    smBtnSoho = SmBtn(
        if (isCopiedSoho) AppGreenDark else AppGreen,
        if (isCopiedSoho) txtCopied else txtCopy,
        if (isCopiedSoho) R.drawable.copy_tick else null
    )

    smBtnFb = SmBtn(
        if (isCopiedFacebook) FacebookBlueDark else FacebookBlue,
        if (isCopiedFacebook) txtCopied else txtCopy,
        if (isCopiedFacebook) R.drawable.copy_tick else null
    )

    smBtnYt = SmBtn(
        if (isCopiedYoutube) YoutubeRedDark else YoutubeRed,
        if (isCopiedYoutube) txtCopied else txtCopy,
        if (isCopiedYoutube) R.drawable.copy_tick else null
    )

    smBtnLi = SmBtn(
        if (isCopiedLinkedIn) LinkedInBlueDark else LinkedInBlue,
        if (isCopiedLinkedIn) txtCopied else txtCopy,
        if (isCopiedLinkedIn) R.drawable.copy_tick else null
    )

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
        ) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppWhite)
            ) {
                val (content, goLiveBtn) = createRefs()

                LazyColumn(
                    modifier = Modifier
                        .constrainAs(content) {
                            top.linkTo(parent.top)
                            bottom.linkTo(goLiveBtn.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    item {
                        //Title and Close
                        Row(
                            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 8.dp)
                        ) {
                            //share icon and text
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                                    .weight(1f)
                            ) {
                                Image(
                                    modifier = Modifier.padding(bottom = 2.dp),
                                    painter = painterResource(id = R.drawable.ic_share),
                                    contentDescription = null,
                                )
                                SpacerSide(size = 8.dp)
                                Text950_20sp(
                                    title = "Shareable Links",
                                    txtColor = infoText
                                )
                            }

                            //close button
                            Image(
                                painter = painterResource(id = R.drawable.ic_round_cross),
                                contentDescription = null,
                                modifier = Modifier.clickable { onDismiss() }
                            )
                        }

                        //content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {

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
                                contentDescription = null
                            )
                            SpacerUp(size = 8.dp)
                            Text400_14sp(
                                info = "Videos will be shown on the property listing.",
                                color = infoText
                            )

                            //Copy Btn
                            SpacerUp(size = 16.dp)
                            ButtonColoredIcon(
                                title = smBtnSoho.txt,
                                btnColor = smBtnSoho.color,
                                icon = smBtnSoho.icon,
                                onBtnClick = {
                                    onClickCopy(SocialMediaInfo.SOHO.name)
                                    isCopiedSoho = true
                                })
                            SpacerUp(size = 16.dp)
                        }

                        // Social Link
                        if (reqLive.simulcastTargets.isNotEmpty()) {

                            Divider(color = BottomBarUnselect)
                            SpacerUp(size = 16.dp)

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
                                    if (isHasShareLink(SocialMedia.FACEBOOK)) {
                                        SocialLinkButton(
                                            smItem = SocialMediaInfo.FACEBOOK,
                                            onClick = {
                                                onClickCopy(SocialMediaInfo.FACEBOOK.name)
                                                isCopiedFacebook = true
                                            },
                                            smBtn = smBtnFb
                                        )
                                        SpacerUp(size = 16.dp)
                                    }

                                    if (isHasShareLink(SocialMedia.YOUTUBE)) {
                                        SocialLinkButton(
                                            smItem = SocialMediaInfo.YOUTUBE,
                                            onClick = {
                                                onClickCopy(SocialMediaInfo.YOUTUBE.name)
                                                isCopiedYoutube = true
                                            },
                                            smBtn = smBtnYt
                                        )
                                        SpacerUp(size = 16.dp)
                                    }

                                    if (isHasShareLink(SocialMedia.LINKEDIN)) {
                                        SocialLinkButton(
                                            smItem = SocialMediaInfo.LINKEDIN,
                                            onClick = {
                                                onClickCopy(SocialMediaInfo.LINKEDIN.name)
                                                isCopiedLinkedIn = true
                                            },
                                            smBtn = smBtnLi
                                        )
                                    }

                                }
                            }
                        }


                        if (isShowLiveBtn) {
                            SpacerUp(size = 16.dp)
                            Divider(color = BottomBarUnselect)
                        } else {
                            SpacerUp(size = 8.dp)
                        }
                    }
                }

                // Go Live Now Button
                if (isShowLiveBtn) {
                    Column(
                        modifier = Modifier
                            .constrainAs(goLiveBtn) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 16.dp)
                    ) {
                        ButtonGradientIcon(
                            text = btnTxt,
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
}

@Composable
private fun SocialLinkButton(smItem: SocialMediaInfo, onClick: () -> Unit, smBtn: SmBtn) {
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
        ButtonConnectIcon(
            color = smBtn.color,
            text = smBtn.txt,
            icon = smBtn.icon,
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
