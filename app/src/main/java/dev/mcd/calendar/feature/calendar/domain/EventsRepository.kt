package dev.mcd.calendar.feature.calendar.domain

import dev.mcd.calendar.feature.calendar.domain.entity.Event
import java.time.LocalDate
import java.time.ZonedDateTime

interface EventsRepository {

    suspend fun addEvent(
        title: String,
        description: String,
        date: LocalDate,
        time: ZonedDateTime,
    ): Event

    suspend fun findById(id: Long): Event

    suspend fun updateEvent(event: Event)

    suspend fun findByDate(date: LocalDate): List<Event>

    suspend fun deleteEvent(id: Long)
}
