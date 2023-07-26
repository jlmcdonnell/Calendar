package dev.mcd.calendar.feature.calendar.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.mcd.calendar.feature.calendar.data.entity.EventEntity
import dev.mcd.calendar.feature.common.room.converter.LocalDateConverter
import dev.mcd.calendar.feature.common.room.converter.ZonedDateTimeConverter

@Database(
    entities = [
        EventEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    ZonedDateTimeConverter::class,
    LocalDateConverter::class,
)
abstract class CalendarDatabase : RoomDatabase() {
    abstract fun events(): Events
}
