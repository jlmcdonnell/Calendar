package dev.mcd.calendar.feature.calendar.domain.entity

import java.time.LocalDate

data class DateEventCount(
    val date: LocalDate,
    val events: Int,
)
