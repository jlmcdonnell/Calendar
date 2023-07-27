package dev.mcd.calendar.feature.calendar.domain

import dev.mcd.calendar.feature.calendar.domain.entity.DateEventCount
import dev.mcd.calendar.feature.calendar.domain.entity.MonthDays
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate

class GetEventCountsForMonth(
    private val repository: EventsRepository,
) {
    suspend operator fun invoke(month: MonthDays): Map<LocalDate, DateEventCount> {
        return coroutineScope {
            val eventsDeferred = month.map { date ->
                async {
                    DateEventCount(
                        date = date.date,
                        events = repository.eventCount(date.date),
                    )
                }
            }

            awaitAll(*eventsDeferred.toTypedArray())
                .associateBy { it.date }
        }
    }
}
