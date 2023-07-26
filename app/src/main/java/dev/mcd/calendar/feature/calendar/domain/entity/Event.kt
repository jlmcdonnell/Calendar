package dev.mcd.calendar.feature.calendar.domain.entity

import java.time.ZonedDateTime

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: ZonedDateTime,
)
