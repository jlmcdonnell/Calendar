package dev.mcd.calendar.feature.calendar.domain

import dev.mcd.calendar.feature.calendar.domain.entity.CalendarDate
import dev.mcd.calendar.feature.calendar.domain.entity.MonthDays
import dev.mcd.calendar.feature.calendar.domain.extension.precedingMonthDays
import dev.mcd.calendar.feature.calendar.domain.extension.succeedingMonthDays
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class GetMonthDays {

    operator fun invoke(date: LocalDate): MonthDays {
        return createCalendarDays(date)
    }

    private fun createCalendarDays(date: LocalDate): List<CalendarDate> {
        val start = date
            .with(TemporalAdjusters.firstDayOfMonth())
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        val days = buildList {
            add(start)

            1.until(42).onEach {
                add(start.plusDays(it.toLong()))
            }
        }

        val precedingDays = days.precedingMonthDays()
        val succeedingDays = days.succeedingMonthDays()

        return days.map {
            CalendarDate(
                date = it,
                isPrecedingMonth = it in precedingDays,
                isSucceedingMonth = it in succeedingDays,
            )
        }
    }
}
