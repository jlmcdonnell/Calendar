package dev.mcd.calendar.feature.backup.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mcd.calendar.feature.backup.data.BackupStoreImpl
import dev.mcd.calendar.feature.backup.data.CopyDatabaseToProviderImpl
import dev.mcd.calendar.feature.backup.data.ExportDatabaseImpl
import dev.mcd.calendar.feature.backup.data.ExtractBackupFileImpl
import dev.mcd.calendar.feature.backup.data.ImportBackupFileImpl
import dev.mcd.calendar.feature.backup.domain.BackupStore
import dev.mcd.calendar.feature.backup.domain.CopyDatabaseToProvider
import dev.mcd.calendar.feature.backup.domain.ExportDatabase
import dev.mcd.calendar.feature.backup.domain.ExtractBackupFile
import dev.mcd.calendar.feature.backup.domain.ImportBackupFile
import dev.mcd.calendar.feature.calendar.data.di.CalendarDBFolder
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BackupModule {

    //
    // Backup Common
    //

    @Provides
    @BackupFileMimeType
    fun backupFileMimeType() = BACKUP_FILE_MIME_TYPE

    @Provides
    @Singleton
    fun backupStore(
        @ApplicationContext context: Context,
    ): BackupStore {
        return BackupStoreImpl(context)
    }

    //
    // Export
    //

    @Provides
    @Singleton
    @ExportFile
    fun exportFile(
        @ApplicationContext context: Context,
    ): File {
        val folder = File(context.filesDir, EXPORT_DIR)
        folder.mkdirs()
        return File(folder, BACKUP_FILE_NAME)
    }

    @Provides
    fun copyDatabaseToProvider(
        @ExportFile exportFile: File,
        @CalendarDBFolder databaseFolder: File,
    ): CopyDatabaseToProvider = CopyDatabaseToProviderImpl(
        exportFile = exportFile,
        databaseFolder = databaseFolder,
        dispatcher = Dispatchers.IO,
    )

    @Provides
    fun exportDatabase(
        @ApplicationContext context: Context,
        @ExportFile exportFile: File,
        backupStore: BackupStore,
        copyDatabaseToProvider: CopyDatabaseToProvider,
        @BackupFileMimeType
        backupFileMimeType: String,
    ): ExportDatabase = ExportDatabaseImpl(
        context = context,
        backupStore = backupStore,
        exportFile = exportFile,
        backupFileMimeType = backupFileMimeType,
        dispatcher = Dispatchers.IO,
        copyDatabaseToProvider = copyDatabaseToProvider,
    )

    //
    // Import
    //

    @Provides
    fun importBackupFile(
        @ApplicationContext
        context: Context,
        @ImportFile
        file: File,
    ): ImportBackupFile = ImportBackupFileImpl(
        context = context,
        importFile = file,
    )

    @Provides
    @Singleton
    @ImportFile
    fun importFile(
        @ApplicationContext context: Context,
    ): File {
        val folder = File(context.filesDir, IMPORT_DIR)
        folder.mkdirs()
        return File(folder, BACKUP_FILE_NAME)
    }

    @Provides
    fun extractBackupFile(
        @ImportFile importFile: File,
        @CalendarDBFolder dbFolder: File,
    ): ExtractBackupFile = ExtractBackupFileImpl(
        importFile = importFile,
        targetDir = dbFolder,
    )

    private companion object {
        const val BACKUP_FILE_MIME_TYPE = "application/zip"
        const val EXPORT_DIR = "export"
        const val IMPORT_DIR = "import"
        const val BACKUP_FILE_NAME = "calendar_backup.zip"
    }
}
