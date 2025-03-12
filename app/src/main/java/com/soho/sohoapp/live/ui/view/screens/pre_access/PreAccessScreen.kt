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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

const val SIGNUP_TITLE = "Soho LiveCast"

val onboardingItems = listOf(
    OnboardingData(
        R.drawable.on_board_4,
        "Apply your agent and agency branding on every video\n"
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
                .padding(top = 60.dp, bottom = 45.dp)
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
    val imgSize = (screenWidth * 0.87f)

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

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (page == 1) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.Asset("rec_live.lottie")
                        )

                        //center animated image
                        Box(modifier = Modifier.size(imgSize)) {
                            LottieAnimation(
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                                modifier = Modifier.aspectRatio(1f)
                            )
                        }
                    } else {
                        //center image
                        Box(modifier = Modifier.size(imgSize)) {
                            Image(
                                painter = painterResource(id = onboardingItems[page].imageRes),
                                contentDescription = null,
                                modifier = Modifier.aspectRatio(1f)
                            )
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 30.dp),
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

        Text800_14sp(label = "No Account Yet? ")
        ButtonOutlineWhite(
            text = "Visit Soho Livecast",
            modifier = Modifier.fillMaxWidth().height(48.dp),
            onBtnClick = {
                val webUrl = "https://soho.com.au/agents/livecast"
                val encodeUrl = URLEncoder.encode(webUrl, StandardCharsets.UTF_8.toString())
                navController.navigate("${NavigationPath.WEB_VIEW_MAIN.name}/$SIGNUP_TITLE/$encodeUrl")
            })
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPreAccessScreen() {
    PreAccessScreen(navController = NavHostController(LocalContext.current))
}