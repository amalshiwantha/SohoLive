package com.soho.sohoapp.live.ui.view.screens.forget_pw

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.PasswordTextFieldWhite
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text400_14spSingleLine
import com.soho.sohoapp.live.ui.components.TextError
import com.soho.sohoapp.live.ui.components.TextLabelWhite14
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.BgGradientPurpleLight
import com.soho.sohoapp.live.ui.theme.DurationDark
import com.soho.sohoapp.live.ui.view.screens.signin.SignInState
import com.soho.sohoapp.live.ui.view.screens.signin.SignInViewModel
import com.soho.sohoapp.live.ui.view.screens.signin.isErrorOnFiled
import org.koin.compose.koinInject

@Composable
fun SetPwScreen(
    modifier: Modifier = Modifier,
    vmSignIn: SignInViewModel = koinInject(),
    navController: NavHostController
) {
    val stateVm = vmSignIn.mStateLogin.value
    val scrollState = rememberScrollState()

    //if successfully sent the ForgetPwLink then open next success screen
    LaunchedEffect(key1 = stateVm.isForgetPwLinkSent) {
        if (stateVm.isForgetPwLinkSent) {
            navController.navigate(NavigationPath.FORGET_PW_SENT.name) {
                popUpTo(NavigationPath.FORGET_PW.name) { inclusive = true }
            }
        }
    }

    Scaffold(containerColor = BgGradientPurpleLight, modifier = modifier.fillMaxSize(), topBar = {
        AppTopBar(title = "Set your password",
            onBackClick = { navController.popBackStack() },
            onRightClick = {})
    }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brushMainGradientBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                        .verticalScroll(scrollState), verticalArrangement = Arrangement.Top
                ) {
                    SetPwForm(stateVm)
                }

                SpacerUp(24.dp)
                BtnUpdatePw(modifier, onSendClick = {
                    //vmSignIn.onTriggerEvent(SignInEvent.OnForgetPWRequest(stateVm.request))
                })
            }
        }
    }
}


@Composable
private fun SetPwForm(stateVm: SignInState) {
    val requestData = stateVm.request
    val errorState = stateVm.errorStates

    Column(modifier = Modifier.fillMaxSize()) {
        TextLabelWhite14(label = "New Password")
        SpacerUp(8.dp)
        PasswordTextFieldWhite(modifier = Modifier.testTag("passwordField"),
            isError = isErrorOnFiled(errorState, FieldType.LOGIN_PW),
            onTextChange = {
                requestData.apply { password = it }
                //viewModel.onTriggerEvent(SignInEvent.OnUpdateRequest(requestData))
            })
        //error pw visibility
        errorState[FieldType.LOGIN_PW]?.let {
            TextError(errorMsg = it)
        }

        SpacerUp(size = 24.dp)

        TextLabelWhite14(label = "Confirm Password")
        SpacerUp(8.dp)
        PasswordTextFieldWhite(modifier = Modifier.testTag("passwordField"),
            isError = isErrorOnFiled(errorState, FieldType.LOGIN_PW),
            onTextChange = {
                requestData.apply { confirm_password = it }
                //viewModel.onTriggerEvent(SignInEvent.OnUpdateRequest(requestData))
            })
        //error pw visibility
        errorState[FieldType.LOGIN_PW]?.let {
            TextError(errorMsg = it)
        }

        SpacerUp(size = 24.dp)

        TakeNote()
    }
}

@Composable
fun TakeNote() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = DurationDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {
                Image(painter = painterResource(id = R.drawable.ic_light), contentDescription = "")
                SpacerSide(size = 8.dp)
                TextLabelWhite14(label = "Take Note")
            }

            SpacerSide(size = 8.dp)
            Text400_14sp(
                info = "This password will also be use to login to other Soho related apps, including the Agent Portal and Soho Livecast.",
            )
        }
    }
}

@Composable
private fun BtnUpdatePw(modifier: Modifier, onSendClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        ButtonColoured(text = "Update Password", color = AppGreen, onBtnClick = { onSendClick() })
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SetPwScreen(navController = NavHostController(LocalContext.current))
}