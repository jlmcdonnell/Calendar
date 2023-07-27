package dev.mcd.calendar.ui.events.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mcd.calendar.R

@Composable
fun CreateEventScreen(
    viewModel: CreateEventViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = { TopBar() },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
        ) {
            Text(text = "TODO")
        }
    }
}

@Composable
private fun TopBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.create_event_title))
        },
    )
}
