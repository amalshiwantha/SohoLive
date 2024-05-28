package com.soho.sohoapp.live.view.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soho.sohoapp.live.view.screens.home.HomeScreen
import com.soho.sohoapp.live.view.screens.pre_access.PreAccessScreen
import com.soho.sohoapp.live.view.screens.signin.SignInScreen
import com.soho.sohoapp.live.view.screens.signup.SignUpScreen
import com.soho.sohoapp.live.view.screens.splash.SplashScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationPath.SIGNUP.name) {
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
        composable(route = NavigationPath.HOME.name) {
            HomeScreen(navController = navController)
        }
    }
}