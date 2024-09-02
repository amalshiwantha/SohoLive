package com.soho.sohoapp.live.ui.view.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.enums.AlertConfig
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.ui.components.AppAlertDialog
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_14spLink
import com.soho.sohoapp.live.ui.components.TopAppBarProfile
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.theme.logoutRed
import org.koin.compose.koinInject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ProfileScreen(
    vmProfile: ProfileViewModel = koinInject(),
    navController: NavHostController,
) {
    val sProfile = vmProfile.mState.value
    var isShowAlert by remember { mutableStateOf(false) }

    //Display alert
    LaunchedEffect(sProfile) {
        isShowAlert = sProfile.alertState is AlertState.Display
    }

    //Show Alert when logout
    if (isShowAlert) {
        AppAlertDialog(
            alert = AlertConfig.SIGN_OUT_ALERT.apply {
                isConfirm = true
            },
            onConfirm = {
                vmProfile.onTriggerEvent(ProfileEvent.LogoutDismissAlert)
            },
            onDismiss = {
                vmProfile.onTriggerEvent(ProfileEvent.DismissAlert)
            })
    }

    MainContent(vmProfile, sProfile, navController)
}

@Composable
private fun MainContent(
    vmProfile: ProfileViewModel,
    sProfile: ProfileState,
    navCont: NavHostController
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        val (topAppBar, content, text) = createRefs()

        //action bar
        TopAppBarProfile(name = sProfile.profileName.orEmpty(),
            imageUrl = sProfile.profileImage,
            modifier = Modifier.constrainAs(topAppBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        //content
        Box(modifier = Modifier
            .fillMaxSize()
            .constrainAs(content) {
                top.linkTo(topAppBar.bottom)
                bottom.linkTo(text.top)
                height = Dimension.fillToConstraints
            }
            .padding(16.dp)) {

            Column {
                Text700_14spLink(name = "Terms", onClick = {
                    openWebView(
                        title = "Terms And Conditions",
                        url = "https://soho.com.au/articles/terms-and-conditions",
                        navCont = navCont
                    )
                })

                SpacerUp(size = 24.dp)

                Text700_14spLink(name = "Privacy Policy", onClick = {
                    openWebView(
                        title = "Privacy Policy",
                        url = "https://soho.com.au/articles/privacy",
                        navCont = navCont
                    )
                })

                SpacerUp(size = 24.dp)

                Text700_14spLink(name = "Support", onClick = {
                    openWebView(
                        title = "Support",
                        url = "https://support.soho.com.au/hc/en-us",
                        navCont = navCont
                    )
                })

                SpacerUp(size = 24.dp)

                LogoutView(onLogout = {
                    vmProfile.showLogoutConfirm()
                })
            }
        }

        //Version View
        Text400_14sp(info = "Version ${sProfile.appVersion}",
            txtAlign = TextAlign.Center,
            color = HintGray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .constrainAs(text) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
    }
}

@Composable
fun LogoutView(onLogout: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onLogout()
            }
    ) {
        Image(painter = painterResource(id = R.drawable.ic_log_out), contentDescription = "logout")
        SpacerSide(size = 8.dp)
        Text700_14spLink(name = "Logout", color = logoutRed, onClick = { onLogout() })
    }
}

private fun openWebView(title: String, url: String, navCont: NavHostController) {
    val encodeUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    navCont.navigate("${NavigationPath.WEB_VIEW.name}/$title/$encodeUrl")
}

@Preview(showBackground = true)
@Composable
fun PreviewProfile() {
    ProfileScreen(
        navController = NavHostController(LocalContext.current),
        vmProfile = ProfileViewModel(dataStore = AppDataStoreManager(LocalContext.current))
    )
}