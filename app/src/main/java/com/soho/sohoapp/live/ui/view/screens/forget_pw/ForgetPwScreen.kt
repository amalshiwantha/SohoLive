package com.soho.sohoapp.live.ui.view.screens.forget_pw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.FieldConfig
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.ui.components.AppAlertDialog
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.ButtonColouredProgress
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.TextError
import com.soho.sohoapp.live.ui.components.TextFieldWhiteEmail
import com.soho.sohoapp.live.ui.components.TextLabelWhite14
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.BgGradientPurpleLight
import com.soho.sohoapp.live.ui.view.screens.signin.SignInEvent
import com.soho.sohoapp.live.ui.view.screens.signin.SignInState
import com.soho.sohoapp.live.ui.view.screens.signin.SignInViewModel
import com.soho.sohoapp.live.ui.view.screens.signin.isErrorOnFiled
import com.soho.sohoapp.live.utility.TrackPwResetRequest
import org.koin.compose.koinInject

@Composable
fun ForgetPwScreen(
    modifier: Modifier = Modifier,
    vmSignIn: SignInViewModel = koinInject(),
    navController: NavHostController
) {
    val stateVm = vmSignIn.mStateLogin.value
    val scrollState = rememberScrollState()

    //if password reset is done then open the login screen
    LaunchedEffect(key1 = stateVm.isPasswordReset) {
        if (stateVm.isPasswordReset) {
            navController.navigate(NavigationPath.SIGNIN.name) {
                popUpTo(NavigationPath.FORGET_PW.name) { inclusive = true }
                popUpTo(NavigationPath.PRE_ACCESS.name) { inclusive = true }
            }
        }
    }

    //if successfully sent the ForgetPwLink then open next success screen
    LaunchedEffect(key1 = stateVm.isForgetPwLinkSent) {
        if (stateVm.isForgetPwLinkSent) {
            stateVm.request.email?.let { TrackPwResetRequest(it) }
            navController.navigate(NavigationPath.FORGET_PW_SENT.name) {
                popUpTo(NavigationPath.FORGET_PW.name) { inclusive = true }
            }
        }
    }

    //Display alert
    if (stateVm.alertState is AlertState.Display) {
        val alertConfig = stateVm.alertState.config

        AppAlertDialog(alert = alertConfig, onConfirm = {
            vmSignIn.onTriggerEvent(SignInEvent.DismissAlert)
        }, onDismiss = {
            vmSignIn.onTriggerEvent(SignInEvent.DismissAlert)
        })
    }

    Scaffold(
        containerColor = BgGradientPurpleLight,
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.forget_pw_title),
                onBackClick = { navController.popBackStack() }, onRightClick = {})
        }
    ) { innerPadding ->

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
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.Top
                ) {
                    FPwForm(stateVm, onTextChange = {
                        vmSignIn.onTriggerEvent(SignInEvent.OnForgetPWRequest(it))
                    })
                }

                SpacerUp(24.dp)
                BtnOpenEmailBtn(modifier, progressState = stateVm.loadingState, onSendClick = {
                    vmSignIn.onTriggerEvent(SignInEvent.CallForgetPassword)
                })
            }
        }
    }
}


@Composable
private fun FPwForm(stateVm: SignInState, onTextChange: (SignInRequest) -> Unit) {
    val requestData = stateVm.request
    val errorState = stateVm.errorStates

    Column(modifier = Modifier.fillMaxSize()) {

        Text400_14sp(info = "If you have an existing account, we will send a password reset link to your email")
        SpacerUp(size = 24.dp)

        TextLabelWhite14(label = stringResource(R.string.email))
        SpacerUp(8.dp)

        TextFieldWhiteEmail(modifier = Modifier.testTag("emailField"),
            fieldConfig = FieldConfig.DONE.apply {
                isError = isErrorOnFiled(errorState, FieldType.LOGIN_EMAIL)
                placeholder = stringResource(R.string.email)
                keyboardType = KeyboardType.Email
            }, onTextChange = {
                requestData.apply { email = it }
                onTextChange(requestData)
            })

        //error email visibility
        errorState[FieldType.LOGIN_EMAIL]?.let {
            if (it.isNotEmpty()) {
                TextError(errorMsg = it)
            }
        }
    }
}

@Composable
private fun BtnOpenEmailBtn(
    modifier: Modifier,
    progressState: ProgressBarState,
    onSendClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        val isLoading = progressState == ProgressBarState.Loading
        ButtonColouredProgress(
            text = stringResource(R.string.forget_pw_link),
            color = AppGreen,
            isLoading = isLoading
        ) {
            onSendClick()
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    ForgetPwScreen(navController = NavHostController(LocalContext.current))
}