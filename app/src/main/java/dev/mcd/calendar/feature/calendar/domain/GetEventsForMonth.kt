package dev.mcd.calendar.feature.calendar.domain

import dev.mcd.calendar.feature.calendar.domain.entity.DateEvents
import dev.mcd.calendar.feature.calendar.domain.entity.MonthData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate

class GetEventsForMonth(
    private val repository: EventsRepository,
) {
    suspend operator fun invoke(month: MonthData): Map<LocalDate, DateEvents> {
        return coroutineScope {
            val eventsDeferred = month.days.map { date ->
                async {
                    DateEvents(
                        date = date.date,
                        events = repository.findByDate(date.date),
                    )
                }
            }

            awaitAll(*eventsDeferred.toTypedArray())
                .associateBy { it.date }
        }
    }
}
