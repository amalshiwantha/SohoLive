package com.soho.sohoapp.live.ui.view.screens.golive

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.facebook.AccessToken
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
import com.soho.sohoapp.live.model.CategoryInfo
import com.soho.sohoapp.live.model.Profile
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.ui.view.activity.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.signin.gauth.GoogleApiContract

//Google Auth
@Composable
fun doConnectGoogle(viewMMain: MainViewModel): ManagedActivityResultLauncher<Int, Task<GoogleSignInAccount>?> {

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
            try {
                val gsa = task?.getResult(ApiException::class.java)

                if (gsa != null) {
                    val profileGoogle = Profile(
                        fullName = gsa.displayName,
                        imageUrl = gsa.photoUrl.toString(),
                        email = gsa.email,
                        token = gsa.idToken,
                        type = SocialMediaInfo.YOUTUBE,
                        isConnected = true
                    )
                    val smProfile = SocialMediaProfile(SocialMediaInfo.YOUTUBE, profileGoogle)
                    viewMMain.saveSMProfile(smProfile)
                    viewMMain.googleSignOut()
                } else {
                    //show error on gAuth
                }
            } catch (e: ApiException) {
                println("myGuser error " + e.toString())
            }
        }

    return authResultLauncher
}

//Facebook
@Composable
fun DoConnectFacebook(viewMMain: MainViewModel) {
    val callbackManager = CallbackManager.Factory.create()
    val loginManager = LoginManager.getInstance()
    val context = LocalContext.current

    DisposableEffect(Unit) {
        loginManager.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {

                    val accessToken = result.accessToken

                    getFbPages(accessToken, callback = { pagesList ->
                        println("myFB pageList " + pagesList.size)
                        for (pageName in pagesList) {
                            println("myFB pages " + pageName)
                        }
                    })

                    getFBGroups(accessToken, callback = { groupsList ->
                        println("myFB groupsList " + groupsList.size)
                        for (group in groupsList) {
                            println("myFB groups " + group)
                        }
                    })

                    /*getFBProfileData(result, onProfileFound = { fbProfile ->
                        fbProfile?.let {
                            val timeLine = getFBTimeLine(it)
                            val smProfile = SocialMediaProfile(
                                smInfo = SocialMediaInfo.FACEBOOK,
                                profile = it,
                                timelines = timeLine
                            )
                            viewMMain.saveSMProfile(smProfile)
                        } ?: run {
                            println("myFB Logged but Error")
                        }
                    })*/

                }

                private fun getFBTimeLine(it: Profile): MutableList<CategoryInfo> {

                    /*return mutableListOf(
                        CategoryInfo(
                            1001, CategoryType.TIMELINE,
                            it.fullName ?: "FB User", "", it.imageUrl ?: ""
                        ), CategoryInfo(
                            1002, CategoryType.TIMELINE,
                            "FB User", "", it.imageUrl ?: ""
                        )
                    )*/
                    return mutableListOf()
                }

                override fun onCancel() {
                    println("myFB onCancel")
                }

                override fun onError(error: FacebookException) {
                    println("myFB onErr " + error)
                }
            })
        onDispose {
            println("myFB unregisterCallback")
            //loginManager.unregisterCallback(callbackManager)
        }
    }

    val permissionList = listOf(
        "email",
        "public_profile",
        "publish_video",
        "pages_read_engagement",
        "pages_manage_posts",
        "pages_show_list"
    )
    loginManager.logIn(
        context as ActivityResultRegistryOwner,
        callbackManager,
        permissionList
    )
}

fun getFbPages(accessToken: AccessToken, callback: (List<String>) -> Unit) {
    val request = GraphRequest.newGraphPathRequest(
        accessToken,
        "/me/accounts"
    ) { response ->
        val pagesList = mutableListOf<String>()
        try {
            val jsonObject = response.jsonObject
            jsonObject?.let {
                val dataArray = it.getJSONArray("data")
                for (i in 0 until dataArray.length()) {
                    val pageObject = dataArray.getJSONObject(i)
                    val pageId = pageObject.getString("id")
                    val pageName = pageObject.getString("name")
                    val pageAccessToken = pageObject.getString("access_token")
                    val pagePicture =
                        pageObject.getJSONObject("picture").getJSONObject("data").getString("url")

                    pagesList.add(pageName)
                }
                callback(pagesList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback(emptyList())
        }
    }

    val parameters = Bundle()
    parameters.putString("fields", "id,name,access_token,picture.type(large)")
    request.parameters = parameters
    request.executeAsync()
}


fun getFBGroups(accessToken: AccessToken, callback: (List<String>) -> Unit) {
    val request = GraphRequest.newGraphPathRequest(
        accessToken,
        "/me/groups"
    ) { response ->
        val groupsList = mutableListOf<String>()
        try {
            val jsonObject = response.jsonObject
            jsonObject?.let {
                val dataArray = jsonObject.getJSONArray("data")
                for (i in 0 until dataArray.length()) {
                    val group = dataArray.getJSONObject(i)
                    val groupName = group.getString("name")
                    groupsList.add(groupName)
                }
            }

            callback(groupsList)

        } catch (e: Exception) {
            e.printStackTrace()
            callback(emptyList())
        }
    }

    val parameters = Bundle()
    parameters.putString("fields", "name")
    request.parameters = parameters
    request.executeAsync()
}


private fun getFBProfileData(loginResult: LoginResult, onProfileFound: (Profile?) -> Unit) {
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

                val profile = Profile(
                    fullName = fullName,
                    imageUrl = profilePicture,
                    email = email,
                    token = loginResult.accessToken.token,
                    type = SocialMediaInfo.FACEBOOK,
                    isConnected = true
                )

                onProfileFound(profile)

                // get refresh token if expire
                // get pages using accessToken
            }
        } catch (e: Exception) {
            Log.e("myFb infoError", e.toString())
            e.printStackTrace()
            onProfileFound(null)
        }
    }

    val parameters = Bundle().apply {
        putString("fields", "id, first_name, last_name, email, birthday, gender")
    }
    request.parameters = parameters
    request.executeAsync()
}