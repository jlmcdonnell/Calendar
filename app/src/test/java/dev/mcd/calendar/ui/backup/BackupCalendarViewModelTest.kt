package dev.mcd.calendar.ui.backup

import dev.mcd.calendar.feature.backup.domain.BackupDatabase
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect.ShareBackup
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test
import java.io.File

class BackupCalendarViewModelTest {

    @Test
    fun `When created, Backup database`() = runTest {
        val backupDatabase = mockk<BackupDatabase>(relaxed = true)
        val backupFile = mockk<File>()
        val vm = BackupCalendarViewModel(
            backupDatabase = backupDatabase,
            backupFile = backupFile,
        )
        vm.test(this) {
            runOnCreate()
            expectInitialState()
            awaitState().ready shouldBe true
        }
        coVerify { backupDatabase() }
    }

    @Test
    fun `When backup is clicked, Post ShareBackup`() = runTest {
        val backupDatabase = mockk<BackupDatabase>(relaxed = true)
        val backupFile = mockk<File>()
        val vm = BackupCalendarViewModel(
            backupDatabase = backupDatabase,
            backupFile = backupFile,
        )
        vm.test(this) {
            runOnCreate()
            expectInitialState()
            awaitState()

            vm.onBackup()

            awaitSideEffect() shouldBe ShareBackup(backupFile = backupFile)
        }
        coVerify { backupDatabase() }
    }
}
