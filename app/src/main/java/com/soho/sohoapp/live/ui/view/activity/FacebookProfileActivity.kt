package com.soho.sohoapp.live.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.utility.printHashKey
import org.json.JSONException
import java.net.MalformedURLException

class FacebookProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginInstant: LoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_facebook_profile)

        printHashKey()

        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()
        loginInstant = LoginManager.getInstance()

        setupFirebaseFB()
    }

    private fun setupFirebaseFB() {
        loginInstant
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("myFb", "facebook:onSuccess:${loginResult}")
                    Log.v("myFb", "Token::" + loginResult.accessToken.token)
                    getFBProfileData(loginResult)
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d("myFb", "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("myFb", "facebook:onError", error)
                }
            })

        facebookLogin()
    }

    private fun facebookLogin() {
        loginInstant.logInWithReadPermissions(this, listOf("email", "public_profile"))
    }

    private fun facebookLogout() {
        loginInstant.logOut()
    }

    private fun getFBProfileData(loginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(loginResult.accessToken) { info, response ->
            Log.e("myFb info", info.toString())

            try {
                info?.let {
                    val userId = it.getString("id")
                    val profilePicture =
                        "https://graph.facebook.com/$userId/picture?width=500&height=500"
                    val firstName = if (it.has("first_name")) it.getString("first_name") else null
                    val lastName = if (it.has("last_name")) it.getString("last_name") else null

                    println("myFb bio : " + firstName)
                    println("myFb bio : " + lastName)
                    println("myFb bio : " + profilePicture)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
        }

        val parameters = Bundle().apply {
            putString("fields", "id, first_name, last_name, email, birthday, gender")
        }
        request.parameters = parameters
        request.executeAsync()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)

        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("myFb", "signInWithCredential:success")
                val user = auth.currentUser
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w("myFb", "signInWithCredential:failure", task.exception)
                updateUI(null)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        Log.d("myFb", "FirebaseUser:$user")
    }
}