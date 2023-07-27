package dev.mcd.calendar.ui.events.create.view

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun DatePicker(
    onDismissRequest: () -> Unit,
    onUpdateDate: (LocalDate) -> Unit,
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Instant
                            .ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault()).toLocalDate()

                        onUpdateDate(date)
                    }
                    onDismissRequest()
                },
            ) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}
