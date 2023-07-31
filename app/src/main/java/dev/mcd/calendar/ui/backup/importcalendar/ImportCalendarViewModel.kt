package dev.mcd.calendar.ui.backup.importcalendar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mcd.calendar.feature.backup.di.BackupFileMimeType
import dev.mcd.calendar.feature.backup.domain.ImportBackupFile
import dev.mcd.calendar.ui.backup.importcalendar.ImportCalendarViewModel.SideEffect
import dev.mcd.calendar.ui.backup.importcalendar.ImportCalendarViewModel.State
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ImportCalendarViewModel @Inject constructor(
    val importBackupFile: ImportBackupFile,
    @BackupFileMimeType
    val backupFileMimeType: String,
) : ViewModel(), ContainerHost<State, SideEffect> {

    override val container = container<State, SideEffect>(State)

    fun onImport() {
        intent {
            postSideEffect(SideEffect.ChooseImportLocation(mimeType = backupFileMimeType))
        }
    }

    fun onImportUriChosen(uri: String) {
        intent {
            runCatching {
                importBackupFile(uri)
                postSideEffect(SideEffect.Restart)
            }.onFailure {
                postSideEffect(SideEffect.ImportError(it.message ?: it.javaClass.name))
            }
        }
    }

    object State

    sealed interface SideEffect {
        data class ChooseImportLocation(val mimeType: String) : SideEffect
        data class ImportError(val message: String) : SideEffect
        data object Restart : SideEffect
    }
}
