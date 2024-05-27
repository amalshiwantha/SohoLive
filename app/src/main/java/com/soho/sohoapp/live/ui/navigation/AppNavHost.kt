package com.soho.sohoapp.live.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soho.sohoapp.live.view.screens.home.HomeScreen
import com.soho.sohoapp.live.view.screens.signin.SignInScreen
import com.soho.sohoapp.live.view.screens.signup.SignupScreen
import com.soho.sohoapp.live.view.screens.splash.SplashScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = NavigationPath.SPLASH.name) {
        composable(route = NavigationPath.SPLASH.name) {
            SplashScreen(navController = navController)
        }
        composable(route = NavigationPath.SIGNIN.name) {
            SignInScreen(navController = navController)
        }
        composable(route = NavigationPath.SIGNUP.name) {
            SignupScreen(navController = navController)
        }
        composable(route = NavigationPath.HOME.name) {
            HomeScreen(navController = navController)
        }
    }
}