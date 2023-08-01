package dev.mcd.calendar.ui.backup.exportcalendar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mcd.calendar.feature.backup.domain.BackupStore
import dev.mcd.calendar.feature.backup.domain.ExportDatabase
import dev.mcd.calendar.feature.backup.domain.ExportDatabase.Result
import dev.mcd.calendar.ui.backup.exportcalendar.ExportCalendarViewModel.SideEffect
import dev.mcd.calendar.ui.backup.exportcalendar.ExportCalendarViewModel.SideEffect.ChooseExportLocation
import dev.mcd.calendar.ui.backup.exportcalendar.ExportCalendarViewModel.SideEffect.Dismiss
import dev.mcd.calendar.ui.backup.exportcalendar.ExportCalendarViewModel.State
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExportCalendarViewModel @Inject constructor(
    private val backupStore: BackupStore,
    private val exportDatabase: ExportDatabase,
) : ViewModel(), ContainerHost<State, SideEffect> {

    override val container = container(
        initialState = State(),
        onCreate = {
            checkExportStatus()
        },
    )

    fun onExport() {
        intent {
            runCatching {
                when (val result = exportDatabase()) {
                    Result.DocumentCreateError -> {
                        Timber.d("$result while backing up database")
                        backupStore.setExportDirectoryUri(null)
                        checkExportStatus()
                    }
                    Result.NoExportUri -> {
                        checkExportStatus()
                    }
                    Result.Success -> {
                        postSideEffect(Dismiss)
                    }
                }
            }.onFailure { exception ->
                Timber.e(exception, "Exporting database")
                postSideEffect(SideEffect.ExportError(exception.message ?: exception.javaClass.name))
            }
        }
    }

    fun onChooseExportLocation() {
        intent {
            postSideEffect(ChooseExportLocation)
        }
    }

    fun onExportUriChosen(uri: String) {
        intent {
            backupStore.setExportDirectoryUri(uri)
            reduce {
                state.copy(showExport = true)
            }
        }
    }

    context(SimpleSyntax<State, SideEffect>)
    private suspend fun checkExportStatus() {
        val uri = backupStore.exportDirectoryUri()
        reduce {
            state.copy(showExport = uri != null)
        }
    }

    data class State(
        val showExport: Boolean = false,
    )

    sealed interface SideEffect {
        data object ChooseExportLocation : SideEffect
        data class ExportError(val message: String) : SideEffect
        data object Dismiss : SideEffect
    }
}
