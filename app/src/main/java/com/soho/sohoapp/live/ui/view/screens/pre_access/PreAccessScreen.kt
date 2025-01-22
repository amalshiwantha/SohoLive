package com.soho.sohoapp.live.ui.view.screens.pre_access

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.model.OnboardingData
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.ButtonOutlineWhite
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text800_14sp
import com.soho.sohoapp.live.ui.components.Text950_20spCenter
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.navigation.NavigationPath
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppWhite

val onboardingItems = listOf(
    OnboardingData(
        R.drawable.on_board_4,
        "Apply your agent and agency branding on every video"
    ),
    OnboardingData(
        R.drawable.on_board_2,
        "Record or livestream property inspections for prospects to view at their convenience"
    ),
    OnboardingData(
        R.drawable.on_board_3,
        "Publish your videos publicly for everyone to watch or as unlisted for private sharing"
    ),
    OnboardingData(
        R.drawable.on_board_network,
        "Simultaneously share your video to your Soho listing and connected social platforms"
    )
)

@Composable
fun PreAccessScreen(navController: NavHostController) {

    val pagerState = rememberPagerState()

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
                .padding(vertical = 60.dp)
                .constrainAs(topLogo) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        // Middle scrollable content
        OnboardingView(
            modifier = Modifier.constrainAs(onBoard) {
                top.linkTo(topLogo.bottom)
                bottom.linkTo(button.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            },
            pagerState = pagerState
        )

        // Bottom button and pager indicator
        BottomBtnIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            navController = navController,
            pagerState = pagerState
        )
    }
}

@Composable
fun OnboardingView(modifier: Modifier, pagerState: PagerState) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        // Pager with images and descriptions
        HorizontalPager(
            count = onboardingItems.size,
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column {

                    if (page == 1) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.Asset("rec_live.lottie")
                        )

                        //center animated image
                        Box(modifier = Modifier.fillMaxWidth()) {
                            LottieAnimation(
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                                modifier = Modifier.aspectRatio(1f)
                            )
                        }
                    } else {
                        //center image
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                painter = painterResource(id = onboardingItems[page].imageRes),
                                contentDescription = null,
                                modifier = Modifier.aspectRatio(1f)
                            )

                            // Gradient overlay at the bottom of the image
                            /*Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .height(120.dp)
                                    .background(onBoardGradientBg)
                            )*/
                        }
                    }


                    SpacerUp(size = 32.dp)

                    // Description text
                    Text950_20spCenter(
                        title = onboardingItems[page].info,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        SpacerUp(size = 16.dp)


    }
}

@Composable
fun BottomBtnIndicator(
    modifier: Modifier,
    navController: NavHostController,
    pagerState: PagerState
) {
    var isShowSignup = remember { true }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Page indicators
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            onboardingItems.forEachIndexed { index, _ ->
                isShowSignup = pagerState.currentPage == 0

                val isSelected = pagerState.currentPage == index
                val itemWidth = if (isSelected) 24.dp else 16.dp
                val itemColor = if (isSelected) Color.White else Color.Gray

                Box(
                    modifier = Modifier
                        .width(itemWidth)
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(itemColor)
                )

                if (index != onboardingItems.lastIndex) {
                    SpacerSide(size = 4.dp)
                }
            }
        }

        // Login and Sign Up buttons
        ButtonColoured(text = stringResource(R.string.log_in),
            color = AppGreen,
            onBtnClick = {
                navController.navigate(NavigationPath.SIGNIN.name)
            })

        //No account login buttons
        if (isShowSignup) {
            Text800_14sp(label = "No Account Yet? ")
            ButtonOutlineWhite(
                text = "Visit soho.com.au/agents/livecast",
                modifier = Modifier.fillMaxWidth(),
                onBtnClick = { navController.navigate(NavigationPath.SIGNUP.name) })
        }
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