package dev.mcd.calendar.feature.calendar.domain.entity

/**
 * Encompasses the days of the given month and extends to include
 * the preceding and succeeding days to ensure complete weeks
 * at both the start and end of the month.
 */
typealias MonthDays = List<CalendarDate>
