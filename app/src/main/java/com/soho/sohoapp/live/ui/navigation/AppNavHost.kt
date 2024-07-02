package com.soho.sohoapp.live.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soho.sohoapp.live.ui.view.activity.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.forget_pw.ForgetPwScreen
import com.soho.sohoapp.live.ui.view.screens.home.HomeScreen
import com.soho.sohoapp.live.ui.view.screens.pre_access.PreAccessScreen
import com.soho.sohoapp.live.ui.view.screens.signin.SignInScreen
import com.soho.sohoapp.live.ui.view.screens.signup.SignUpScreen
import com.soho.sohoapp.live.ui.view.screens.splash.SplashScreen

@Composable
fun AppNavHost(viewMMain: MainViewModel) {
    val navController = rememberNavController()

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
    }
}