package com.soho.sohoapp.live.view.screens.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.AlertDialogConfig
import com.soho.sohoapp.live.enums.FieldConfig
import com.soho.sohoapp.live.network.common.AlertState
import com.soho.sohoapp.live.network.common.ProgressBarState
import com.soho.sohoapp.live.utility.NetworkUtils
import com.soho.sohoapp.live.view.ui.components.AppAlertDialog
import com.soho.sohoapp.live.view.ui.components.AppTopBar
import com.soho.sohoapp.live.view.ui.components.ButtonColoured
import com.soho.sohoapp.live.view.ui.components.PasswordTextFieldWhite
import com.soho.sohoapp.live.view.ui.components.SpacerVertical
import com.soho.sohoapp.live.view.ui.components.TextButtonBlue
import com.soho.sohoapp.live.view.ui.components.TextFieldWhite
import com.soho.sohoapp.live.view.ui.components.TextLabelWhite14
import com.soho.sohoapp.live.view.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.view.ui.navigation.NavigationPath
import com.soho.sohoapp.live.view.ui.theme.AppGreen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    vmSignIn: SignInViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val stateVm = vmSignIn.state.value
    val snackBarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    //if login success then open home screen
    LaunchedEffect(key1 = stateVm.isLoginSuccess) {
        if (stateVm.isLoginSuccess) {
            navController.navigate(NavigationPath.HOME.name) {
                popUpTo(NavigationPath.SIGNIN.name) { inclusive = true }
                popUpTo(NavigationPath.PRE_ACCESS.name) { inclusive = true }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarState) },
        topBar = {
            AppTopBar(
                title = stringResource(R.string.signin_title),
                onBackClick = { navController.popBackStack() })
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

                    //Display login form
                    LoginForm(vmSignIn, stateVm) {
                        navController.navigate(NavigationPath.FORGET_PW.name)
                    }

                    //Display progress bar
                    if (stateVm.loadingState is ProgressBarState.Loading) {
                        CircularProgressIndicator()
                    }

                    //Display alert
                    if (stateVm.alertState is AlertState.Display) {
                        AppAlertDialog(
                            alert = AlertDialogConfig.SIGN_IN_ERROR,
                            onConfirm = {
                                vmSignIn.onTriggerEvent(SignInEvent.DismissAlert)
                            },
                            onDismiss = {
                                vmSignIn.onTriggerEvent(SignInEvent.DismissAlert)
                            })
                    }
                }

                SpacerVertical(24.dp)

                //Display bottom login button
                BottomLoginBtn(modifier) {
                    if (netUtil.isNetworkAvailable()) {
                        vmSignIn.onTriggerEvent(SignInEvent.CallSignIn)
                    } else {
                        scope.launch {
                            snackBarState.showSnackbar(
                                message = context.getString(R.string.no_net),
                                actionLabel = context.getString(R.string.ok),
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginForm(viewModel: SignInViewModel, state: SignInState, onForgetPwClick: () -> Unit) {
    Column {
        val requestData = state.request

        TextLabelWhite14(label = stringResource(R.string.email))
        SpacerVertical(8.dp)
        TextFieldWhite(
            FieldConfig.NEXT.apply { placeholder = stringResource(R.string.email) },
            onTextChange = {
                requestData.apply { email = it }
                viewModel.onTriggerEvent(SignInEvent.OnUpdateRequest(requestData))
            })

        SpacerVertical(24.dp)

        TextLabelWhite14(label = stringResource(R.string.password))
        SpacerVertical(8.dp)
        PasswordTextFieldWhite(onTextChange = {
            requestData.apply { password = it }
            viewModel.onTriggerEvent(SignInEvent.OnUpdateRequest(requestData))
        })

        SpacerVertical(24.dp)

        TextButtonBlue(text = stringResource(R.string.forgot_password), modifier = Modifier
            .fillMaxWidth()
            .align(CenterHorizontally), onBtnClick = {
            onForgetPwClick()
        })
    }
}

@Composable
private fun BottomLoginBtn(modifier: Modifier, onBtnClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        ButtonColoured(text = stringResource(R.string.log_in),
            color = AppGreen,
            onBtnClick = {
                onBtnClick()
            })
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SignInScreen(navController = NavHostController(LocalContext.current))
}