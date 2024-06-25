package com.soho.sohoapp.live.ui.view.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.theme.AppPrimaryDark
import com.soho.sohoapp.live.ui.theme.SohoLiveTheme
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : ComponentActivity() {

    companion object {

        const val RESULT_ERROR = 102
        const val EXTRA_DATA_FB = "extraDataFb"

        fun getInstance(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val callbackManager: CallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainViewModel by viewModels()

        enableEdgeToEdge()

        setContent {
            SohoLiveTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    //MainViw
                    ChangeSystemTrayColor()
                    //AppNavHost()

                    //TEst FB
                    printHashKey(LocalContext.current)
                    setupLogin()
                    LoginViewComponent()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupLogin() {
        LoginManager.getInstance()
            .registerCallback(callbackManager, callback = object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d("myFBData", "facebook login cancelled")
                    /*setResult(RESULT_CANCELED)
                    finish()*/
                }

                override fun onError(error: FacebookException) {
                    Log.d("myFBData", "facebook login error")
                    /*setResult(RESULT_ERROR)
                    finish()*/
                }

                override fun onSuccess(result: LoginResult) {
                    Log.d("myFBData", "facebook login success")
                    /*val intent = Intent().apply {
                        putExtra(EXTRA_DATA_FB, result.accessToken)
                    }
                    setResult(RESULT_OK, intent)
                    finish()*/
                }
            })
    }

    @Composable
    fun LoginViewComponent() {
        val context = LocalContext.current

        val facebookSignRequest =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val data = result.data?.getStringExtra(EXTRA_DATA_FB)
                    println("myFBData " + data.toString())
                }
            }

        Column {

            OutlinedButton(
                onClick = {
                    //facebookSignRequest.launch(getInstance(context))
                    doLogin()
                },
                shape = RoundedCornerShape(20),
                border = BorderStroke(1.dp, color = Color.Red),
                modifier = Modifier
                    .padding(8.dp)
                    .height(60.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_facebook),
                    contentDescription = "facebook login",
                    colorFilter = ColorFilter.tint(color = AppPrimaryDark.copy(alpha = .7f))
                )
            }

            Button(onClick = { doLoginOut() }) {
                Text(text = "Logout")
            }
        }
    }

    private fun doLogin() {
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            listOf("email", "public_profile")
        )
    }

    private fun doLoginOut() {
        LoginManager.getInstance().logOut()
    }

    @Composable
    private fun ChangeSystemTrayColor() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = false

        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }

    private fun printHashKey(context: Context) {
        try {
            val info: PackageInfo = context.packageManager
                .getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.d("hashkey", "Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("Error", "${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("Exception", "${e.localizedMessage}")
        }
    }
}