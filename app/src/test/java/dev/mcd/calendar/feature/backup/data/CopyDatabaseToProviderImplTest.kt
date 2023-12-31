@file:Suppress("BlockingMethodInNonBlockingContext")

package dev.mcd.calendar.feature.backup.data

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.zip.ZipFile

class CopyDatabaseToProviderImplTest {

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
    fun `Copy database files to export folder`() = runTest {
        val testFolder = tempDir.newFolder()
        val databaseFolder = File(testFolder, "database").apply { mkdir() }
        val exportFolder = File(testFolder, "export").apply { mkdir() }
        val exportFile = File(exportFolder, "export.zip")

        val dbFiles = 0.until(2).map {
            File(databaseFolder, "file$it").also { file ->
                file.createNewFile()
                file.writeText(text = "data")
            }
        }

        CopyDatabaseToProviderImpl(
            exportFile = exportFile,
            databaseFolder = databaseFolder,
            dispatcher = Dispatchers.Unconfined,
        ).invoke()

        ZipFile(exportFile).use { zip ->
            val entries = zip.entries().toList()
            entries.size shouldBe 2
            entries.forEachIndexed { i, entry ->
                val data = zip.getInputStream(entry).reader().readText()
                dbFiles[i].readText() shouldBe data
            }
        }
    }
}
