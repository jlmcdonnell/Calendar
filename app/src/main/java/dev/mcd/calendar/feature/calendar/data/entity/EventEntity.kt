package dev.mcd.calendar.feature.calendar.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.ZonedDateTime

@Entity(
    tableName = "events",
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    val title: String,
    val description: String,
    val date: LocalDate,
    val time: ZonedDateTime,
)
