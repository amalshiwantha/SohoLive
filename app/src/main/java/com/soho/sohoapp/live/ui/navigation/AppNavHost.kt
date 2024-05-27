package com.soho.sohoapp.live.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soho.sohoapp.live.view.screens.home.HomeScreen
import com.soho.sohoapp.live.view.screens.splash.SplashScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationPath.SPLASH.name) {
        composable(route = NavigationPath.SPLASH.name) {
            SplashScreen(navController = navController)
        }
        composable(route = NavigationPath.SIGNIN.name) {
            SplashScreen(navController = navController)
        }
        composable(route = NavigationPath.SIGNUP.name) {
            SplashScreen(navController = navController)
        }
        composable(route = NavigationPath.HOME.name) {
            HomeScreen(navController = navController)
        }
    }
}