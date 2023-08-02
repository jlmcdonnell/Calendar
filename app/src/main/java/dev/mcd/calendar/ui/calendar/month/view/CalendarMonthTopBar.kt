package dev.mcd.calendar.ui.calendar.month.view

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.Save
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
    showReturnToDate: Boolean,
    onExportClicked: () -> Unit,
    onImportClicked: () -> Unit,
    onReturnToDateClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            date?.run {
                DateTitle(date = date)
            } ?: run {
                Text(text = stringResource(id = R.string.app_name))
            }
        },
        actions = {
            if (showReturnToDate) {
                ReturnToDateButton {
                    onReturnToDateClicked()
                }
            }
            ImportButton(onImport = onImportClicked)
            ExportButton(onExport = onExportClicked)
        },
    )
}

@Composable
private fun ReturnToDateButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Rounded.Restore),
            contentDescription = stringResource(id = R.string.calendar_return_to_date),
        )
    }
}

@Composable
private fun DateTitle(date: LocalDate) {
    Row {
        val monthText = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        Text(
            modifier = Modifier.alpha(.65f),
            text = "${date.year} / ",
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(text = monthText)
    }
}

@Composable
private fun ExportButton(onExport: () -> Unit) {
    IconButton(
        onClick = {
            onExport()
        },
    ) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Rounded.Save),
            contentDescription = stringResource(id = R.string.calendar_export),
        )
    }
}

@Composable
private fun ImportButton(onImport: () -> Unit) {
    IconButton(
        onClick = {
            onImport()
        },
    ) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Default.FolderOpen),
            contentDescription = stringResource(id = R.string.calendar_import),
        )
    }
}
