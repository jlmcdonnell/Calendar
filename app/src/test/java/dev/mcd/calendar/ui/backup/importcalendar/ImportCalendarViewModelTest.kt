package dev.mcd.calendar.ui.backup.importcalendar

import dev.mcd.calendar.feature.backup.domain.ImportBackupFile
import dev.mcd.calendar.ui.backup.importcalendar.ImportCalendarViewModel.SideEffect.ChooseImportLocation
import dev.mcd.calendar.ui.backup.importcalendar.ImportCalendarViewModel.SideEffect.ImportError
import dev.mcd.calendar.ui.backup.importcalendar.ImportCalendarViewModel.SideEffect.Restart
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test

class ImportCalendarViewModelTest {

    @Test
    fun `When Import is clicked, Post ChooseImportLocation`() = runTest {
        val importBackupFile = mockk<ImportBackupFile>()
        val vm = ImportCalendarViewModel(
            importBackupFile = importBackupFile,
            backupFileMimeType = "application/zip",
        )
        vm.test(this) {
            expectInitialState()

            vm.onImport()

            awaitSideEffect() shouldBe ChooseImportLocation(mimeType = "application/zip")
        }
    }

    @Test
    fun `When Import URI is chosen, Import database`() = runTest {
        val importBackupFile = mockk<ImportBackupFile>(relaxed = true)
        val vm = ImportCalendarViewModel(
            importBackupFile = importBackupFile,
            backupFileMimeType = "application/zip",
        )
        vm.test(this) {
            expectInitialState()

            vm.onImportUriChosen("uri")

            awaitSideEffect() shouldBe Restart
        }

        coVerify { importBackupFile.invoke("uri") }
    }

    @Test
    fun `When Importing database fails, Post ImportError`() = runTest {
        val importBackupFile = mockk<ImportBackupFile>(relaxed = true) {
            coEvery { this@mockk.invoke("uri") } throws Exception("message")
        }

        val vm = ImportCalendarViewModel(
            importBackupFile = importBackupFile,
            backupFileMimeType = "application/zip",
        )
        vm.test(this) {
            expectInitialState()

            vm.onImportUriChosen("uri")

            awaitSideEffect() shouldBe ImportError("message")
        }

        coVerify { importBackupFile.invoke("uri") }
    }
}
