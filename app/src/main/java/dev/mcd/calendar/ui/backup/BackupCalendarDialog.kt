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
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect.BackupError
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect.ChooseBackupLocation
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect.Dismiss
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun BackupCalendarDialog(
    viewModel: BackupCalendarViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri ->
            uri?.let { viewModel.onBackupUriChosen(uri) }
        },
    )

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is ChooseBackupLocation -> {
                launcher.launch(null)
            }
            is BackupError -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
            is Dismiss -> {
                onDismiss()
            }
        }
    }

    BackupDialog(
        showBackup = state.showBackup,
        onDismiss = onDismiss,
        onBackup = { viewModel.onBackup() },
        onChooseBackupLocation = { viewModel.onChooseBackupLocation() },
    )
}

@Composable
private fun BackupDialog(
    showBackup: Boolean,
    onDismiss: () -> Unit,
    onBackup: () -> Unit,
    onChooseBackupLocation: () -> Unit,
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
                    text = stringResource(id = R.string.backup_title),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(modifier = Modifier.height(24.dp))

                if (showBackup) {
                    OutlinedButton(
                        onClick = { onBackup() },
                    ) {
                        Text(text = stringResource(id = R.string.backup))
                    }
                } else {
                    OutlinedButton(
                        onClick = { onChooseBackupLocation() },
                    ) {
                        Text(text = stringResource(id = R.string.backup_choose_location))
                    }
                }
            }
        }
    }
}
