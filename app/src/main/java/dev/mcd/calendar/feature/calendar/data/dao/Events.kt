package dev.mcd.calendar.feature.calendar.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.mcd.calendar.feature.calendar.data.entity.EventEntity
import java.time.LocalDate

@Dao
interface Events {
    @Insert
    suspend fun addEvent(event: EventEntity): Long

    @Query("SELECT * FROM events WHERE date = :date")
    suspend fun findByDate(date: LocalDate): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun findById(id: Long): EventEntity

    @Query("SELECT count() FROM events WHERE date = :date")
    suspend fun eventCount(date: LocalDate): Int

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEvent(id: Long)
}
