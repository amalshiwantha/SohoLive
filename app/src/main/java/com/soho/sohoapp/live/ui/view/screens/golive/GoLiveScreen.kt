package com.soho.sohoapp.live.ui.view.screens.golive

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.SearchBar
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_12sp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_12sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.brushBottomGradientBg
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

@Composable
fun GoLiveScreen(
    navController: NavController,
    goLiveVm: GoLiveViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject()
) {
    Box(modifier = Modifier.background(brushMainGradientBg)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            StepCountTitleInfo()
            SpacerVertical(40.dp)
            SearchBar()
            SpacerVertical(16.dp)
            ScrollableContent()
        }

        FixedNextButton(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun FixedNextButton(modifier: Modifier) {
    Box(
        modifier = modifier
            .background(
                brushBottomGradientBg, shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
    ) {
        ButtonColoured(
            text = "Next", color = AppGreen,
            modifier = Modifier.padding(16.dp)
        ) {

        }
    }
}


@Composable
fun ScrollableContent() {
    val defaultPadding = 50.dp
    val listState = rememberLazyListState()
    var bottomPadding by remember { mutableStateOf(defaultPadding) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                bottomPadding =
                    if (visibleItems.isNotEmpty() &&
                        visibleItems.lastOrNull()?.index ==
                        listState.layoutInfo.totalItemsCount - 1
                    ) {
                        80.dp
                    } else {
                        defaultPadding
                    }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(bottom = bottomPadding)
    ) {
        items(10) { index ->
            ItemContent(index)
        }
    }
}

@Composable
fun ItemContent(index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = ItemCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp)) {
            //image
            Image(
                painter = painterResource(id = R.drawable.prop_image),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(width = 70.dp, height = 68.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            //info
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .fillMaxWidth()
            ) {
                TypeAndCheckBox()
                Text700_14sp(step = "308/50 Murray Street, Sydney NSW 2000")
                SpacerVertical(size = 8.dp)
                Text400_14sp(info = "3 scheduled livestream")
                SpacerVertical(size = 8.dp)
                AmenitiesView()
            }
        }
    }
}

@Composable
fun AmenitiesView() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text400_12sp(label = "3")
        AmenitiesIcon(icon = R.drawable.ic_bedroom)
        Text400_12sp(label = "2")
        AmenitiesIcon(icon = R.drawable.ic_bathroom)
        Text400_12sp(label = "1")
        AmenitiesIcon(icon = R.drawable.ic_car_park)
        Text400_12sp(label = "120 m²")
        AmenitiesIcon(icon = R.drawable.ic_floor_size)
    }
}

@Composable
fun AmenitiesIcon(icon: Int) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = null,
        modifier = Modifier.padding(start = 4.dp, end = 8.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun TypeAndCheckBox() {
    var checked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text700_12sp(label = "For Sale")
            Image(
                painter = painterResource(id = R.drawable.space_dot),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 8.dp),
                contentScale = ContentScale.FillBounds
            )
            Text400_12sp(label = "Apartment")
        }

        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it }
        )

    }
}

@Composable
fun StepCountTitleInfo() {
    Text700_14sp(step = "Step 1 of 4")
    SpacerVertical(8.dp)
    Text950_20sp(title = "Link livestream to your property")
    SpacerVertical(8.dp)
    Text400_14sp(info = "Prospect interested in your listing will be notified of your scheduled livestream and will be livecasted on your property listing page.")
}

@Preview
@Composable
private fun PreviewGoLiveScreen() {
    Box(modifier = Modifier.background(brushMainGradientBg)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            StepCountTitleInfo()
            SpacerVertical(40.dp)
            SearchBar()
            SpacerVertical(16.dp)
            ScrollableContent()
        }

        FixedNextButton(modifier = Modifier.align(Alignment.BottomCenter))
    }
}
