package dev.mcd.calendar.ui.backup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mcd.calendar.feature.backup.di.BackupFile
import dev.mcd.calendar.feature.backup.domain.BackupDatabase
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect.ShareBackup
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.State
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BackupCalendarViewModel @Inject constructor(
    private val backupDatabase: BackupDatabase,
    @BackupFile
    private val backupFile: File,
) : ViewModel(), ContainerHost<State, SideEffect> {

    override val container = container<State, SideEffect>(
        initialState = State(),
        onCreate = {
            backupDatabase()
            reduce {
                state.copy(ready = true)
            }
        },
    )

    fun onBackup() {
        intent {
            postSideEffect(ShareBackup(backupFile))
        }
    }

    data class State(
        val ready: Boolean = false,
    )

    sealed interface SideEffect {
        data class ShareBackup(val backupFile: File) : SideEffect
    }
}
