package com.soho.sohoapp.live.ui.view.screens.webview

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.ui.components.AppTopBarCustom
import com.soho.sohoapp.live.ui.components.brushMainGradientBg

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(navController: NavHostController, title: String, url: String) {

    val webViewState by remember { mutableStateOf(url) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        val (topAppBar, content) = createRefs()

        //action bar
        AppTopBarCustom(title = title,
            modifier = Modifier.constrainAs(topAppBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onBackClick = { navController.popBackStack() })

        //content
        Box(modifier = Modifier
            .fillMaxSize()
            .constrainAs(content) {
                top.linkTo(topAppBar.bottom)
                height = Dimension.fillToConstraints
            }) {

            Column(modifier = Modifier.fillMaxWidth()) {
                AndroidView(
                    factory = {
                        WebView(context).apply {
                            webViewClient = object : WebViewClient() {
                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: android.webkit.WebResourceRequest?
                                ): Boolean {
                                    view?.loadUrl(request?.url.toString())
                                    return true
                                }
                            }
                            settings.javaScriptEnabled = true
                            loadUrl(webViewState)
                        }
                    },
                    update = {
                        it.loadUrl(webViewState)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            }
        }
    }
}
