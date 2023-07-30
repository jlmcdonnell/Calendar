package dev.mcd.calendar.feature.backup.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import dev.mcd.calendar.feature.backup.di.ExportFile
import dev.mcd.calendar.feature.backup.domain.BackupStore
import dev.mcd.calendar.feature.backup.domain.CopyDatabaseToProvider
import dev.mcd.calendar.feature.backup.domain.ExportDatabase
import dev.mcd.calendar.feature.backup.domain.ExportDatabase.Result
import dev.mcd.calendar.feature.backup.domain.ExportDatabase.Result.DocumentCreateError
import dev.mcd.calendar.feature.backup.domain.ExportDatabase.Result.NoExportUri
import dev.mcd.calendar.feature.backup.domain.ExportDatabase.Result.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File

class ExportDatabaseImpl(
    private val context: Context,
    private val backupStore: BackupStore,
    private val copyDatabaseToProvider: CopyDatabaseToProvider,
    @ExportFile
    private val exportFile: File,
    private val dispatcher: CoroutineDispatcher,
) : ExportDatabase {

    override suspend fun invoke(): Result {
        return withContext(dispatcher) {
            val uriString = backupStore.exportDirectoryUri() ?: return@withContext NoExportUri
            val uri = Uri.parse(uriString)

            copyDatabaseToProvider()

            takePermission(uri)

            val backupFolder = DocumentFile.fromTreeUri(context, uri) ?: return@withContext DocumentCreateError
            val userBackupFile = findOrCreateFile(backupFolder)
            check(userBackupFile != null) { return@withContext DocumentCreateError }

            copy(userBackupFile.uri)

            Success
        }
    }

    private fun findOrCreateFile(folder: DocumentFile): DocumentFile? {
        return folder.findFile(exportFile.name) ?: folder.createFile("application/zip", exportFile.name)
    }

    private fun copy(uri: Uri) {
        context.contentResolver.openOutputStream(uri)?.use {
            exportFile.inputStream().copyTo(it)
        } ?: throw Exception("Unable to open output stream")
    }

    private fun takePermission(uri: Uri) {
        val modeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        context.contentResolver.takePersistableUriPermission(uri, modeFlags)
    }
}
