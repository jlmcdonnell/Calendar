package dev.mcd.calendar.feature.calendar.domain.entity

import java.time.LocalDate
import java.time.ZonedDateTime

data class Event(
    val id: Long,
    val title: String,
    val description: String,
    val date: LocalDate,
    val time: ZonedDateTime,
)
