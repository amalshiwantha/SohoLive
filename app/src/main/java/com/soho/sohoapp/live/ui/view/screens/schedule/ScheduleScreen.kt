package com.soho.sohoapp.live.ui.view.screens.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.soho.sohoapp.live.model.ScheduleSlots
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
import com.soho.sohoapp.live.ui.theme.ErrorRed
import com.soho.sohoapp.live.ui.theme.HintGray
import com.soho.sohoapp.live.ui.theme.ItemCardBg
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ScheduleScreen(
    navController: NavController,
    scheduleSlots: MutableList<ScheduleSlots>
) {
    var isShowDialog by remember { mutableStateOf(false) }
    var isShowDateTimePicker by remember { mutableStateOf(false) }
    var slotToDelete by remember { mutableStateOf<ScheduleSlots?>(null) }
    var slotToSave by remember { mutableStateOf<ScheduleSlots?>(null) }

    if (isShowDialog) {
        ShowDeleteAlert(
            isShowDialog = isShowDialog,
            slotToDelete = slotToDelete,
            onDismiss = { isShowDialog = it },
            onDelete = { scheduleSlots.remove(it) }
        )
    }

    if (isShowDateTimePicker) {
        DateTimePicker(onDismissed = { isShowDateTimePicker = !it }, onDateTimeSelect = {
            slotToSave = it
        })
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
                            TopContent(scheduleSlots.isEmpty(), slotToSave, onClickAdd = {
                                slotToSave?.let {
                                    if (!it.date.isNullOrEmpty() && !it.time.isNullOrEmpty()) {
                                        slotToSave = null
                                        scheduleSlots.add(it)
                                    }
                                }
                            },
                                onClickShowDateTimePicker = {
                                    isShowDateTimePicker = it
                                })
                        }
                        items(scheduleSlots) { slot ->
                            ScheduleItemView(slot = slot, onItemClick = { deleteSlot ->
                                isShowDialog = true
                                slotToDelete = deleteSlot
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
fun ShowDeleteAlert(
    isShowDialog: Boolean,
    slotToDelete: ScheduleSlots?,
    onDismiss: (Boolean) -> Unit,
    onDelete: (ScheduleSlots) -> Unit
) {
    slotToDelete?.let {
        AlertDialog(
            onDismissRequest = { onDismiss(!isShowDialog) },
            title = { Text(text = "Confirm Deletion") },
            text = { Text(text = "Do you want to delete ${it.date.orEmpty()} schedule slot?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(it)
                        onDismiss(!isShowDialog)
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss(!isShowDialog) }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun ScheduleItemView(slot: ScheduleSlots, onItemClick: (ScheduleSlots) -> Unit) {
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
                txtColor = ErrorRed,
                onBtnClick = { onItemClick(slot) }
            )
        }
    }
}

@Composable
fun TopContent(
    isSlotsEmpty: Boolean,
    slotToSave: ScheduleSlots?,
    onClickShowDateTimePicker: (Boolean) -> Unit,
    onClickAdd: () -> Unit
) {
    val input = slotToSave?.display.orEmpty()

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
            inputTxt = input,
            onTextChange = {}, onClick = {
                onClickShowDateTimePicker(true)
            })
        SpacerVertical(size = 24.dp)

        ButtonOutLinedIcon(
            text = "Add to Schedule",
            icon = R.drawable.ic_add,
            onBtnClick = {
                onClickAdd()
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


@Composable
fun DateTimePicker(
    onDismissed: (Boolean) -> Unit,
    onDateTimeSelect: (ScheduleSlots) -> Unit
) {
    val slot = ScheduleSlots()
    val context = LocalContext.current

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var displayDateTime by remember { mutableStateOf("") }
    var isShowDatePicker by remember { mutableStateOf(true) }
    var isShowTimePicker by remember { mutableStateOf(false) }

    // Get current date
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    // Set minimum date (today)
    val minDate = calendar.timeInMillis

    // Set maximum date (30 days from today)
    calendar.add(Calendar.DAY_OF_YEAR, 30)
    val maxDate = calendar.timeInMillis

    // DateRime format
    val dateFormat = SimpleDateFormat("d MMMM yyyy, EEEE", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val displayFormat = SimpleDateFormat("d/M/yyyy,", Locale.getDefault())

    // Open DatePickerDialog
    if (isShowDatePicker) {
        val datePickerDialog =
            DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)

                displayDateTime = displayFormat.format(calendar.time)
                selectedDate = dateFormat.format(calendar.time)
            }, year, month, day)

        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.datePicker.maxDate = maxDate

        datePickerDialog.setOnDismissListener {
            isShowDatePicker = false
            isShowTimePicker = true
        }
        datePickerDialog.show()
    }

    // Open TimePickerDialog
    if (isShowTimePicker) {
        val timePickerDialog = TimePickerDialog(context, { _, selectedHour, selectedMinute ->
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)

            selectedTime = timeFormat.format(calendar.time)
            displayDateTime += " $selectedTime"
        }, hour, minute, false)

        timePickerDialog.setOnDismissListener {
            !isShowTimePicker
            onDateTimeSelect(slot.apply {
                date = selectedDate
                time = selectedTime
                display = displayDateTime
            })
            onDismissed(true)
        }
        timePickerDialog.show()
    }
}


@Preview
@Composable
private fun ScheduleScreenPreview() {

    val scheduleSlots = remember { mutableStateListOf<ScheduleSlots>() }
    LaunchedEffect(Unit) {
        scheduleSlots.addAll(
            listOf(
                ScheduleSlots("3 July 2024, Friday", "10:00am"),
                ScheduleSlots("4 July 2024, Friday", "12:00pm"),
                ScheduleSlots("5 July 2024, Friday", "01:00pm")
            )
        )
    }

    ScheduleScreen(
        navController = NavHostController(LocalContext.current),
        scheduleSlots = scheduleSlots
    )
}