package dev.mcd.calendar.ui.backup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mcd.calendar.feature.backup.domain.BackupDatabase
import dev.mcd.calendar.feature.backup.domain.BackupDatabase.Result
import dev.mcd.calendar.feature.backup.domain.BackupStore
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect.ChooseBackupLocation
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect.Dismiss
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.State
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BackupCalendarViewModel @Inject constructor(
    private val backupStore: BackupStore,
    private val backupDatabase: BackupDatabase,
) : ViewModel(), ContainerHost<State, SideEffect> {

    override val container = container(
        initialState = State(),
        onCreate = {
            checkBackupStatus()
        },
    )

    fun onBackup() {
        intent {
            runCatching {
                when (val result = backupDatabase()) {
                    Result.DocumentCreateError -> {
                        Timber.d("$result while backing up database")
                        backupStore.setBackupDirectoryUri(null)
                        checkBackupStatus()
                    }
                    Result.NoBackupUri -> {
                        checkBackupStatus()
                    }
                    Result.Success -> {
                        postSideEffect(Dismiss)
                    }
                }
            }.onFailure { exception ->
                Timber.e(exception, "Backup up database")
                postSideEffect(SideEffect.BackupError(exception.message ?: exception.javaClass.name))
            }
        }
    }

    fun onChooseBackupLocation() {
        intent {
            postSideEffect(ChooseBackupLocation)
        }
    }

    fun onBackupUriChosen(uri: String) {
        intent {
            backupStore.setBackupDirectoryUri(uri)
            reduce {
                state.copy(showBackup = true)
            }
        }
    }

    context(SimpleSyntax<State, SideEffect>)
    private suspend fun checkBackupStatus() {
        val uri = backupStore.backupDirectoryUri()
        reduce {
            state.copy(showBackup = uri != null)
        }
    }

    data class State(
        val showBackup: Boolean = false,
    )

    sealed interface SideEffect {
        data object ChooseBackupLocation : SideEffect
        data class BackupError(val message: String) : SideEffect
        data object Dismiss : SideEffect
    }
}
