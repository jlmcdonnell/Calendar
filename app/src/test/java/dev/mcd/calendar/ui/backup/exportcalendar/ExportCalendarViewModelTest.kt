package dev.mcd.calendar.ui.backup.exportcalendar

import dev.mcd.calendar.feature.backup.domain.BackupStore
import dev.mcd.calendar.feature.backup.domain.ExportDatabase
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test

class ExportCalendarViewModelTest {

    @Test
    fun `Given export URI exists, Then show export button`() = runTest {
        val exportDatabase = mockk<ExportDatabase>()
        val backupStore = mockk<BackupStore> {
            coEvery { exportDirectoryUri() } returns "uri"
        }

        val vm = ExportCalendarViewModel(
            exportDatabase = exportDatabase,
            backupStore = backupStore,
        )

        vm.test(this) {
            runOnCreate()
            expectInitialState()
            awaitState().showExport shouldBe true
        }
    }

    @Test
    fun `Given no export URI, Then hide export button`() = runTest {
        val exportDatabase = mockk<ExportDatabase>()
        val backupStore = mockk<BackupStore> {
            coEvery { exportDirectoryUri() } returns null
        }

        val vm = ExportCalendarViewModel(
            exportDatabase = exportDatabase,
            backupStore = backupStore,
        )
        vm.test(this) {
            runOnCreate()
            expectInitialState()
            // No further state
        }
    }

    @Test
    fun `When export is clicked, Then invoke BackupDatabase`() = runTest {
        val exportDatabase = mockk<ExportDatabase> {
            coEvery { this@mockk.invoke() } returns ExportDatabase.Result.Success
        }
        val backupStore = mockk<BackupStore> {
            coEvery { exportDirectoryUri() } returns "uri"
        }

        val vm = ExportCalendarViewModel(
            exportDatabase = exportDatabase,
            backupStore = backupStore,
        )

        vm.test(this) {
            runOnCreate()
            expectInitialState()

            awaitState()
            vm.onExport()
            awaitSideEffect() shouldBe ExportCalendarViewModel.SideEffect.Dismiss
        }
        coVerify { exportDatabase.invoke() }
    }

    @Test
    fun `When BackupDatabase throws Exception, Then show error`() = runTest {
        val message = "error"
        val exportDatabase = mockk<ExportDatabase> {
            coEvery { this@mockk.invoke() } throws Exception(message)
        }
        val backupStore = mockk<BackupStore> {
            coEvery { exportDirectoryUri() } returns "uri"
        }

        val vm = ExportCalendarViewModel(
            exportDatabase = exportDatabase,
            backupStore = backupStore,
        )

        vm.test(this) {
            runOnCreate()
            expectInitialState()

            awaitState()
            vm.onExport()
            awaitSideEffect() shouldBe ExportCalendarViewModel.SideEffect.ExportError(message)
        }
    }

    @Test
    fun `When BackupDatabase returns DocumentCreateError, Then delete export URI`() = runTest {
        val exportDatabase = mockk<ExportDatabase> {
            coEvery { this@mockk.invoke() } returns ExportDatabase.Result.DocumentCreateError
        }
        val backupStore = mockk<BackupStore> {
            coEvery { exportDirectoryUri() } returns "uri" andThen null
            coEvery { setExportDirectoryUri(null) } just Runs
        }

        val vm = ExportCalendarViewModel(
            exportDatabase = exportDatabase,
            backupStore = backupStore,
        )

        vm.test(this) {
            runOnCreate()
            expectInitialState()

            awaitState()
            vm.onExport()
            awaitState().showExport shouldBe false
        }
        coVerify(exactly = 2) { backupStore.exportDirectoryUri() }
        coVerify { backupStore.setExportDirectoryUri(null) }
    }

    @Test
    fun `When choose export location is clicked, Then emit ChooseExportLocation`() = runTest {
        val exportDatabase = mockk<ExportDatabase>()
        val backupStore = mockk<BackupStore> {
            coEvery { exportDirectoryUri() } returns null
        }

        val vm = ExportCalendarViewModel(
            exportDatabase = exportDatabase,
            backupStore = backupStore,
        )

        vm.test(this) {
            runOnCreate()
            expectInitialState()

            vm.onChooseExportLocation()

            awaitSideEffect() shouldBe ExportCalendarViewModel.SideEffect.ChooseExportLocation
        }
    }

    @Test
    fun `When export location chosen, Then emit showExport=true`() = runTest {
        val exportDatabase = mockk<ExportDatabase>()
        val backupStore = mockk<BackupStore>(relaxed = true) {
            coEvery { exportDirectoryUri() } returns null
        }

        val vm = ExportCalendarViewModel(
            exportDatabase = exportDatabase,
            backupStore = backupStore,
        )

        vm.test(this) {
            runOnCreate()
            expectInitialState()

            vm.onExportUriChosen("uri")

            awaitState().showExport shouldBe true
        }

        coVerify {
            backupStore.setExportDirectoryUri("uri")
        }
    }
}
