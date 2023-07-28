package dev.mcd.calendar.feature.backup.data

import android.content.Context
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

        val backupFolder = DocumentFile.fromTreeUri(context, uri) ?: return DocumentCreateError
        val userBackupFile = backupFolder.findFile(backupFile.name) ?: backupFolder.createFile("application/zip", backupFile.name)

        check(userBackupFile != null) { return DocumentCreateError }

        context.contentResolver.openOutputStream(userBackupFile.uri)?.use {
            backupFile.inputStream().copyTo(it)
        } ?: throw Exception("Unable to open output stream")

        return Success
    }
}
