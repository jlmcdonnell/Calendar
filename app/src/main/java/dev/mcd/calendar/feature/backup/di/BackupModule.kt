package dev.mcd.calendar.feature.backup.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mcd.calendar.feature.backup.data.BackupDatabaseImpl
import dev.mcd.calendar.feature.backup.data.BackupStoreImpl
import dev.mcd.calendar.feature.backup.data.CopyDatabaseToProviderImpl
import dev.mcd.calendar.feature.backup.domain.BackupDatabase
import dev.mcd.calendar.feature.backup.domain.BackupStore
import dev.mcd.calendar.feature.backup.domain.CopyDatabaseToProvider
import dev.mcd.calendar.feature.calendar.data.di.CalendarDBFolder
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BackupModule {

    @Provides
    @Singleton
    fun backupStore(
        @ApplicationContext context: Context,
    ): BackupStore {
        return BackupStoreImpl(context)
    }

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
    fun copyDatabaseToProvider(
        @BackupFile backupFile: File,
        @CalendarDBFolder databaseFolder: File,
    ): CopyDatabaseToProvider = CopyDatabaseToProviderImpl(
        backupFile = backupFile,
        databaseFolder = databaseFolder,
        dispatcher = Dispatchers.IO,
    )

    @Provides
    fun backupDatabase(
        @ApplicationContext context: Context,
        @BackupFile backupFile: File,
        backupStore: BackupStore,
        copyDatabaseToProvider: CopyDatabaseToProvider,
    ): BackupDatabase = BackupDatabaseImpl(
        context = context,
        backupStore = backupStore,
        backupFile = backupFile,
        copyDatabaseToProvider = copyDatabaseToProvider,
        dispatcher = Dispatchers.IO,
    )

    private companion object {
        const val BACKUP_DIR = "backup"
        const val BACKUP_FILE_NAME = "calendar_backup.zip"
    }
}
