package dev.mcd.calendar.feature.common.room.converter

import androidx.room.TypeConverter
import java.time.ZonedDateTime

object ZonedDateTimeConverter {
    @TypeConverter
    fun toDate(dateString: String?): ZonedDateTime? {
        return if (dateString == null) {
            null
        } else {
            ZonedDateTime.parse(dateString)
        }
    }

    @TypeConverter
    fun toDateString(date: ZonedDateTime?): String? {
        return date?.toString()
    }
}
