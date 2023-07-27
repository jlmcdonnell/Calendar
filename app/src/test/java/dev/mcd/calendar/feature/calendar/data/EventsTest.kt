package dev.mcd.calendar.feature.calendar.data

import dev.mcd.calendar.feature.calendar.data.entity.EventEntity
import dev.mcd.calendar.test.feature.calendar.data.database.calendarDatabaseRule
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDate
import java.time.LocalTime

@RunWith(RobolectricTestRunner::class)
class EventsTest {

    @get:Rule
    val database = calendarDatabaseRule()
    private val events by database

    @Test
    fun `Insert event`() {
        runBlocking {
            val event = EventEntity(
                id = 0,
                title = "Hello",
                description = "Description",
                date = LocalDate.now(),
                time = LocalTime.now(),
            )
            events.addEvent(event)
        }
    }

    @Test
    fun `Query event for date`() {
        runBlocking {
            val date = LocalDate.of(2023, 7, 26)
            val event = EventEntity(
                id = 0,
                title = "Hello",
                description = "Description",
                date = date,
                time = LocalTime.now(),
            )
            events.addEvent(event)
            events.findByDate(date) shouldBe listOf(event)
        }
    }

    @Test
    fun `Query entity for ID`() {
        runBlocking {
            val date = LocalDate.of(2023, 7, 26)
            val event = EventEntity(
                id = 0,
                title = "Hello",
                description = "Description",
                date = date,
                time = LocalTime.now(),
            )
            val id = events.addEvent(event)
            events.findById(id) shouldBe event
        }
    }

    @Test
    fun `Query event count for date`() {
        runBlocking {
            val date = LocalDate.of(2023, 7, 26)
            val event = EventEntity(
                id = 0,
                title = "Hello",
                description = "Description",
                date = date,
                time = LocalTime.now(),
            )
            events.addEvent(event)
            events.eventCount(date) shouldBe 1
        }
    }

    @Test
    fun `Update entity`() {
        runBlocking {
            val date = LocalDate.of(2023, 7, 26)
            val event = EventEntity(
                id = 0,
                title = "Hello",
                description = "Description",
                date = date,
                time = LocalTime.now(),
            )
            val update = event.copy(title = "world")
            events.addEvent(event)
            events.updateEvent(update)
            events.findByDate(date) shouldBe listOf(update)
        }
    }

    @Test
    fun `Delete event by ID`() {
        runBlocking {
            val date = LocalDate.now()
            val event = EventEntity(
                id = 0,
                title = "Hello",
                description = "Description",
                date = date,
                time = LocalTime.now(),
            )
            val id = events.addEvent(event)
            events.deleteEvent(id)
            events.findByDate(date) shouldBe emptyList()
        }
    }
}
