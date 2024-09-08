package com.soho.sohoapp.live.ui.view.screens.pre_access

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.brushLiveGradientBg
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.components.onBoardGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppWhite

@Composable
fun PreAccessScreen(modifier: Modifier = Modifier, navController: NavHostController) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        val (topLogo, onBoard, button) = createRefs()

        // Top static logo
        Image(
            painter = painterResource(id = R.drawable.soho_livecast_logo),
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 40.dp, bottom = 60.dp)
                .constrainAs(topLogo) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        // Middle scrollable content
        OnboardingView(modifier = Modifier.constrainAs(onBoard) {
            top.linkTo(topLogo.bottom)
            bottom.linkTo(button.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            height = Dimension.fillToConstraints
        })

        // Bottom static buttons
        BottomButtons(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            navController = navController
        )
    }
}


@Composable
fun OnboardingView(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Image
        Box {
            Image(
                painter = painterResource(id = R.drawable.on_board_1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 45.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            //bottom overlay gradient view
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(horizontal = 45.dp)
                    .background(
                        brush = onBoardGradientBg
                    )
            )
        }

        // Description
        Text(
            text = "Livecast your property inspections for prospects to watch at their own convenience",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Page indicators
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (index == 2) Color.White else Color.Gray) // Highlight current page
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}


/*private fun oldView(){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brushMainGradientBg),
        contentAlignment = Alignment.Center,
    ) {
        CenterImgText(modifier)
        BottomButtons(modifier.align(alignment = Alignment.BottomCenter), navController)
    }
}*/

@Composable
fun BottomButtons(modifier: Modifier, navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ButtonColoured(text = stringResource(R.string.log_in),
            color = AppGreen,
            onBtnClick = { navController.navigate(NavigationPath.SIGNIN.name) })

        /*ButtonOutlineWhite(
            text = stringResource(R.string.sign_up),
            modifier = Modifier.fillMaxWidth(),
            onBtnClick = { navController.navigate(NavigationPath.SIGNUP.name) })*/
    }
}

@Composable
fun CenterImgText(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CenterImage()

        Text(
            text = stringResource(R.string.pre_access_msg),
            fontFamily = FontFamily(Font(R.font.axiforma)),
            fontWeight = FontWeight(950),
            color = AppWhite,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            lineHeight = 33.6.sp,
            letterSpacing = 0.28.sp
        )
    }
}

@Composable
fun CenterImage() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.pre_access_img),
            contentDescription = null,
            modifier = Modifier.wrapContentSize()
        )
        Image(
            painter = painterResource(id = R.drawable.girl_stand),
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .align(alignment = Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPreAccessScreen() {
    PreAccessScreen(navController = NavHostController(LocalContext.current))
}