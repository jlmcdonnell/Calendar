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
import java.time.ZonedDateTime

@RunWith(RobolectricTestRunner::class)
class EventsTest {

    @get:Rule
    val database = calendarDatabaseRule()
    private val events by database

    @Test
    fun `Insert entity`() {
        runBlocking {
            val entity = EventEntity(
                id = 0,
                title = "Hello",
                description = "Description",
                date = LocalDate.now(),
                time = ZonedDateTime.now(),
            )
            events.addEvent(entity)
        }
    }

    @Test
    fun `Query entity for date`() {
        runBlocking {
            val date = LocalDate.of(2023, 7, 26)
            val entity = EventEntity(
                id = 0,
                title = "Hello",
                description = "Description",
                date = date,
                time = ZonedDateTime.now(),
            )
            events.addEvent(entity)
            events.findByDate(date) shouldBe listOf(entity)
        }
    }

    @Test
    fun `Query entity for ID`() {
        runBlocking {
            val date = LocalDate.of(2023, 7, 26)
            val entity = EventEntity(
                id = 0,
                title = "Hello",
                description = "Description",
                date = date,
                time = ZonedDateTime.now(),
            )
            val id = events.addEvent(entity)
            events.findById(id) shouldBe entity
        }
    }

    @Test
    fun `Query event count for date`() {
        runBlocking {
            val date = LocalDate.of(2023, 7, 26)
            val entity = EventEntity(
                id = 0,
                title = "Hello",
                description = "Description",
                date = date,
                time = ZonedDateTime.now(),
            )
            events.addEvent(entity)
            events.eventCount(date) shouldBe 1
        }
    }

    @Test
    fun `Update entity`() {
        runBlocking {
            val date = LocalDate.of(2023, 7, 26)
            val entity = EventEntity(
                id = 0,
                title = "Hello",
                description = "Description",
                date = date,
                time = ZonedDateTime.now(),
            )
            val update = entity.copy(title = "world")
            events.addEvent(entity)
            events.updateEvent(update)
            events.findByDate(date) shouldBe listOf(update)
        }
    }

    @Test
    fun `Delete entity by ID`() {
        runBlocking {
            val date = LocalDate.now()
            val entity = EventEntity(
                id = 0,
                title = "Hello",
                description = "Description",
                date = date,
                time = ZonedDateTime.now(),
            )
            val id = events.addEvent(entity)
            events.deleteEvent(id)
            events.findByDate(date) shouldBe emptyList()
        }
    }
}
