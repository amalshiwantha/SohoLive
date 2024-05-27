package com.soho.sohoapp.live.view.screens.home

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.view.activity.HaishinActivity
import com.soho.sohoapp.live.viewmodel.HomeViewModel

@Composable
fun HomeScreen(navController: NavHostController, homeVm: HomeViewModel = viewModel()) {

    val context = LocalContext.current
    val uiState by homeVm.uiState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Welcome to the SohoLive ${uiState.loadingMessage}")
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
                Button(onClick = {
                    //homeVm.setMessage("Updated")
                    val intent = Intent(context, HaishinActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Text("Update Message")
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    HomeScreen(navController = NavHostController(LocalContext.current))
}