@file:Suppress("BlockingMethodInNonBlockingContext")

package dev.mcd.calendar.feature.backup.data

import dev.mcd.calendar.feature.backup.di.BackupFolder
import dev.mcd.calendar.feature.backup.domain.BackupDatabase
import dev.mcd.calendar.feature.calendar.data.di.CalendarDBFolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

class BackupDatabaseImpl @Inject constructor(
    @BackupFolder
    private val backupFolder: File,
    private val backupFileName: String,
    @CalendarDBFolder
    private val databaseFolder: File,
    private val dispatcher: CoroutineDispatcher,
) : BackupDatabase {

    override suspend fun invoke() {
        withContext(dispatcher) {
            val backupFile = File(backupFolder, backupFileName)
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
