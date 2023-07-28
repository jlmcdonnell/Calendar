package dev.mcd.calendar.feature.backup.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import dev.mcd.calendar.feature.backup.di.BackupFile
import dev.mcd.calendar.feature.backup.domain.BackupDatabase
import dev.mcd.calendar.feature.backup.domain.BackupDatabase.Result
import dev.mcd.calendar.feature.backup.domain.BackupDatabase.Result.DocumentCreateError
import dev.mcd.calendar.feature.backup.domain.BackupDatabase.Result.NoBackupUri
import dev.mcd.calendar.feature.backup.domain.BackupDatabase.Result.Success
import dev.mcd.calendar.feature.backup.domain.BackupStore
import dev.mcd.calendar.feature.backup.domain.CopyDatabaseToProvider
import java.io.File

class BackupDatabaseImpl(
    private val context: Context,
    private val backupStore: BackupStore,
    private val copyDatabaseToProvider: CopyDatabaseToProvider,
    @BackupFile
    private val backupFile: File,
) : BackupDatabase {

    override suspend fun invoke(): Result {
        val uriString = backupStore.backupDirectoryUri() ?: return NoBackupUri
        val uri = Uri.parse(uriString)

        copyDatabaseToProvider()

        takePermission(uri)

        val backupFolder = DocumentFile.fromTreeUri(context, uri) ?: return DocumentCreateError
        val userBackupFile = findOrCreateFile(backupFolder)
        check(userBackupFile != null) { return DocumentCreateError }

        copy(userBackupFile.uri)

        return Success
    }

    private fun findOrCreateFile(folder: DocumentFile): DocumentFile? {
        return folder.findFile(backupFile.name) ?: folder.createFile("application/zip", backupFile.name)
    }

    private fun copy(uri: Uri) {
        context.contentResolver.openOutputStream(uri)?.use {
            backupFile.inputStream().copyTo(it)
        } ?: throw Exception("Unable to open output stream")
    }

    private fun takePermission(uri: Uri) {
        val modeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        context.contentResolver.takePersistableUriPermission(uri, modeFlags)
    }
}
