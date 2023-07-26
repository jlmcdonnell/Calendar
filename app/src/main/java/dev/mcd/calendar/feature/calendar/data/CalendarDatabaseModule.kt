package dev.mcd.calendar.feature.calendar.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mcd.calendar.feature.calendar.data.mapper.EventEntityMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CalendarDatabaseModule {

    @Provides
    @Singleton
    fun calendarDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context = context,
        klass = CalendarDatabase::class.java,
        name = DATABASE_NAME,
    )

    @Provides
    fun events(database: CalendarDatabase): Events {
        return database.events()
    }

    @Provides
    fun eventEntityMapper() = EventEntityMapper()

    private companion object {
        const val DATABASE_NAME = "calendar"
    }
}
