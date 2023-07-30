package dev.mcd.calendar.ui.backup

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mcd.calendar.R
import dev.mcd.calendar.ui.backup.ExportCalendarViewModel.SideEffect.ChooseExportLocation
import dev.mcd.calendar.ui.backup.ExportCalendarViewModel.SideEffect.Dismiss
import dev.mcd.calendar.ui.backup.ExportCalendarViewModel.SideEffect.ExportError
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ExportCalendarDialog(
    viewModel: ExportCalendarViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri ->
            uri?.let { viewModel.onExportUriChosen(uri.toString()) }
        },
    )

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is ChooseExportLocation -> {
                launcher.launch(null)
            }
            is ExportError -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
            is Dismiss -> {
                onDismiss()
            }
        }
    }

    ExportDialog(
        showExport = state.showExport,
        onDismiss = onDismiss,
        onExport = { viewModel.onExport() },
        onChooseExportLocation = { viewModel.onChooseExportLocation() },
    )
}

@Composable
private fun ExportDialog(
    showExport: Boolean,
    onDismiss: () -> Unit,
    onExport: () -> Unit,
    onChooseExportLocation: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .width(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.export_dialog_title),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(modifier = Modifier.height(24.dp))

                if (showExport) {
                    OutlinedButton(
                        onClick = { onExport() },
                    ) {
                        Text(text = stringResource(id = R.string.export_dialog_export))
                    }
                } else {
                    OutlinedButton(
                        onClick = { onChooseExportLocation() },
                    ) {
                        Text(text = stringResource(id = R.string.export_dialog_choose_location))
                    }
                }
            }
        }
    }
}
