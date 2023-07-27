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

@Module
@InstallIn(SingletonComponent::class)
class BackupModule {

    @Provides
    @BackupFolder
    fun backupFolder(
        @ApplicationContext context: Context,
    ): File {
        return File(context.filesDir, BACKUP_DIR)
    }

    @Provides
    fun backupDatabase(
        @BackupFolder backupFolder: File,
        @CalendarDBFolder databaseFolder: File,
    ): BackupDatabase = BackupDatabaseImpl(
        backupFolder = backupFolder,
        backupFileName = BACKUP_FILE_NAME,
        databaseFolder = databaseFolder,
        dispatcher = Dispatchers.IO,
    )

    private companion object {
        const val BACKUP_DIR = "backup"
        const val BACKUP_FILE_NAME = "calendar_backup.zip"
    }
}
