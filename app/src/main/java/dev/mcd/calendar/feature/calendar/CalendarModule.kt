package dev.mcd.calendar.feature.calendar

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mcd.calendar.feature.calendar.data.EventsRepositoryImpl
import dev.mcd.calendar.feature.calendar.data.dao.Events
import dev.mcd.calendar.feature.calendar.data.mapper.EventEntityMapper
import dev.mcd.calendar.feature.calendar.domain.EventsRepository
import dev.mcd.calendar.feature.calendar.domain.GetEventsForMonth
import dev.mcd.calendar.feature.calendar.domain.GetMonthDays
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CalendarModule {

    @Provides
    fun getMonthDays() = GetMonthDays()

    @Provides
    fun getEventsForMonth(repository: EventsRepository) = GetEventsForMonth(repository)

    @Provides
    @Singleton
    fun eventsRepository(
        events: Events,
        mapper: EventEntityMapper,
    ): EventsRepository = EventsRepositoryImpl(
        events = events,
        mapper = mapper,
        dispatcher = Dispatchers.IO,
    )
}
