@file:Suppress("BlockingMethodInNonBlockingContext")

package dev.mcd.calendar.feature.backup.data

import dev.mcd.calendar.feature.backup.di.ExportFile
import dev.mcd.calendar.feature.backup.domain.CopyDatabaseToProvider
import dev.mcd.calendar.feature.calendar.data.di.CalendarDBFolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

class CopyDatabaseToProviderImpl @Inject constructor(
    @ExportFile
    private val exportFile: File,
    @CalendarDBFolder
    private val databaseFolder: File,
    private val dispatcher: CoroutineDispatcher,
) : CopyDatabaseToProvider {

    override suspend fun invoke() {
        if (exportFile.exists()) {
            exportFile.delete()
        }
        exportFile.createNewFile()

        withContext(dispatcher) {
            ZipOutputStream(exportFile.outputStream()).use { out ->
                databaseFolder.listFiles()?.forEach { file ->
                    out.putNextEntry(ZipEntry(file.name))
                    out.write(file.readBytes())
                    out.closeEntry()
                }
            }
        }
    }
}
