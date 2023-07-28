package dev.mcd.calendar.ui.backup

import dev.mcd.calendar.feature.backup.domain.BackupDatabase
import dev.mcd.calendar.feature.backup.domain.BackupDatabase.Result.DocumentCreateError
import dev.mcd.calendar.feature.backup.domain.BackupDatabase.Result.Success
import dev.mcd.calendar.feature.backup.domain.BackupStore
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect.BackupError
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect.ChooseBackupLocation
import dev.mcd.calendar.ui.backup.BackupCalendarViewModel.SideEffect.Dismiss
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test

class BackupCalendarViewModelTest {

    @Test
    fun `Given backup URI exists, Then show backup button`() = runTest {
        val backupDatabase = mockk<BackupDatabase>()
        val backupStore = mockk<BackupStore> {
            coEvery { backupDirectoryUri() } returns "uri"
        }

        val vm = BackupCalendarViewModel(
            backupDatabase = backupDatabase,
            backupStore = backupStore,
        )

        vm.test(this) {
            runOnCreate()
            expectInitialState()
            awaitState().showBackup shouldBe true
        }
    }

    @Test
    fun `Given no backup URI, Then hide backup button`() = runTest {
        val backupDatabase = mockk<BackupDatabase>()
        val backupStore = mockk<BackupStore> {
            coEvery { backupDirectoryUri() } returns null
        }

        val vm = BackupCalendarViewModel(
            backupDatabase = backupDatabase,
            backupStore = backupStore,
        )
        vm.test(this) {
            runOnCreate()
            expectInitialState()
            // No further state
        }
    }

    @Test
    fun `When backup is clicked, Then invoke BackupDatabase`() = runTest {
        val backupDatabase = mockk<BackupDatabase> {
            coEvery { this@mockk.invoke() } returns Success
        }
        val backupStore = mockk<BackupStore> {
            coEvery { backupDirectoryUri() } returns "uri"
        }

        val vm = BackupCalendarViewModel(
            backupDatabase = backupDatabase,
            backupStore = backupStore,
        )

        vm.test(this) {
            runOnCreate()
            expectInitialState()

            awaitState()
            vm.onBackup()
            awaitSideEffect() shouldBe Dismiss
        }
        coVerify { backupDatabase.invoke() }
    }

    @Test
    fun `When BackupDatabase throws Exception, Then show error`() = runTest {
        val message = "error"
        val backupDatabase = mockk<BackupDatabase> {
            coEvery { this@mockk.invoke() } throws Exception(message)
        }
        val backupStore = mockk<BackupStore> {
            coEvery { backupDirectoryUri() } returns "uri"
        }

        val vm = BackupCalendarViewModel(
            backupDatabase = backupDatabase,
            backupStore = backupStore,
        )

        vm.test(this) {
            runOnCreate()
            expectInitialState()

            awaitState()
            vm.onBackup()
            awaitSideEffect() shouldBe BackupError(message)
        }
    }

    @Test
    fun `When BackupDatabase returns DocumentCreateError, Then delete backup URI`() = runTest {
        val backupDatabase = mockk<BackupDatabase> {
            coEvery { this@mockk.invoke() } returns DocumentCreateError
        }
        val backupStore = mockk<BackupStore> {
            coEvery { backupDirectoryUri() } returns "uri" andThen null
            coEvery { setBackupDirectoryUri(null) } just Runs
        }

        val vm = BackupCalendarViewModel(
            backupDatabase = backupDatabase,
            backupStore = backupStore,
        )

        vm.test(this) {
            runOnCreate()
            expectInitialState()

            awaitState()
            vm.onBackup()
            awaitState().showBackup shouldBe false
        }
        coVerify(exactly = 2) { backupStore.backupDirectoryUri() }
        coVerify { backupStore.setBackupDirectoryUri(null) }
    }

    @Test
    fun `When choose backup location is clicked, Then emit ChooseBackupLocation`() = runTest {
        val backupDatabase = mockk<BackupDatabase>()
        val backupStore = mockk<BackupStore> {
            coEvery { backupDirectoryUri() } returns null
        }

        val vm = BackupCalendarViewModel(
            backupDatabase = backupDatabase,
            backupStore = backupStore,
        )

        vm.test(this) {
            runOnCreate()
            expectInitialState()

            vm.onChooseBackupLocation()

            awaitSideEffect() shouldBe ChooseBackupLocation
        }
    }
}
