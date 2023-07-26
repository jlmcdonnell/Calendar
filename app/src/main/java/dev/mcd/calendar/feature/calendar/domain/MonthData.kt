package dev.mcd.calendar.feature.calendar.domain

import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.lastDayOfMonth

/**
 * Encompasses the days of the given month and extends to include
 * the preceding and succeeding days to ensure complete weeks
 * at both the start and end of the month.
 *
 * [days] - Contains 42 days
 */
data class MonthData(
    val days: List<CalendarDate>,
)

data class CalendarDate(
    val date: LocalDate,
    val isPrecedingMonth: Boolean,
    val isSucceedingMonth: Boolean,
)

val CalendarDate.isInMonth get() = !isPrecedingMonth && !isSucceedingMonth

fun List<LocalDate>.precedingMonthDays(): List<LocalDate> {
    return if (first().dayOfMonth == 1) {
        emptyList()
    } else {
        val previousMonth = first().monthValue
        takeWhile { it.monthValue == previousMonth }
    }
}

fun List<LocalDate>.succeedingMonthDays(): List<LocalDate> {
    val lastDay = last()
    return if (lastDay == lastDay.with(lastDayOfMonth())) {
        emptyList()
    } else {
        val nextMonth = lastDay.monthValue
        dropWhile { it.monthValue != nextMonth }
    }
}
