package dev.mcd.calendar.ui.calendar.day

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mcd.calendar.R
import dev.mcd.calendar.feature.common.date.dateFormatter
import dev.mcd.calendar.ui.calendar.day.DayEventsViewModel.SideEffect.NavigateCreateEvent
import dev.mcd.calendar.ui.calendar.day.view.EventsList
import dev.mcd.calendar.ui.common.component.TopBar
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate

@Composable
fun DayEventsScreen(
    onNavigateBack: () -> Unit,
    onNavigateCreateEvent: (LocalDate) -> Unit,
    viewModel: DayEventsViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is NavigateCreateEvent -> onNavigateCreateEvent(it.date)
        }
    }

    Scaffold(
        topBar = {
            val title = state.date?.let { dateFormatter.format(it) } ?: ""
            TopBar(
                title = title,
                onNavigateBack = onNavigateBack,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddEvent() },
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Rounded.Add),
                    contentDescription = stringResource(id = R.string.event_add),
                )
            }
        },
    ) {
        Column(
            modifier = Modifier.padding(it),
        ) {
            EventsList(
                events = state.events,
                onDeleteEvent = { event -> viewModel.onDeleteEvent(event) },
            )
        }
    }
}
