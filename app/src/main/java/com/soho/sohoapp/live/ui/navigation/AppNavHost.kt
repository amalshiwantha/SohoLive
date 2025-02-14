package com.soho.sohoapp.live.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.soho.sohoapp.live.ui.view.activity.main.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.forget_pw.ForgetPwScreen
import com.soho.sohoapp.live.ui.view.screens.forget_pw.ForgetPwSentScreen
import com.soho.sohoapp.live.ui.view.screens.forget_pw.SetPwScreen
import com.soho.sohoapp.live.ui.view.screens.home.HomeScreen
import com.soho.sohoapp.live.ui.view.screens.pre_access.PreAccessScreen
import com.soho.sohoapp.live.ui.view.screens.signin.SignInScreen
import com.soho.sohoapp.live.ui.view.screens.signup.SignUpScreen
import com.soho.sohoapp.live.ui.view.screens.splash.SplashScreen
import com.soho.sohoapp.live.ui.view.screens.webview.WebViewScreen

@Composable
fun AppNavHost(viewMMain: MainViewModel) {
    val navController = rememberNavController()

    // Handle deep link intent and navigate
    val isOpenResetPwScreen by viewMMain.isOpenResetPw.collectAsState()

    LaunchedEffect(isOpenResetPwScreen) {
        if (isOpenResetPwScreen) {
            viewMMain.deepLinkToken.value?.let {
                navController.navigate("${NavigationPath.RESET_PASSWORD.name}/${Uri.encode(it)}")
            }
        }
    }

    //Main Navigation
    NavHost(navController = navController, startDestination = NavigationPath.SPLASH.name) {
        composable(route = NavigationPath.SPLASH.name) {
            SplashScreen(navController = navController)
        }
        composable(route = NavigationPath.PRE_ACCESS.name) {
            PreAccessScreen(navController = navController)
        }
        composable(route = NavigationPath.SIGNIN.name) {
            SignInScreen(navController = navController)
        }
        composable(route = NavigationPath.SIGNUP.name) {
            SignUpScreen(navController = navController)
        }
        composable(route = NavigationPath.FORGET_PW.name) {
            ForgetPwScreen(navController = navController)
        }
        composable(route = NavigationPath.HOME.name) {
            HomeScreen(navControllerHome = navController, viewMMain = viewMMain)
        }
        composable(
            route = "${NavigationPath.WEB_VIEW_MAIN.name}/{title}/{url}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("url") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Extract the arguments from the back stack entry
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val url = backStackEntry.arguments?.getString("url") ?: ""

            // Pass the arguments to the WebViewScreen
            WebViewScreen(navController = navController, title = title, url = url)
        }
        composable(route = NavigationPath.FORGET_PW_SENT.name) {
            ForgetPwSentScreen(navController = navController)
        }
        composable(
            route = "${NavigationPath.RESET_PASSWORD.name}/{login_token}",
            arguments = listOf(navArgument("login_token") {
                type = NavType.StringType; nullable = true
            })
        ) { backStackEntry ->
            val loginToken = backStackEntry.arguments?.getString("login_token")
            SetPwScreen(navController = navController, resetToken = loginToken)
        }
    }
}