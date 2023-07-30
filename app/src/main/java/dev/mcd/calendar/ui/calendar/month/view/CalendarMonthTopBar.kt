package dev.mcd.calendar.ui.calendar.month.view

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import dev.mcd.calendar.R
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarMonthTopBar(
    date: LocalDate?,
    onBackupClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            date?.run {
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
        actions = {
            IconButton(
                onClick = {
                    onBackupClicked()
                },
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Rounded.Backup),
                    contentDescription = stringResource(id = R.string.calendar_export),
                )
            }
        },
    )
}
