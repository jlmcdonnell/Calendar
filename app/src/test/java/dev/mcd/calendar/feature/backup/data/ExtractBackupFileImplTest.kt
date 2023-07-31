package dev.mcd.calendar.feature.backup.data

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ExtractBackupFileImplTest {

    private lateinit var tempDir: TemporaryFolder

    @Before
    fun setUp() {
        tempDir = TemporaryFolder.builder()
            .assureDeletion()
            .build()
        tempDir.create()
    }

    @After
    fun tearDown() {
        tempDir.delete()
    }

    @Test
    fun `When the import file doesn't exist, Return`() = runTest {
        // Given
        val importFile = tempDir.newFile().apply {
            delete()
        }

        val targetDir = tempDir.newFolder("database")
        targetDir.mkdirs()

        val extractBackup = ExtractBackupFileImpl(
            importFile = importFile,
            targetDir = targetDir,
        )

        // When
        extractBackup()

        // Then
        // No errors thrown
    }

    @Test
    fun `When the database folder doesn't exist, Create it`() = runTest {
        // Given
        val importFile = tempDir.newFile()
        val targetDir = tempDir.newFolder("database")
        targetDir.delete()

        val extractBackup = ExtractBackupFileImpl(
            importFile = importFile,
            targetDir = targetDir,
        )

        // When
        extractBackup()

        // Then
        targetDir.exists() shouldBe true
    }

    @Test
    fun `Copy import file to database folder`() = runTest {
        // Given
        val importFile = tempDir.newFile().apply {
            delete()
        }

        val targetDir = tempDir.newFolder("database")
        targetDir.mkdirs()

        writeTestData(importFile)

        val extractBackup = ExtractBackupFileImpl(
            importFile = importFile,
            targetDir = targetDir,
        )

        // When
        extractBackup()

        // Then
        assertTestData(targetDir)
    }

    @Test
    fun `Delete import file after extraction`() = runTest {
        // Given
        val importFile = tempDir.newFile().apply {
            delete()
        }

        val targetDir = tempDir.newFolder("database")
        targetDir.mkdirs()

        writeTestData(importFile)

        val extractBackup = ExtractBackupFileImpl(
            importFile = importFile,
            targetDir = targetDir,
        )

        // When
        extractBackup()

        // Then
        importFile.exists() shouldBe false
    }

    private fun writeTestData(importFile: File) {
        ZipOutputStream(importFile.outputStream()).use { output ->
            repeat(3) {
                output.putNextEntry(ZipEntry("entry_$it"))
                output.write("data_$it".toByteArray())
                output.closeEntry()
            }
        }
    }

    private fun assertTestData(dir: File) {
        repeat(3) {
            val file = File(dir, "entry_$it")
            file.readText() shouldBe "data_$it"
        }
    }
}
