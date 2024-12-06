package com.soho.sohoapp.live.ui.view.screens.usage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.model.GlobalState
import com.soho.sohoapp.live.network.response.CurrentUsage
import com.soho.sohoapp.live.network.response.PlanDetails
import com.soho.sohoapp.live.ui.components.CenterMessageProgress
import com.soho.sohoapp.live.ui.components.SpacerUp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text950_16sp
import com.soho.sohoapp.live.ui.components.TopAppBarCustomClose
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppRed
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.AppWhiteGray
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import com.soho.sohoapp.live.ui.theme.OverageDark
import com.soho.sohoapp.live.ui.theme.OverageRed
import com.soho.sohoapp.live.ui.theme.TextDark
import com.soho.sohoapp.live.ui.view.screens.subscription.NoNetView
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UsageScreen(
    mGState: GlobalState,
    navController: NavController,
    usageVm: UsageViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject(),
) {
    val mState = usageVm.mState.value

    LaunchedEffect("load_usage") {
        if (netUtil.isNetworkAvailable() && mState.usageRes == null) {
            usageVm.loadUsage()
        }
    }

    //Main Content
    if (netUtil.isNetworkAvailable()) {
        MainContent(mState, mGState, onBackClick = {
            navController.popBackStack()
        })
    } else {
        NoNetView(onRetryClick = { usageVm.loadUsage() })
    }
}

@Composable
private fun MainContent(
    mState: UsageState,
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
                title = "Your Usage",
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
            UsageContent(mState, mGState)
        }
    }
}

@Composable
private fun UsageContent(mState: UsageState, mGState: GlobalState) {
    if (mState.isLoading) {
        CenterMessageProgress(message = mState.loadingMessage)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text400_14sp(info = "Your usage information will only be shown after a 12 hours delay.")
            SpacerUp(size = 16.dp)
            mState.usageRes?.let {
                UsageCard(it)
            }
        }
    }
}

@Composable
private fun UsageCard(usage: CurrentUsage) {
    val planData = usage.planDetails
    val maxStream = planData?.streamingMinutes ?: "0"
    val usedStream = usage.streamingMinutes

    val maxView = planData?.viewingMinutes ?: "0"
    val usedView = usage.viewingMinutes

    val totalOverage = usage.overageStreamingMinutes + usage.overageViewingMinutes
    val streamUsage = "$usedStream/$maxStream"
    val viewUsage = "$usedView/$maxView"
    val period = "${getFormatDate(usage.startTime)} - ${getFormatDate(usage.endTime)}"
    val progStream = getProgress(usedStream, maxStream.toInt())
    val progView = getProgress(usedView, maxView.toInt())
    val isStreamOver = usage.overageStreamingMinutes > 0
    val isViewOver = usage.overageViewingMinutes > 0

    val isOverage = totalOverage != 0
    val cardBg = if (isOverage) ItemCardBg else AppWhite
    val txtColor = if (isOverage) AppWhite else TextDark
    val overageBg = if (isOverage) OverageDark else AppWhiteGray

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            //top content
            Column(modifier = Modifier.padding(16.dp)) {
                //Date Range
                Text950_16sp(title = period, txtColor = txtColor)
                SpacerUp(size = 24.dp)

                //Usage for each
                UsageProgress(
                    "Streamed",
                    "$streamUsage mins",
                    progress = progStream,
                    txtColor = txtColor,
                    txtValueColor = if (isStreamOver) OverageRed else txtColor,
                    isOverage,
                    isStreamOver
                )
                SpacerUp(size = 16.dp)
                UsageProgress(
                    "Viewed",
                    "$viewUsage mins",
                    progress = progView,
                    txtColor = txtColor,
                    txtValueColor = if (isStreamOver) OverageRed else txtColor,
                    isOverage,
                    isViewOver
                )
                SpacerUp(size = 24.dp)
            }

            //bottom content overage
            TotalOverage("$totalOverage min", txtColor = txtColor, overageBg = overageBg)
        }

    }
}

fun getFormatDate(dateTime: String?): String {
    return dateTime?.let {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormatter.timeZone = java.util.TimeZone.getTimeZone("UTC")
        val date: Date = inputFormatter.parse(it) ?: return ""
        val outputFormatter = SimpleDateFormat("d MMMM", Locale.getDefault())
        outputFormatter.format(date)
    } ?: run {
        ""
    }
}

@Composable
fun TotalOverage(overage: String, txtColor: Color, overageBg: Color) {
    Column(
        modifier = Modifier
            .background(overageBg)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text400_14sp(info = "Total Overages", modifier = Modifier.weight(1f), color = txtColor)
            Text700_14sp(step = overage, color = txtColor)
        }
    }
}

@Composable
fun UsageProgress(
    label: String,
    usage: String,
    progress: Int,
    txtColor: Color,
    txtValueColor: Color,
    isOverage: Boolean,
    isItemOverage: Boolean,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        //Info
        Row(modifier = Modifier.fillMaxWidth()) {
            Text400_14sp(info = label, modifier = Modifier.weight(1f), color = txtColor)
            Text700_14sp(step = usage, color = txtValueColor)
        }
        SpacerUp(size = 16.dp)
        UsageProgressBar(progress = progress, isOverage = isOverage, isItemOverage = isItemOverage)
    }
}

@Composable
fun UsageProgressBar(
    progress: Int,
    isOverage: Boolean,
    isItemOverage: Boolean
) {
    val progPrimary = if (isItemOverage) {
        OverageRed
    } else {
        if (isOverage) Color(0xFFD8D1E4) else Color(0xFF4E215C)
    }
    val progSecond = if (isItemOverage) {
        OverageRed
    } else {
        if (isOverage) Color(0xFF4E215C) else Color(0xFFD8D1E4)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(progSecond) // Secondery
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress / 100.toFloat())
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(progPrimary) // Primary
        )
    }
}

fun getProgress(current: Int, max: Int): Int {
    if (max == 0) return 0
    return (current.toFloat() / max.toFloat() * 100).toInt()
}

@Preview
@Composable
private fun PreviewUsageCard() {
    UsageCard(getSampleUsage())
}

private fun getSampleUsage(): CurrentUsage {
    return CurrentUsage(
        startTime = "2024-12-04T00:00:00.000Z",
        endTime = "2025-01-03T00:00:00.000Z",
        planDetails = PlanDetails(
            name = "Multicast 15",
            interval = "month",
            price = "20.0",
            stripePlanId = "price_1PzsGfKYLTBX2qczWS2hEke2",
            stripeViewingPlanId = "price_1PuU0VKYLTBX2qczpQx1LFza",
            stripeStreamingPlanId = "price_1PuU3rKYLTBX2qcziNIKN5mM",
            planType = "soho_live",
            viewingMinutes = "1500",
            streamingMinutes = "20",
            inAppStorageDays = "30",
            simulcastingEnabled = true,
            listingAvailableDays = "90",
            overageRateDollarsPerMinute = "0.6"
        ),
        streamingMinutes = 10,
        viewingMinutes = 120,
        overageStreamingMinutes = 10,
        overageViewingMinutes = 10
    )
}