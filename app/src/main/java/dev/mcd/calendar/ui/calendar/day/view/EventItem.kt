package dev.mcd.calendar.ui.calendar.day.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.mcd.calendar.R
import dev.mcd.calendar.feature.calendar.domain.entity.Event
import dev.mcd.calendar.feature.common.date.timeFormatter

@Composable
fun EventItem(
    modifier: Modifier = Modifier,
    event: Event,
    onDeleteEvent: (Event) -> Unit,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier,
                text = event.title,
                style = MaterialTheme.typography.headlineSmall,
            )
            if (event.description.isNotBlank()) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = event.description,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Rounded.AccessTime),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = timeFormatter.format(event.time),
                    )
                }
                IconButton(
                    onClick = { onDeleteEvent(event) },
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Rounded.Delete),
                        contentDescription = stringResource(id = R.string.event_delete),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
