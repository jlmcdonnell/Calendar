package dev.mcd.calendar.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mcd.calendar.ui.calendar.view.CalendarView
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun HomeScreen(viewModel: CalendarViewModel = hiltViewModel()) {
    val state by viewModel.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "Calendar") })
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
            }
        }
    }
}
