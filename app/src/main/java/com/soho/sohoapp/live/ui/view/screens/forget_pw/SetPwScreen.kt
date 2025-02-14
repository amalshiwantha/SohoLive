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
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.PasswordTextFieldWhite
import com.soho.sohoapp.live.ui.components.SpacerUp
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
        PasswordTextFieldWhite(
            modifier = Modifier.testTag("passwordField"),
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
        PasswordTextFieldWhite(
            modifier = Modifier.testTag("passwordField"),
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
        ButtonColoured(text = "Update Password",
            color = AppGreen,
            onBtnClick = { onSendClick() })
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SetPwScreen(navController = NavHostController(LocalContext.current))
}