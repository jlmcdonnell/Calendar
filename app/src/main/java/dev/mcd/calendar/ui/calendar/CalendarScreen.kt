package dev.mcd.calendar.ui.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mcd.calendar.R
import dev.mcd.calendar.ui.calendar.view.CalendarView
import org.orbitmvi.orbit.compose.collectAsState
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(viewModel: CalendarViewModel = hiltViewModel()) {
    val state by viewModel.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    state.date?.run {
                        Text(text = month.getDisplayName(TextStyle.FULL, Locale.getDefault()))
                    } ?: run {
                        Text(text = stringResource(id = R.string.app_name))
                    }
                },
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(it),
        ) {
            state.data?.let { monthData ->
                CalendarView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    monthData = monthData,
                ) { date ->
                    // TODO: Render date cell
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    TextButton(
                        onClick = { viewModel.onPreviousMonth() },
                    ) {
                        Text(text = stringResource(id = R.string.calendar_previous_month))
                    }
                    TextButton(
                        onClick = { viewModel.onNextMonth() },
                    ) {
                        Text(text = stringResource(id = R.string.calendar_next_month))
                    }
                }
            }
        }
    }
}
