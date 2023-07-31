package dev.mcd.calendar.feature.calendar.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mcd.calendar.feature.backup.domain.ExtractBackupFile
import dev.mcd.calendar.feature.calendar.data.dao.Events
import dev.mcd.calendar.feature.calendar.data.database.CalendarDatabase
import dev.mcd.calendar.feature.calendar.data.mapper.EventEntityMapper
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

@Module
@InstallIn(SingletonComponent::class)
class CalendarDatabaseModule {

    @Provides
    @CalendarDBName
    fun provideCalendarDBName() = DATABASE_NAME

    @Provides
    @Singleton
    @CalendarDBFolder
    fun provideCalendarDBFolder(
        @ApplicationContext context: Context,
    ): File = File(context.dataDir, "databases")

    @Provides
    @Singleton
    fun calendarDatabase(
        @ApplicationContext context: Context,
        @CalendarDBName dbName: String,
        extractBackupFile: ExtractBackupFile,
    ): CalendarDatabase {
        runBlocking {
            val time = measureTimeMillis {
                extractBackupFile()
            }
            Timber.d("Ran DB extract: $time ms")
        }
        return Room.databaseBuilder(
            context = context,
            klass = CalendarDatabase::class.java,
            name = dbName,
        ).build()
    }

    @Provides
    fun events(database: CalendarDatabase): Events {
        return database.events()
    }

    @Provides
    fun eventEntityMapper() = EventEntityMapper()

    private companion object {
        const val DATABASE_NAME = "calendar.db"
    }
}
