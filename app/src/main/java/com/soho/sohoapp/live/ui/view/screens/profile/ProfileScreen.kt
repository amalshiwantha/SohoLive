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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.datastore.AppDataStoreManager
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text700_14spLink
import com.soho.sohoapp.live.ui.components.Text800_20sp
import com.soho.sohoapp.live.ui.components.TopAppBarProfile
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.logoutRed
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    vmProfile: ProfileViewModel = koinInject(),
    navController: NavHostController,
) {
    val sProfile = vmProfile.mState.value

    MainContent(vmProfile, sProfile, navController)
}

@Composable
private fun MainContent(
    vmProfile: ProfileViewModel,
    sProfile: ProfileState,
    navController: NavHostController
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        val (topAppBar, content, text) = createRefs()

        //action bar
        TopAppBarProfile(name = sProfile.profileName.orEmpty(),
            imageUrl = sProfile.profileImage.orEmpty(),
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
                    //navController.navigate("terms")
                })

                SpacerUp(size = 24.dp)

                Text700_14spLink(name = "Privacy Policy", onClick = {
                    //navController.navigate("terms")
                })

                SpacerUp(size = 24.dp)

                Text700_14spLink(name = "Support", onClick = {
                    //navController.navigate("terms")
                })

                SpacerUp(size = 24.dp)

                LogoutView(onLogout = {
                    vmProfile.logout()
                })
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
        Text700_14spLink(name = "Logout", color = logoutRed, onClick = {})
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