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
import com.facebook.GraphResponse
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.enums.CategoryType
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.model.CategoryInfo
import com.soho.sohoapp.live.model.Profile
import com.soho.sohoapp.live.model.SocialMediaProfile
import com.soho.sohoapp.live.ui.view.activity.main.MainViewModel
import com.soho.sohoapp.live.ui.view.screens.signin.gauth.GoogleApiContract
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

//Logout SM
fun doLogout(smProfile: SocialMediaProfile) {
    when (smProfile.smInfo.name) {
        SocialMediaInfo.YOUTUBE.name -> {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("myGuser signed out successfully.")
                } else {
                    println("myGuser Sign out failed.")
                }
            }
        }

        SocialMediaInfo.FACEBOOK.name -> {
            LoginManager.getInstance().logOut()
            println("myFB logged out from Facebook.")
        }

        SocialMediaInfo.LINKEDIN.name -> {}
    }
}


//Google Auth
@Composable
fun doConnectGoogle(viewMMain: MainViewModel): ManagedActivityResultLauncher<Int, Task<GoogleSignInAccount>?> {

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
            try {
                val gsa = task?.getResult(ApiException::class.java)
                gsa?.let { account ->
                    val authCode = account.serverAuthCode
                    authCode?.let { auth -> getTokens(auth, viewMMain, account) }
                } ?: run {
                    println("myGuser gsa null ")
                }
            } catch (e: ApiException) {
                println("myGuser error " + e.toString())
            }
        }

    return authResultLauncher
}

private fun getTokens(authCode: String, viewMMain: MainViewModel, gsa: GoogleSignInAccount) {
    val clientId = context.getString(R.string.google_client_id)
    val clientSecret = context.getString(R.string.google_client_secret)
    val redirectUri = "" // Redirect URI, usually "urn:ietf:wg:oauth:2.0:oob"
    val grantType = "authorization_code"

    val url = "https://oauth2.googleapis.com/token"
    val requestBody = FormBody.Builder()
        .add("code", authCode)
        .add("client_id", clientId)
        .add("client_secret", clientSecret)
        .add("redirect_uri", redirectUri)
        .add("grant_type", grantType)
        .build()

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("myGuser getTokensErrr " + e.toString())
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.body?.string()?.let {
                val jsonObject = JSONObject(it)

                val accessToken = jsonObject.getString("access_token")
                val refreshToken = jsonObject.getString("refresh_token")

                val profileGoogle = Profile(
                    fullName = gsa.displayName,
                    imageUrl = gsa.photoUrl.toString(),
                    email = gsa.email,
                    token = accessToken,
                    type = SocialMediaInfo.YOUTUBE,
                    isConnected = true
                )

                val smProfile = SocialMediaProfile(SocialMediaInfo.YOUTUBE, profileGoogle)
                viewMMain.saveSMProfile(smProfile)

                println("myGuser profile " + profileGoogle)
            }
        }
    })
}

//Facebook
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun DoConnectFacebook(viewMMain: MainViewModel) {
    val callbackManager = CallbackManager.Factory.create()
    val loginManager = LoginManager.getInstance()
    val context = LocalContext.current

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {

                val accessToken = result.accessToken

                if (!accessToken.isExpired) {

                    GlobalScope.launch(Dispatchers.Main) {

                        getFBProfile(accessToken) { fbProfile ->
                            fbProfile?.let {

                                getFbPages(accessToken, callback = { pagesList ->

                                    getFBGroups(accessToken, callback = { groupsList ->

                                        val smProfile = SocialMediaProfile(
                                            smInfo = SocialMediaInfo.FACEBOOK,
                                            profile = it,
                                            timelines = getFBTimeLine(it),
                                            pages = pagesList,
                                            groups = groupsList
                                        )

                                        viewMMain.saveSMProfile(smProfile)
                                    })
                                })
                            }
                        }
                    }
                }

            }

            private fun getFBTimeLine(it: Profile): MutableList<CategoryInfo> {
                return mutableListOf(
                    CategoryInfo(
                        index = 1001,
                        id = "1",
                        type = CategoryType.TIMELINE,
                        title = it.fullName ?: "FB User",
                        url = "",
                        imageUrl = it.imageUrl.orEmpty(),
                        accessToken = it.token.orEmpty(),
                    )
                )
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
        context as ActivityResultRegistryOwner, callbackManager, permissionList
    )
}

private fun getFBProfile(accessToken: AccessToken, callback: (Profile?) -> Unit) {
    val request = GraphRequest.newMeRequest(
        accessToken
    ) { jsonObject: JSONObject?, response: GraphResponse? ->
        try {
            jsonObject?.let {
                val id = it.getString("id")
                val name = it.getString("name")
                val email = it.optString("email", "")
                val pictureUrl = "https://graph.facebook.com/$id/picture?type=large"

                val profile = Profile(
                    fullName = name,
                    imageUrl = pictureUrl,
                    email = email,
                    token = accessToken.token,
                    type = SocialMediaInfo.FACEBOOK,
                    isConnected = true
                )

                callback(profile)
            } ?: run {
                callback(null)
            }
        } catch (e: Exception) {
            Log.e("myFb profielError", e.toString())
            e.printStackTrace()
            callback(null)
        }
    }

    val parameters = Bundle()
    parameters.putString("fields", "id,name,email")
    request.parameters = parameters
    request.executeAsync()
}

fun getFbPages(accessToken: AccessToken, callback: (MutableList<CategoryInfo>) -> Unit) {
    val request = GraphRequest.newGraphPathRequest(
        accessToken, "/me/accounts"
    ) { response ->
        val pagesList = mutableListOf<CategoryInfo>()
        try {
            val jsonObject = response.jsonObject
            jsonObject?.let {
                val dataArray = it.getJSONArray("data")
                for (i in 0 until dataArray.length()) {
                    val pageObj = dataArray.getJSONObject(i)
                    val pageId = pageObj.getString("id")
                    val pageName = pageObj.getString("name")
                    val pageAccessToken = pageObj.getString("access_token")
                    val pagePicture =
                        pageObj.getJSONObject("picture").getJSONObject("data").getString("url")

                    pagesList.add(
                        CategoryInfo(
                            index = 100 + i,
                            id = pageId,
                            type = CategoryType.PAGES,
                            title = pageName,
                            url = "",
                            imageUrl = pagePicture,
                            accessToken = pageAccessToken,
                        )
                    )
                }
                callback(pagesList)
            }
        } catch (e: Exception) {
            Log.e("myFb pageError", e.toString())
            e.printStackTrace()
            callback(mutableListOf())
        }
    }

    val parameters = Bundle()
    parameters.putString("fields", "id,name,access_token,picture.type(large)")
    request.parameters = parameters
    request.executeAsync()
}


fun getFBGroups(accessToken: AccessToken, callback: (MutableList<CategoryInfo>) -> Unit) {
    val request = GraphRequest.newGraphPathRequest(
        accessToken, "/me/groups"
    ) { response ->
        val groupsList = mutableListOf<CategoryInfo>()
        try {
            val jsonObject = response.jsonObject
            jsonObject?.let {
                val dataArray = jsonObject.getJSONArray("data")
                for (i in 0 until dataArray.length()) {
                    val group = dataArray.getJSONObject(i)
                    val groupId = group.getString("id")
                    val groupName = group.getString("name")
                    val groupPicture =
                        group.getJSONObject("picture").getJSONObject("data").getString("url")

                    groupsList.add(
                        CategoryInfo(
                            index = 200 + i,
                            id = groupId,
                            type = CategoryType.GROUPS,
                            title = groupName,
                            url = "",
                            imageUrl = groupPicture,
                            accessToken = "",
                        )
                    )
                }
            }

            callback(groupsList)

        } catch (e: Exception) {
            Log.e("myFb groupError", e.toString())
            e.printStackTrace()
            callback(mutableListOf())
        }
    }

    val parameters = Bundle()
    parameters.putString("fields", "id,name,picture.type(large)")
    request.parameters = parameters
    request.executeAsync()
}