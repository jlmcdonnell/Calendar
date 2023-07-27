package dev.mcd.calendar.feature.calendar.domain.entity

import java.time.LocalDate

data class DateEvents(
    val date: LocalDate,
    val events: List<Event>,
)
