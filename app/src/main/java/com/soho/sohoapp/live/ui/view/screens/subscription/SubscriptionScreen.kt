package com.soho.sohoapp.live.ui.view.screens.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.model.PlanTerms
import com.soho.sohoapp.live.model.SubscriptionCategory
import com.soho.sohoapp.live.model.SubscriptionPlan
import com.soho.sohoapp.live.ui.components.CenterMessageProgress
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text700_10sp
import com.soho.sohoapp.live.ui.components.TopAppBarCustomClose
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import com.soho.sohoapp.live.ui.view.screens.golive.NoInternetScreen
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

@Composable
fun SubscriptionScreen(
    mGState: GlobalState,
    navController: NavController,
    vmSubs: SubscriptionViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject(),
) {
    val mState = vmSubs.mState.value

    //load main subs plans
    LaunchedEffect(key1 = "LoadPlans") {
        if (netUtil.isNetworkAvailable() && mState.planListRes.isEmpty()) {
            vmSubs.loadPlans()
        }
    }

    //Main Content
    if (netUtil.isNetworkAvailable()) {
        MainContent(mState, onBackClick = {
            navController.popBackStack()
        })
    } else {
        NoNetView(onRetryClick = { vmSubs.loadPlans() })
    }
}

@Composable
fun NoNetView(onRetryClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        NoInternetScreen(onRetryClick = { onRetryClick() })
    }
}

@Composable
fun MainContent(mState: SubscriptionState, onBackClick: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(brushMainGradientBg)
    ) {
        val (actionBar, content) = createRefs()

        //TopActionBar
        Column(modifier = Modifier.constrainAs(actionBar) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            SpacerUp(size = 8.dp)

            TopAppBarCustomClose(
                title = "Available Plans",
                rightIcon = R.drawable.ic_close_circle,
                modifier = Modifier,
                onCloseClick = { onBackClick() }
            )

            SpacerUp(size = 64.dp)
        }

        //Content
        Column(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(content) {
                top.linkTo(actionBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
            PlanContent(mState)
        }
    }
}

@Composable
fun PlanContent(mState: SubscriptionState) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (mState.isLoading) {
            //Loading View
            CenterMessageProgress(message = mState.loadingMessage)
        } else {
            mState.planListRes.forEach {
                SubsPlanCard(it)
            }
        }
    }
}

@Composable
fun SubsPlanCard(planInfo: SubscriptionCategory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = ItemCardBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text700_10sp(title = planInfo.title)
        }
    }
}


//Preview

@Preview
@Composable
private fun ScreenSubsPlanCard() {
    SubsPlanCard(subscriptionCategory)
}

@Preview
@Composable
private fun ScreenMain() {
    MainContent(mState = SubscriptionState(), onBackClick = {})
}

@Preview
@Composable
private fun ScreenNoNet() {
    NoNetView {}
}


//sample data
val subscriptionCategory = SubscriptionCategory(
    title = "Title",
    subTitle = "Subtitle example",
    features = listOf(
        "Feature 1: Cast to multiple destinations",
        "Feature 2: 90 Days In-App Storage",
        "Feature 3: Livecast available on listing for 90 days",
        "Feature 4: Agency and Agent Branding"
    ),
    plans = listOf(
        SubscriptionPlan(
            id = 13,
            interval = "year",
            name = "Multicast 15",
            price = "192.0",
            terms = PlanTerms(
                viewingMinutes = "1500",
                streamingMinutes = "15",
                inAppStorageDays = "30",
                simulcastingEnabled = true,
                listingAvailableDays = "90",
                overageRateDollarsPerMinute = "0.6"
            ),
            planType = "soho_live",
            bestValue = false
        ),
        SubscriptionPlan(
            id = 14,
            interval = "year",
            name = "Multicast 60",
            price = "384.0",
            terms = PlanTerms(
                viewingMinutes = "6000",
                streamingMinutes = "60",
                inAppStorageDays = "30",
                simulcastingEnabled = true,
                listingAvailableDays = "90",
                overageRateDollarsPerMinute = "0.5"
            ),
            planType = "soho_live",
            bestValue = true
        ),
        SubscriptionPlan(
            id = 15,
            interval = "year",
            name = "Multicast 120",
            price = "672.0",
            terms = PlanTerms(
                viewingMinutes = "12000",
                streamingMinutes = "120",
                inAppStorageDays = "30",
                simulcastingEnabled = true,
                listingAvailableDays = "90",
                overageRateDollarsPerMinute = "0.4"
            ),
            planType = "soho_live",
            bestValue = false
        )
    )
)