package com.cornellappdev.transit.ui.components

import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.Style
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Uneditable text field that allows users to select a date using datePickerDialog.
 *
 * @param dateState State that represents the current date selected by user
 * @param dateFormatter Formatter used for displaying the text of the date
 * @param modifier Modifier to be applied to the [DatePicker]
 * @param disabled Whether the field is disabled
 * @param onDateChanged Callback called when the user changes the date selected.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DatePicker(
    date: String,
    dateFormatter: SimpleDateFormat,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    onDateChanged: (String) -> Unit,
) {

    val datePickerDialog = createDatePickerDialog(
        LocalContext.current,
        onDateChanged,
        dateFormatter
    )

    Row(
        modifier = modifier
            .width(150.dp)
            .height(40.dp)
            .clip(
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                DividerGray
            )
            .clickable {
                if (!disabled) {
                    datePickerDialog.show()
                }
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            text = if (disabled) dateFormatter.format(Date.from(Instant.now())) else date,
            style = Style.heading3,
            color = if (disabled) Color.DarkGray else Color.Black
        )

    }
}

fun createDatePickerDialog(
    context: Context,
    setDateText: (String) -> Unit,
    dateFormatter: SimpleDateFormat
): DatePickerDialog {
    val currentMoment = Clock.System.now()
    val date: LocalDateTime =
        currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    val datePickerDialog = DatePickerDialog(
        context,
        android.R.style.Theme_DeviceDefault_Dialog_Alert,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            setDateText(dateFormatter.format(cal.time))
        }, date.year, date.monthNumber - 1, date.dayOfMonth
    )
    datePickerDialog.datePicker.minDate = currentMoment.toEpochMilliseconds()
    return datePickerDialog
}