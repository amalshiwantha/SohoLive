package com.soho.sohoapp.live.ui.view.screens.golive

import android.util.Size
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.FieldConfig
import com.soho.sohoapp.live.enums.SocialMediaInfo
import com.soho.sohoapp.live.enums.StepInfo
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.ButtonConnect
import com.soho.sohoapp.live.ui.components.DropDownWhatForLiveStream
import com.soho.sohoapp.live.ui.components.SearchBar
import com.soho.sohoapp.live.ui.components.SpacerHorizontal
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_12sp
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_12sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text700_14spBold
import com.soho.sohoapp.live.ui.components.Text950_20sp
import com.soho.sohoapp.live.ui.components.TextAreaWhite
import com.soho.sohoapp.live.ui.components.TextFieldWhite
import com.soho.sohoapp.live.ui.components.TextStarRating
import com.soho.sohoapp.live.ui.components.TextSwipeSelection
import com.soho.sohoapp.live.ui.components.brushBottomGradientBg
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.AppWhite
import com.soho.sohoapp.live.ui.theme.AppWhiteGray
import com.soho.sohoapp.live.ui.theme.BorderGray
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import com.soho.sohoapp.live.ui.theme.TextDark
import com.soho.sohoapp.live.utility.NetworkUtils
import org.koin.compose.koinInject

@Composable
fun GoLiveScreen(
    navController: NavController,
    goLiveVm: GoLiveViewModel = koinInject(),
    netUtil: NetworkUtils = koinInject()
) {
    val stepCount = 4
    var currentStepId by remember { mutableIntStateOf(3) }

    //step #4
    val optionList = mutableListOf("Option1", "Option 2", "Option 3")
    var selectedOption by remember { mutableStateOf("") }

    Box(modifier = Modifier.background(brushMainGradientBg)) {

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            item {
                TopContent(stepCount, currentStepId)
            }
            item {
                StepContents(
                    currentStepId = currentStepId,
                    optionList = optionList,
                    selectedOption = selectedOption,
                    onSelectOption = {
                        selectedOption = it
                    })
            }
        }

        NextBackButtons(modifier = Modifier.align(Alignment.BottomCenter),
            currentStepId = currentStepId,
            onClickedNext = {
                if (currentStepId < stepCount - 1) {
                    currentStepId++
                }
            },
            onClickedBack = {
                currentStepId = (currentStepId - 1) % stepCount
            })
    }
}

@Composable
private fun ScrollableContentStep3a() {
    val defaultPadding = 0.dp
    val listState = rememberLazyListState()
    var bottomPadding by remember { mutableStateOf(defaultPadding) }
    val socialMediaItems = SocialMediaInfo.entries

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }.collect { visibleItems ->
            bottomPadding =
                if (visibleItems.isNotEmpty() && visibleItems.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1) {
                    80.dp
                } else {
                    defaultPadding
                }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        items(socialMediaItems) { item ->
            SocialMediaItemContent(item)
        }
    }
}

@Composable
fun TopContent(stepCount: Int, currentStepId: Int) {
    StepIndicator(totalSteps = stepCount, currentStep = currentStepId)
    SpacerVertical(16.dp)
    StepCountTitleInfo(currentStepId)
}

@Composable
fun StepContents(
    currentStepId: Int,
    optionList: MutableList<String>,
    selectedOption: String,
    onSelectOption: (String) -> Unit
) {
    SpacerVertical(40.dp)

    when (currentStepId) {
        0 -> {
            SearchBar()
            SpacerVertical(16.dp)
            ScrollableContentStep1()
        }

        1 -> {
            ProfileHideItem()
            ScrollableContentStep2()
        }

        2 -> {
            ScrollableContentStep3()
        }

        3 -> {
            Content4(
                optionList = optionList,
                selectedOption = selectedOption,
                onSelectOption = { onSelectOption(it) })
        }
    }
}

@Composable
private fun Content4(
    optionList: MutableList<String>,
    selectedOption: String,
    onSelectOption: (String) -> Unit
) {
    Text700_14sp(step = "When do you want to go live?")

    SpacerVertical(size = 8.dp)
    SwipeableSwitch()

    SpacerVertical(size = 24.dp)
    Text700_14sp(step = "What is this livestream for?")
    DropDownWhatForLiveStream(
        selectedValue = selectedOption,
        options = optionList,
        placeHolder = "Select an option",
        onValueChangedEvent = {
            onSelectOption(it)
        })

    SpacerVertical(size = 24.dp)
    Text700_14sp(step = "Stream title")
    TextFieldWhite(FieldConfig.NEXT.apply {
        placeholder = "Address or title for your livestream"
    }) {}

    SpacerVertical(size = 24.dp)
    Text700_14sp(step = "Description")
    TextAreaWhite(FieldConfig.NEXT.apply {
        placeholder =
            "Let viewers know more about what you are streaming. E.g. Property description, address, etc."
    }) {}

    SpacerVertical(size = 40.dp)
    Text700_14sp(step = "Livestream cover image")
    Text400_14sp(info = "We’ve generated a cover image for your livestream. Cover image may be seen by viewers on connected social platforms and when you share your livestream link.")

    SpacerVertical(size = 128.dp)
}

@Composable
private fun SwipeableSwitch() {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = 16.dp
    val availableWidth = screenWidth - (horizontalPadding * 2)
    val indicatorWidth = availableWidth / 2
    var isNowSelected by remember { mutableStateOf(false) }
    val indicatorOffset by animateDpAsState(
        targetValue = if (isNowSelected) 0.dp else indicatorWidth,
        label = "animateToMove"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(width = 1.dp, color = BorderGray, shape = RoundedCornerShape(16.dp))
            .background(Color.Transparent)
    ) {
        //move selection
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(indicatorWidth)
                .padding(4.dp)
                .fillMaxHeight()
                .background(AppWhiteGray, shape = RoundedCornerShape(14.dp))
        )

        //two options
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextSwipeSelection(
                modifier = Modifier
                    .weight(1f)
                    .clickable { isNowSelected = true },
                title = "Now",
                textColor = if (isNowSelected) TextDark else AppWhite
            )

            TextSwipeSelection(
                modifier = Modifier
                    .weight(1f)
                    .clickable { isNowSelected = false },
                title = "Schedule for later",
                textColor = if (!isNowSelected) TextDark else AppWhite
            )
        }
    }
}

@Composable
private fun StepIndicator(
    totalSteps: Int,
    currentStep: Int,
    activeColor: Color = Color.White,
    inactiveColor: Color = Color.DarkGray
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0 until totalSteps) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(
                        color = if (i <= currentStep) activeColor else inactiveColor,
                        shape = CircleShape
                    )
            )

            if (i != totalSteps - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
private fun NextBackButtons(
    modifier: Modifier,
    currentStepId: Int,
    onClickedNext: () -> Unit,
    onClickedBack: () -> Unit
) {
    Box(
        modifier = modifier.background(
            brushBottomGradientBg, shape = RoundedCornerShape(
                topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp
            )
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (currentStepId > 0) {
                ButtonColoured(
                    text = stringResource(R.string.back), color = Color.Transparent,
                    modifier = Modifier.weight(1f),
                    isBackButton = true
                ) {
                    onClickedBack.invoke()
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            ButtonColoured(
                text = stringResource(R.string.next), color = AppGreen,
                modifier = Modifier.weight(1f)
            ) {
                onClickedNext.invoke()
            }
        }
    }
}

@Composable
private fun ScrollableContentStep3() {
    SocialMediaInfo.entries.forEach { item ->
        SocialMediaItemContent(item)
    }
}

@Composable
private fun ScrollableContentStep2() {
    for (index in 1..3) {
        ProfileItemContent(index)
    }
}

@Composable
private fun ScrollableContentStep1() {
    for (i in 1..5) {
        ItemContent(i)
    }
}

@Composable
private fun ProfileHideItem() {
    var checked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = ItemCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text700_14spBold(step = "Do not show profile")
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { checked = !checked },
                contentAlignment = Alignment.Center
            ) {
                //CheckBox BG
                Image(
                    painter = if (checked) {
                        painterResource(id = R.drawable.check_box_chcked)
                    } else {
                        painterResource(id = R.drawable.cehck_box_uncheck)
                    }, contentDescription = null, contentScale = ContentScale.FillBounds
                )

                //Tick Icon
                if (checked) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_tick),
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}

@Composable
private fun SocialMediaItemContent(info: SocialMediaInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {

            //logo
            val imgSize = getImageWidth(info.icon)
            Image(
                painter = painterResource(id = info.icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = if (imgSize.width == 0) {
                    Modifier
                } else {
                    Modifier.size(imgSize.width.dp, imgSize.height.dp)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            if (info.isConnect) {
                //switch
                ConnectSwitch()
            } else {
                //button
                ButtonConnect(text = "Connect Now", color = AppGreen) {
                    println("ConnectNow ${info.title}")
                }
            }
        }
    }
}

@Composable
private fun getImageWidth(drawableResId: Int): Size {
    val context = LocalContext.current
    val drawable = ResourcesCompat.getDrawable(context.resources, drawableResId, null)
    val width = drawable?.intrinsicWidth ?: 0
    val height = drawable?.intrinsicHeight ?: 0

    if (width > 1000) {
        return Size(105, 24)
    } else {
        return Size(0, 0)
    }

}

@Composable
private fun ProfileItemContent(index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = ItemCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            //image
            Image(
                painter = painterResource(id = R.drawable.prop_image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 70.dp, height = 68.dp)
                    .clip(CircleShape)
            )
            //info
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .fillMaxWidth()
            ) {
                val isChecked = index % 2 == 0
                ProfileNameCheckBox(isChecked)
                SpacerVertical(size = 8.dp)
                Text400_14sp(info = "james@raywhite.com.au")
            }
        }
    }
}

@Composable
private fun ItemContent(index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = ItemCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp)) {
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
                val isChecked = index % 2 == 0
                TypeAndCheckBox(isChecked)
                SpacerVertical(size = 8.dp)
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
private fun AmenitiesView() {
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
private fun AmenitiesIcon(icon: Int) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = null,
        modifier = Modifier.padding(start = 4.dp, end = 8.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun TypeAndCheckBox(isChecked: Boolean) {

    var checked by remember { mutableStateOf(isChecked) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
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

        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable { checked = !checked },
            contentAlignment = Alignment.Center
        ) {
            //CheckBox BG
            Image(
                painter = if (checked) {
                    painterResource(id = R.drawable.check_box_chcked)
                } else {
                    painterResource(id = R.drawable.cehck_box_uncheck)
                }, contentDescription = null, contentScale = ContentScale.FillBounds
            )

            //Tick Icon
            if (checked) {
                Image(
                    painter = painterResource(id = R.drawable.ic_tick),
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }

    }
}

@Composable
private fun ProfileNameCheckBox(isChecked: Boolean) {

    var checked by remember { mutableStateOf(isChecked) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text700_14spBold(step = "James Hiyashi")

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Right
        ) {

            TextStarRating(rate = "* 5.0")

            SpacerHorizontal(size = 16.dp)

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { checked = !checked },
                contentAlignment = Alignment.Center
            ) {
                //CheckBox BG
                Image(
                    painter = if (checked) {
                        painterResource(id = R.drawable.check_box_chcked)
                    } else {
                        painterResource(id = R.drawable.cehck_box_uncheck)
                    }, contentDescription = null, contentScale = ContentScale.FillBounds
                )

                //Tick Icon
                if (checked) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_tick),
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}

private fun getStepInfo(currentStepId: Int): StepInfo {
    return StepInfo.entries[(currentStepId) % StepInfo.entries.size]
}

@Composable
private fun StepCountTitleInfo(currentStepId: Int) {
    val stepInfo = getStepInfo(currentStepId)

    Text700_14sp(step = stepInfo.counter)
    SpacerVertical(8.dp)
    Text950_20sp(title = stepInfo.title)
    SpacerVertical(8.dp)
    Text400_14sp(info = stepInfo.info)
}


@Composable
fun ConnectSwitch() {
    var checked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable { checked = !checked },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = if (checked) {
                painterResource(id = R.drawable.switch_on)
            } else {
                painterResource(id = R.drawable.switch_off)
            }, contentDescription = null, contentScale = ContentScale.FillBounds
        )
    }
}

@Preview
@Composable
private fun PreviewGoLiveScreen() {
    Box(modifier = Modifier.background(brushMainGradientBg)) {
        val countSteps = 4
        val currentStep = 3

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            item {
                TopContent(countSteps, currentStep)
            }
            item {
                StepContents(currentStep, mutableListOf(), "") {}
            }
        }

        NextBackButtons(modifier = Modifier.align(Alignment.BottomCenter),
            currentStepId = currentStep,
            onClickedNext = {},
            onClickedBack = {})
    }
}
