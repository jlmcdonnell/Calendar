package dev.mcd.calendar.feature.calendar.data

import dev.mcd.calendar.feature.calendar.data.entity.EventEntity
import dev.mcd.calendar.feature.calendar.data.mapper.EventEntityMapper
import dev.mcd.calendar.feature.calendar.domain.EventsRepository
import dev.mcd.calendar.feature.calendar.domain.entity.Event
import java.time.LocalDate
import java.time.ZonedDateTime

class EventsRepositoryImpl(
    private val events: Events,
    private val mapper: EventEntityMapper,
) : EventsRepository {

    override suspend fun addEvent(
        title: String,
        description: String,
        date: LocalDate,
        time: ZonedDateTime,
    ): Event {
        val event = EventEntity(
            title = title,
            description = description,
            date = date,
            time = time,
        )
        val id = events.addEvent(event)
        return events.findById(id).let { mapper.toDomain(it) }
    }

    override suspend fun findById(id: Long): Event {
        return events.findById(id).let { mapper.toDomain(it) }
    }

    override suspend fun updateEvent(event: Event) {
        events.updateEvent(mapper.toData(event))
    }

    override suspend fun findByDate(date: LocalDate): List<Event> {
        return events.findByDate(date).map { mapper.toDomain(it) }
    }

    override suspend fun deleteEvent(id: Long) {
        events.deleteEvent(id)
    }
}
