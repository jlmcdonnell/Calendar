package dev.mcd.calendar.ui.backup.importcalendar

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mcd.calendar.R
import dev.mcd.calendar.ui.backup.importcalendar.ImportCalendarViewModel.SideEffect.ChooseImportLocation
import dev.mcd.calendar.ui.backup.importcalendar.ImportCalendarViewModel.SideEffect.ImportError
import dev.mcd.calendar.ui.backup.importcalendar.ImportCalendarViewModel.SideEffect.Restart
import dev.mcd.calendar.ui.common.extensions.restartApplication
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ImportCalendarDialog(
    viewModel: ImportCalendarViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let { viewModel.onImportUriChosen(uri.toString()) }
        },
    )

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is ChooseImportLocation -> {
                launcher.launch(arrayOf(effect.mimeType))
            }
            is ImportError -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
            is Restart -> {
                context.restartApplication()
            }
        }
    }

    ImportDialog(
        onDismiss = onDismiss,
        onImport = { viewModel.onImport() },
    )
}

@Composable
private fun ImportDialog(
    onDismiss: () -> Unit,
    onImport: () -> Unit,
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
                    text = stringResource(id = R.string.import_dialog_title),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedButton(
                    onClick = { onImport() },
                ) {
                    Text(text = stringResource(id = R.string.import_dialog_import))
                }
            }
        }
    }
}
