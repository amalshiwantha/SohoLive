package com.soho.sohoapp.live.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.model.GoLiveSubmit
import com.soho.sohoapp.live.ui.view.activity.main.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.forget_pw.ForgetPwScreen
import com.soho.sohoapp.live.ui.view.screens.home.HomeScreen
import com.soho.sohoapp.live.ui.view.screens.player.PlayerScreen
import com.soho.sohoapp.live.ui.view.screens.pre_access.PreAccessScreen
import com.soho.sohoapp.live.ui.view.screens.signin.SignInScreen
import com.soho.sohoapp.live.ui.view.screens.signup.SignUpScreen
import com.soho.sohoapp.live.ui.view.screens.splash.SplashScreen
import com.soho.sohoapp.live.ui.view.screens.video_recorder.VideoRecorderScreen

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

        //TEMP
        composable(
            route = "${NavigationPath.PLAYER.name}/{uri}",
            arguments = listOf(navArgument("uri") { type = NavType.StringType })
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("uri")
            val uri = uriString?.let { Uri.parse(it) }
            uri?.let {
                PlayerScreen(
                    mGState = GlobalState(),
                    navController = navController,
                    fileUri = it,
                    onNextClick = {
                        navController.navigate(NavigationPath.REVIEW.name) {
                            popUpTo(NavigationPath.REVIEW.name) {
                                inclusive = true
                            }
                        }
                    })
            }
        }
    }
}