package com.soho.sohoapp.live.view.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.soho.sohoapp.live.view.screens.home.HomeScreen
import com.soho.sohoapp.live.view.screens.pre_access.PreAccessScreen
import com.soho.sohoapp.live.view.screens.signin.SignInScreen
import com.soho.sohoapp.live.view.screens.signup.SignupScreen
import com.soho.sohoapp.live.view.screens.splash.SplashScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    /*LaunchedEffect(Unit) {
        if (currentRoute == NavigationPath.SIGNIN.name) {
            navController.navigate(NavigationPath.HOME.name) {
                popUpTo(NavigationPath.SPLASH.name) { inclusive = true }
                popUpTo(NavigationPath.SPLASH.name) { inclusive = true }
            }
        }
    }*/

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
            SignupScreen(navController = navController)
        }
        composable(route = NavigationPath.HOME.name) {
            HomeScreen(navController = navController)
        }
    }
}