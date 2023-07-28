package dev.mcd.calendar.feature.backup.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mcd.calendar.feature.backup.data.BackupDatabaseImpl
import dev.mcd.calendar.feature.backup.domain.BackupDatabase
import dev.mcd.calendar.feature.calendar.data.di.CalendarDBFolder
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BackupModule {

    @Provides
    @Singleton
    @BackupFile
    fun backupFile(
        @ApplicationContext context: Context,
    ): File {
        val folder = File(context.filesDir, BACKUP_DIR)
        folder.mkdirs()
        return File(folder, BACKUP_FILE_NAME)
    }

    @Provides
    fun backupDatabase(
        @BackupFile backupFile: File,
        @CalendarDBFolder databaseFolder: File,
    ): BackupDatabase = BackupDatabaseImpl(
        backupFile = backupFile,
        databaseFolder = databaseFolder,
        dispatcher = Dispatchers.IO,
    )

    private companion object {
        const val BACKUP_DIR = "backup"
        const val BACKUP_FILE_NAME = "calendar_backup.zip"
    }
}
