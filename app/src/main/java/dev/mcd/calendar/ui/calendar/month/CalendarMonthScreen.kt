package dev.mcd.calendar.ui.calendar.month

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mcd.calendar.R
import dev.mcd.calendar.feature.calendar.domain.entity.DateEventCount
import dev.mcd.calendar.feature.calendar.domain.entity.MonthDays
import dev.mcd.calendar.ui.calendar.month.CalendarMonthViewModel.SideEffect.NavigateCreateEvent
import dev.mcd.calendar.ui.calendar.month.CalendarMonthViewModel.SideEffect.NavigateToDay
import dev.mcd.calendar.ui.calendar.month.view.CalendarView
import dev.mcd.calendar.ui.calendar.month.view.EventCountText
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarMonthScreen(
    viewModel: CalendarMonthViewModel = hiltViewModel(),
    onNavigateCreateEvent: (LocalDate) -> Unit,
    onNavigateDay: (LocalDate) -> Unit,
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is NavigateCreateEvent -> onNavigateCreateEvent(it.date)
            is NavigateToDay -> onNavigateDay(it.date)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    state.date?.run {
                        Row {
                            val monthText = month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                            Text(
                                modifier = Modifier.alpha(.65f),
                                text = "$year / ",
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(text = monthText)
                        }
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
            if (state.monthDays != null) {
                Calendar(
                    monthDays = state.monthDays!!,
                    events = state.events,
                    onPreviousMonth = { viewModel.onPreviousMonth() },
                    onNextMonth = { viewModel.onNextMonth() },
                    onDateClicked = { date -> viewModel.onDateClicked(date) },
                )
            }
        }
    }
}

@Composable
private fun Calendar(
    monthDays: MonthDays,
    events: Map<LocalDate, DateEventCount>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateClicked: (LocalDate) -> Unit,
) {
    CalendarView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        monthDays = monthDays,
        onCellClicked = { date -> onDateClicked(date.date) },
        renderCell = { date ->
            events[date.date]?.let { count ->
                EventCountText(
                    modifier = Modifier.align(Alignment.Center),
                    count = count.count,
                )
            }
        },
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        TextButton(
            onClick = { onPreviousMonth() },
        ) {
            Text(text = stringResource(id = R.string.calendar_previous_month))
        }
        TextButton(
            onClick = { onNextMonth() },
        ) {
            Text(text = stringResource(id = R.string.calendar_next_month))
        }
    }
}
