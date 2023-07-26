package dev.mcd.calendar.feature.calendar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDate

@Dao
interface Events {
    @Insert
    suspend fun addEvent(event: EventEntity)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("SELECT * FROM events WHERE date = :date")
    suspend fun findByDate(date: LocalDate): List<EventEntity>

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEvent(id: Int)
}
