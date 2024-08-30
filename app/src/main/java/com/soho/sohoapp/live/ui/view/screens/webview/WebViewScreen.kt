package com.soho.sohoapp.live.ui.view.screens.webview

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.ui.components.AppTopBarCustom
import com.soho.sohoapp.live.ui.components.brushMainGradientBg

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(navController: NavHostController, title: String, url: String) {
    // State to track loading status
    val isLoading = remember { mutableStateOf(true) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        val (topAppBar, content, progressBar) = createRefs()

        // Action bar
        AppTopBarCustom(
            title = title,
            modifier = Modifier.constrainAs(topAppBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onBackClick = { navController.popBackStack() }
        )

        // Content
        Box(modifier = Modifier
            .fillMaxSize()
            .constrainAs(content) {
                top.linkTo(topAppBar.bottom)
                height = Dimension.fillToConstraints
            }) {

            Column(modifier = Modifier.fillMaxWidth()) {
                AndroidView(
                    factory = {
                        WebView(it).apply {
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(
                                    view: WebView?,
                                    url: String?,
                                    favicon: android.graphics.Bitmap?
                                ) {
                                    super.onPageStarted(view, url, favicon)
                                    isLoading.value = true
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading.value = false
                                }

                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: android.webkit.WebResourceRequest?
                                ): Boolean {
                                    view?.loadUrl(request?.url.toString())
                                    return true
                                }
                            }
                            settings.javaScriptEnabled = true
                            loadUrl(url)
                        }
                    },
                    update = {
                        it.loadUrl(url)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            }
        }

        // Progress bar
        if (isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .constrainAs(progressBar) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    }
                    .padding(16.dp)
            )
        }
    }
}
