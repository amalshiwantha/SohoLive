package com.soho.sohoapp.live.ui.view.screens.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.enums.FieldConfig
import com.soho.sohoapp.live.network.response.ScheduleSlots
import com.soho.sohoapp.live.ui.components.AppTopBar
import com.soho.sohoapp.live.ui.components.ButtonColoured
import com.soho.sohoapp.live.ui.components.ButtonConnect
import com.soho.sohoapp.live.ui.components.ButtonOutLinedIcon
import com.soho.sohoapp.live.ui.components.SpacerVertical
import com.soho.sohoapp.live.ui.components.Text400_14sp
import com.soho.sohoapp.live.ui.components.Text700_14sp
import com.soho.sohoapp.live.ui.components.Text800_14sp
import com.soho.sohoapp.live.ui.components.Text950_16sp
import com.soho.sohoapp.live.ui.components.TextFieldWhiteIcon
import com.soho.sohoapp.live.ui.components.brushMainGradientBg
import com.soho.sohoapp.live.ui.theme.AppGreen
import com.soho.sohoapp.live.ui.theme.DeleteRed
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.theme.ItemCardBg

@Composable
fun ScheduleScreen(
    navController: NavController,
    scheduleSlots: MutableList<ScheduleSlots>,
    onUpdateSlots: (MutableState<ScheduleSlots>) -> Unit
) {

    LaunchedEffect(Unit) {
        scheduleSlots.addAll(
            listOf(
                ScheduleSlots(1, "3 July 2024, Friday", "10:00am"),
                ScheduleSlots(2, "4 July 2024, Friday", "12:00pm"),
                ScheduleSlots(3, "5 July 2024, Friday", "01:00pm")
            )
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = stringResource(R.string.date_time_title),
                onBackClick = { navController.popBackStack() })
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brushMainGradientBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val (content, submitButton) = createRefs()

                    LazyColumn(
                        modifier = Modifier
                            .constrainAs(content) {
                                top.linkTo(parent.top)
                                bottom.linkTo(submitButton.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                height = Dimension.fillToConstraints
                            }
                            .padding(16.dp)
                    ) {
                        item {
                            TopContent(scheduleSlots.isEmpty())
                        }
                        items(scheduleSlots) { slot ->
                            ScheduleItemView(slot = slot, onItemClick = {
                                println("myItem Delete $it")
                            })
                        }
                    }

                    ButtonColoured(text = "Complete Scheduling",
                        color = AppGreen,
                        modifier = Modifier
                            .constrainAs(submitButton) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(16.dp)
                            .fillMaxWidth(),
                        onBtnClick = { /* Handle button click */ })
                }
            }
        }
    }
}

@Composable
fun ScheduleItemView(slot: ScheduleSlots, onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = ItemCardBg)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            //date and time
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text800_14sp(label = slot.date.orEmpty())
                SpacerVertical(size = 8.dp)
                Text400_14sp(info = slot.time.orEmpty())
            }

            //Delete button
            ButtonConnect(
                text = "Delete",
                color = Color.Transparent,
                txtColor = DeleteRed,
                onBtnClick = { onItemClick(slot.id) }
            )
        }
    }
}

@Composable
fun TopContent(isSlotsEmpty: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        Text700_14sp(step = "Choose Date & Time")
        SpacerVertical(size = 8.dp)

        TextFieldWhiteIcon(
            fieldConfig = FieldConfig.DONE.apply {
                placeholder = ""
                keyboardType = KeyboardType.Text
                trailingIcon = Icons.Filled.ArrowDropDown
                clickable = true
            },
            onTextChange = {}, onClick = {})
        SpacerVertical(size = 24.dp)

        ButtonOutLinedIcon(
            text = "Add to Schedule",
            icon = R.drawable.ic_add,
            onBtnClick = {
            })
        SpacerVertical(size = 40.dp)

        Text950_16sp(
            title = "Your Scheduled Livestreams",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (isSlotsEmpty) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text400_14sp(
                    info = "No scheduled livecast streams yet",
                    color = HintGray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun ScheduleScreenPreview() {
    ScheduleScreen(
        navController = NavHostController(LocalContext.current),
        scheduleSlots = mutableListOf(),
        onUpdateSlots = { })
}