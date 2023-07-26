package dev.mcd.calendar.feature.calendar

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mcd.calendar.feature.calendar.data.Events
import dev.mcd.calendar.feature.calendar.data.EventsRepositoryImpl
import dev.mcd.calendar.feature.calendar.data.mapper.EventEntityMapper
import dev.mcd.calendar.feature.calendar.domain.EventsRepository
import dev.mcd.calendar.feature.calendar.domain.GetMonthData
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CalendarModule {

    @Provides
    fun getMonthData() = GetMonthData()

    @Provides
    @Singleton
    fun eventsRepository(
        events: Events,
        mapper: EventEntityMapper,
    ): EventsRepository = EventsRepositoryImpl(
        events = events,
        mapper = mapper,
    )
}
