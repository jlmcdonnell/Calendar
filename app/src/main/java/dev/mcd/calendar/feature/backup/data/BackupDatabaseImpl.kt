@file:Suppress("BlockingMethodInNonBlockingContext")

package dev.mcd.calendar.feature.backup.data

import dev.mcd.calendar.feature.backup.di.BackupFile
import dev.mcd.calendar.feature.backup.domain.BackupDatabase
import dev.mcd.calendar.feature.calendar.data.di.CalendarDBFolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

class BackupDatabaseImpl @Inject constructor(
    @BackupFile
    private val backupFile: File,
    @CalendarDBFolder
    private val databaseFolder: File,
    private val dispatcher: CoroutineDispatcher,
) : BackupDatabase {

    override suspend fun invoke() {
        if (backupFile.exists()) {
            backupFile.delete()
        }
        backupFile.createNewFile()

        withContext(dispatcher) {
            ZipOutputStream(backupFile.outputStream()).use { out ->
                databaseFolder.listFiles()?.forEach { file ->
                    out.putNextEntry(ZipEntry(file.name))
                    out.write(file.readBytes())
                    out.closeEntry()
                }
            }
        }
    }
}
