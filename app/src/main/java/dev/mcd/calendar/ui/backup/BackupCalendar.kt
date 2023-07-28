package dev.mcd.calendar.ui.backup

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mcd.calendar.R
import dev.mcd.calendar.feature.backup.data.buildBackupIntent
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect.ShareBackup
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.io.File

@Composable
fun BackupCalendarDialog(
    viewModel: BackupCalendarViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()
    var backupFile by remember { mutableStateOf<File?>(null) }

    LaunchedEffect(backupFile) {
        if (backupFile != null) {
            val intent = buildBackupIntent(
                context = context,
                backupFile = backupFile!!,
            )
            context.startActivity(intent)
            onDismiss()
        }
    }

    viewModel.collectSideEffect { effect ->
        if (effect is ShareBackup) {
            backupFile = effect.backupFile
        }
    }

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
                OutlinedButton(
                    onClick = { viewModel.onBackup() },
                    enabled = state.ready,
                ) {
                    Text(text = stringResource(id = R.string.backup))
                }
            }
        }
    }
}
