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
import dev.mcd.calendar.feature.backup.domain.BackupStore
import dev.mcd.calendar.feature.backup.domain.CopyDatabaseToProvider
import dev.mcd.calendar.feature.backup.domain.ExportDatabase
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
    @ExportFile
    fun exportFile(
        @ApplicationContext context: Context,
    ): File {
        val folder = File(context.filesDir, EXPORT_DIR)
        folder.mkdirs()
        return File(folder, EXPORT_FILE_NAME)
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
    fun backupDatabase(
        @ApplicationContext context: Context,
        @ExportFile exportFile: File,
        backupStore: BackupStore,
        copyDatabaseToProvider: CopyDatabaseToProvider,
    ): ExportDatabase = ExportDatabaseImpl(
        context = context,
        backupStore = backupStore,
        exportFile = exportFile,
        copyDatabaseToProvider = copyDatabaseToProvider,
        dispatcher = Dispatchers.IO,
    )

    private companion object {
        const val EXPORT_DIR = "export"
        const val EXPORT_FILE_NAME = "calendar_backup.zip"
    }
}
