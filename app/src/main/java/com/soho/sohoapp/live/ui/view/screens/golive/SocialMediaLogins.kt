package com.soho.sohoapp.live.ui.view.screens.golive

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.SMProfile
import com.soho.sohoapp.live.ui.view.activity.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.signin.gauth.GoogleApiContract
import com.soho.sohoapp.live.ui.view.screens.signin.gauth.GoogleUserModel
import com.soho.sohoapp.live.utility.AppEvent
import com.soho.sohoapp.live.utility.AppEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Google Auth
@Composable
fun doConnectGoogle(viewMMain: MainViewModel): ManagedActivityResultLauncher<Int, Task<GoogleSignInAccount>?> {
    val state = viewMMain.googleUser.observeAsState()
    val user = state.value

    val isError = rememberSaveable { mutableStateOf(false) }

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
            try {
                val gsa = task?.getResult(ApiException::class.java)
                println("myGuser " + gsa?.photoUrl)
                println("myGuser " + gsa?.email)

                if (gsa != null) {
                    viewMMain.fetchGoogleUser(gsa.email, gsa.displayName)
                    viewMMain.googleSignOut()
                } else {
                    isError.value = true
                }
            } catch (e: ApiException) {
                println("myGuser error " + e.toString())
            }
        }

    //Check onLoad Google Auth state
    if (viewMMain.googleUser.value != null) {
        LaunchedEffect(key1 = Unit) {
            val guser = GoogleUserModel(
                email = user?.email,
                name = user?.name,
            )
            println("myGuser Done SignedIn " + guser)
        }
    }

    return authResultLauncher
}

//Facebook
@Composable
fun DoConnectFacebook() {
    val callbackManager = CallbackManager.Factory.create()
    val loginManager = LoginManager.getInstance()
    val context = LocalContext.current

    DisposableEffect(Unit) {
        loginManager.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    println("myFB onCancel")
                }

                override fun onError(error: FacebookException) {
                    println("myFB onErr " + error)
                }

                override fun onSuccess(result: LoginResult) {
                    getFBProfileData(result)
                }
            })
        onDispose {
            println("myFB unregisterCallback")
            //loginManager.unregisterCallback(callbackManager)
        }
    }

    //listOf("email","public_profile","publish_video","pages_read_engagement","pages_manage_posts","pages_show_list")
    loginManager.logIn(
        context as ActivityResultRegistryOwner,
        callbackManager,
        listOf("email", "public_profile")
    )
}

private fun getFBProfileData(loginResult: LoginResult) {
    val request = GraphRequest.newMeRequest(loginResult.accessToken) { info, response ->
        Log.e("myFb info", info.toString())

        try {
            info?.let {
                val userId = it.getString("id")
                val profilePicture =
                    "https://graph.facebook.com/$userId/picture?width=500&height=500"
                val firstName = if (it.has("first_name")) it.getString("first_name") else ""
                val lastName = if (it.has("last_name")) it.getString("last_name") else ""
                val email = if (it.has("email")) it.getString("email") else ""
                val fullName = "$firstName $lastName"

                val profile = SMProfile(
                    fullName,
                    profilePicture,
                    email,
                    loginResult.accessToken.token,
                    SocialMediaInfo.FACEBOOK
                )

                // get refresh token if expire
                // get pages using accessToken

                // Send an event
                CoroutineScope(Dispatchers.Main).launch {
                    AppEventBus.sendEvent(AppEvent.SaveSMProfile(profile))
                }
            }
        } catch (e: Exception) {
            Log.e("myFb infoError", e.toString())
            e.printStackTrace()
        }
    }

    val parameters = Bundle().apply {
        putString("fields", "id, first_name, last_name, email, birthday, gender")
    }
    request.parameters = parameters
    request.executeAsync()
}