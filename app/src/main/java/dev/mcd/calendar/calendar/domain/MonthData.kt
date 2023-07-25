package dev.mcd.calendar.calendar.domain

import java.time.LocalDate

/**
 * Encompasses the days of the given month and extends to include
 * the preceding and succeeding days to ensure complete weeks
 * at both the start and end of the month.
 *
 * [days] - Contains 42 days
 */
data class MonthData(
    val days: List<LocalDate>,
)
