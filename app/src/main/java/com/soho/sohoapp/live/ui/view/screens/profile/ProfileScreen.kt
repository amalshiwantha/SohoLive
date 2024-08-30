package com.soho.sohoapp.live.ui.view.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.Text800_20sp
import com.soho.sohoapp.live.ui.components.TextWhite12
import com.soho.sohoapp.live.ui.components.TopAppBarProfile
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppRed
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    vmProfile: ProfileViewModel = koinInject(),
    navController: NavHostController,
) {
    val sProfile = vmProfile.mState.value

    MainContent(vmProfile, sProfile)
}

@Composable
private fun MainContent(vmProfile: ProfileViewModel, sProfile: ProfileState) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        val (topAppBar, content, text) = createRefs()

        //action bar
        TopAppBarProfile(title = "Profile",
            rightIcon = R.drawable.ic_cross,
            modifier = Modifier.constrainAs(topAppBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onCloseClick = { })

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
                TextWhite12(title = "Terms")
                TextWhite12(title = "Privacy Policy")
                TextWhite12(title = "Support")
                ButtonColoured(text = "Logout", color = AppRed) {
                }
            }
        }

        //Bottom Button
        Text800_20sp(label = "Version ${sProfile.appVersion}", modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .constrainAs(text) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfile() {
    ProfileScreen(
        navController = NavHostController(LocalContext.current),
        vmProfile = ProfileViewModel(dataStore = AppDataStoreManager(LocalContext.current))
    )
}