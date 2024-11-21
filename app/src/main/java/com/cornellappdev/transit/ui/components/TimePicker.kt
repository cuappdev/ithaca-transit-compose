package com.cornellappdev.transit.ui.components

import android.app.TimePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.Style
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.Instant
import java.util.*

/**
 * Uneditable text field that allows users to select a time using timePickerDialog.
 *
 * @param timeState State that represents the current time selected by user
 * @param timeFormatter Formatter used for displaying the text of the time
 * @param modifier Modifier to be applied to the [DatePicker]
 * @param disabled Whether the field is disabled
 * @param onTimeChanged Callback called when the user changes the time selected.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePicker(
    time: String,
    timeFormatter: SimpleDateFormat,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    onTimeChanged: (String) -> Unit,
) {
    val timePickerDialog =
        createTimePickerDialog(LocalContext.current, onTimeChanged, timeFormatter)

    Row(
        modifier = modifier
            .width(90.dp)
            .height(40.dp)
            .clip(
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                DividerGray
            )
            .clickable {
                if (!disabled) {
                    timePickerDialog.show()
                }
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            text = if (disabled) timeFormatter.format(Date.from(Instant.now())) else time,
            style = Style.heading3,
            color = if (disabled) Color.DarkGray else Color.Black
        )

    }
}

fun createTimePickerDialog(
    context: Context,
    setTimeText: (String) -> Unit,
    timeFormatter: SimpleDateFormat
): TimePickerDialog {
    val currentMoment = Clock.System.now()
    val date: LocalDateTime =
        currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    val timePickerDialog = TimePickerDialog(
        context,
        android.R.style.Theme_DeviceDefault_Dialog_Alert,
        { _: TimePicker, hourOfDay: Int, minute: Int ->
            val cal = Calendar.getInstance()
            cal[Calendar.HOUR_OF_DAY] = hourOfDay
            cal[Calendar.MINUTE] = minute
            setTimeText(timeFormatter.format(cal.time))
        }, date.hour, date.minute, false
    )
    return timePickerDialog
}