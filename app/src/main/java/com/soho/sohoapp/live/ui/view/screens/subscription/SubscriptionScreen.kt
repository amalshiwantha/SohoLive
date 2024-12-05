package com.soho.sohoapp.live.ui.view.screens.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.soho.sohoapp.live.ui.components.SpacerSide
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text400_12sp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text800_10sp
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.TopAppBarCustomClose
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import com.soho.sohoapp.live.ui.theme.SelectedOrange
import com.soho.sohoapp.live.ui.view.screens.golive.NoInternetScreen
import com.soho.sohoapp.live.utility.NetworkUtils
import com.soho.sohoapp.live.utility.formatNumber
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

    //Update ActivePlan status
    LaunchedEffect(mState.isCalledActivePlan) {
        if (mState.isCalledActivePlan) {
            mState.activePlanRes?.let {
                mGState.apply { activePlan.value = it }
            }
        }
    }

    //Main Content
    if (netUtil.isNetworkAvailable()) {
        MainContent(mState, mGState, onBackClick = {
            navController.popBackStack()
        })
    } else {
        NoNetView(onRetryClick = { vmSubs.loadPlans() })
    }
}

@Composable
private fun NoNetView(onRetryClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brushMainGradientBg)
    ) {
        NoInternetScreen(onRetryClick = { onRetryClick() })
    }
}

@Composable
private fun MainContent(
    mState: SubscriptionState,
    mGState: GlobalState,
    onBackClick: () -> Unit
) {
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
            .padding(bottom = 24.dp)
            .constrainAs(content) {
                top.linkTo(actionBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
            PlanContent(mState, mGState)
        }
    }
}

@Composable
private fun PlanContent(mState: SubscriptionState, mGState: GlobalState) {
    if (mState.isLoading) {
        CenterMessageProgress(message = mState.loadingMessage)
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(mState.planListRes) { plan ->
                SubsPlanCard(plan, mGState)
            }
        }
    }
}

@Composable
private fun SubsPlanCard(plan: SubscriptionCategory, mGState: GlobalState) {
    val isSingleCast = isSingleCast(plan.title)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = ItemCardBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            //Cast Icon
            Image(
                painter = painterResource(id = getCastIcon(isSingleCast)),
                contentDescription = "cast_icon"
            )
            SpacerUp(size = 16.dp)

            //Title
            Text950_20sp(title = plan.title)
            SpacerUp(size = 16.dp)

            //Subtitle with SM icon
            SubtitleCastTo(isSingleCast, plan.subTitle)
            SpacerUp(size = 16.dp)

            //Features
            FeaturesList(plan.features)
            SpacerUp(size = 24.dp)

            //Cast Plans List
            plan.plans.forEach {
                if (it.id == mGState.activePlan.value?.id) {
                    CastTermCardSelected(it)
                } else {
                    CastTermCard(it)
                }
            }

            //Info View
            Text400_12sp(
                modifier = Modifier,
                label = "Unfortunately, you can't modify your plan or view pricing in the app. We know it's not ideal.",
                txtColor = HintGray, isCenter = true
            )

            SpacerUp(size = 16.dp)
        }
    }
}

@Composable
private fun CastTermCard(subPlan: SubscriptionPlan) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .border(
                width = 1.dp,
                color = AppWhite,
                shape = MaterialTheme.shapes.small
            ),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = ItemCardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text700_14sp(step = subPlan.name)

            CastTerms(subPlan.terms)

            Text400_12sp(
                label = "Please visit our website for more details.",
                txtColor = HintGray
            )
        }
    }
}

@Composable
private fun CastTermCardSelected(subPlan: SubscriptionPlan) {
    val innerCardRadius = 8.dp
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        shape = RoundedCornerShape(innerCardRadius),
        colors = CardDefaults.cardColors(containerColor = SelectedOrange)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SpacerUp(size = 8.dp)
            Text800_10sp(label = "CURRENT", txtColor = AppWhite)
            SpacerUp(size = 4.dp)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = innerCardRadius,
                    bottomEnd = innerCardRadius
                ),
                colors = CardDefaults.cardColors(containerColor = ItemCardBg)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text700_14sp(step = subPlan.name)
                    CastTerms(subPlan.terms)
                }
            }

        }
    }
}

@Composable
private fun CastTerms(terms: PlanTerms) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TermsInfo(terms.streamingMinutes, "streaming mins")
        TermsInfo(terms.viewingMinutes, "viewing mins")
    }
}

@Composable
private fun TermsInfo(minValue: String, title: String) {
    Column(horizontalAlignment = CenterHorizontally) {
        Text950_20sp(title = minValue.toInt().formatNumber())
        Text700_14sp(step = title, isBold = false)
    }
}

@Composable
private fun FeaturesList(features: List<String>) {
    Text700_14sp(step = "Features")
    SpacerUp(size = 16.dp)
    Column(modifier = Modifier.padding(start = 8.dp)) {
        features.forEach {
            BulletPointText(it)
        }
    }
}

@Composable
private fun BulletPointText(value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        // Bullet Point
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
        SpacerSide(size = 8.dp)
        // Text
        Text400_14sp(info = value)
    }
}

@Composable
private fun SubtitleCastTo(isSingle: Boolean, subTitle: String) {
    Column {
        //SM List icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, CenterHorizontally)
        ) {
            Text400_14sp(info = "Cast to")

            Image(
                painter = painterResource(id = R.drawable.soho_logo),
                modifier = Modifier.size(16.dp),
                contentDescription = "soho_logo"
            )

            if (!isSingle) {
                Image(
                    painter = painterResource(id = R.drawable.cast_sm_fb),
                    contentDescription = "fb_logo"
                )
                Image(
                    painter = painterResource(id = R.drawable.cast_sm_yt),
                    contentDescription = "fb_logo"
                )
                Image(
                    painter = painterResource(id = R.drawable.cast_sm_li),
                    contentDescription = "fb_logo"
                )
            }
        }

        SpacerUp(size = 16.dp)

        Text400_14sp(info = subTitle, color = HintGray)
    }
}

private fun getCastIcon(isSingle: Boolean): Int {
    return if (isSingle) {
        R.drawable.singlecast
    } else {
        R.drawable.multicast
    }
}

private fun isSingleCast(title: String): Boolean {
    return title.lowercase().contains("single")
}

//Preview

@Preview
@Composable
private fun ScreenSubsPlanCard() {
    SubsPlanCard(subscriptionCategory, GlobalState())
}

@Preview
@Composable
private fun ScreenMain() {
    MainContent(mState = SubscriptionState(), mGState = GlobalState(), onBackClick = {})
}

@Preview
@Composable
private fun ScreenNoNet() {
    NoNetView {}
}


//sample data
val subscriptionCategory = SubscriptionCategory(
    title = "Single Cast",
    subTitle = "Live stream to your property inspections to your listing",
    features = listOf(
        "Cast to multiple destinations",
        "90 Days In-App Storage",
        "Livecast available on listing for 90 days",
        "Agency and Agent Branding"
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