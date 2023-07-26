package dev.mcd.calendar.feature.calendar.data

import androidx.room.Room
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.time.LocalDate
import java.time.ZonedDateTime

@RunWith(RobolectricTestRunner::class)
class EventsTest {

    private lateinit var database: CalendarDatabase
    private lateinit var events: Events

    @Before
    fun setUp() {
        database = createDatabase()
        events = database.events()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `Insert entity`() = runBlocking {
        val entity = EventEntity(
            id = 0,
            title = "Hello",
            description = "Description",
            date = LocalDate.now(),
            time = ZonedDateTime.now(),
        )
        events.addEvent(entity)
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
            events.addEvent(entity)
            events.deleteEvent(entity.id)
            events.findByDate(date) shouldBe emptyList()
        }
    }

    private fun createDatabase(): CalendarDatabase {
        return Room.inMemoryDatabaseBuilder(
            context = RuntimeEnvironment.getApplication(),
            klass = CalendarDatabase::class.java,
        ).build()
    }
}
