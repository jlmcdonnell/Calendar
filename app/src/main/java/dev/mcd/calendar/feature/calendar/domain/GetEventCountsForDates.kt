package dev.mcd.calendar.feature.calendar.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate

class GetEventCountsForDates(
    private val repository: EventsRepository,
) {
    suspend operator fun invoke(dates: List<LocalDate>): Map<LocalDate, Int> {
        return coroutineScope {
            val eventsDeferred = dates.map { date ->
                async {
                    date to repository.eventCount(date)
                }
            }

            awaitAll(*eventsDeferred.toTypedArray())
                .filter { (_, count) -> count > 0 }
                .associateBy { (date, _) -> date }
                .mapValues { it.value.second }
        }
    }
}
