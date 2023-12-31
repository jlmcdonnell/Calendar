package dev.mcd.calendar.feature.calendar.domain.entity

import java.time.LocalDate

data class CalendarDate(
    val date: LocalDate,
    val isPrecedingMonth: Boolean,
    val isSucceedingMonth: Boolean,
)

val CalendarDate.isInMonth get() = !isPrecedingMonth && !isSucceedingMonth
