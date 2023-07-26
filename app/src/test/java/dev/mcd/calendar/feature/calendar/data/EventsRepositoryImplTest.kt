package dev.mcd.calendar.feature.calendar.data

import androidx.room.Room
import dev.mcd.calendar.feature.calendar.data.mapper.EventEntityMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.time.LocalDate
import java.time.ZonedDateTime

@RunWith(RobolectricTestRunner::class)
class EventsRepositoryImplTest {

    private val mapper = EventEntityMapper()

    private lateinit var database: CalendarDatabase
    private lateinit var events: Events
    private lateinit var repository: EventsRepositoryImpl

    @Before
    fun setUp() {
        database = createDatabase()
        events = database.events()
        repository = EventsRepositoryImpl(events, mapper)
    }

    @Test
    fun `Insert entity`() {
        runBlocking {
            repository.addEvent(
                title = "Hello",
                description = "Description",
                date = LocalDate.now(),
                time = ZonedDateTime.now(),
            )
        }
    }

    @Test
    fun `Query entity for date`() {
        runBlocking {
            val date = LocalDate.of(2023, 7, 26)

            repository.addEvent(
                title = "Hello",
                description = "Description",
                date = date,
                time = ZonedDateTime.now(),
            )

            repository.findByDate(date).first().title shouldBe "Hello"
        }
    }

    @Test
    fun `Query entity for ID`() {
        runBlocking {
            val date = LocalDate.of(2023, 7, 26)

            val entity = repository.addEvent(
                title = "Hello",
                description = "Description",
                date = date,
                time = ZonedDateTime.now(),
            )

            repository.findById(entity.id) shouldBe entity
        }
    }

    @Test
    fun `Update entity`() {
        runBlocking {
            val date = LocalDate.of(2023, 7, 26)

            repository.addEvent(
                title = "Hello",
                description = "Description",
                date = date,
                time = ZonedDateTime.now(),
            )

            val event = repository.findByDate(date).first()
            val update = event.copy(title = "world")

            repository.updateEvent(update)
            repository.findByDate(date).first().title shouldBe "world"
        }
    }

    @Test
    fun `Delete entity by ID`() {
        runBlocking {
            val date = LocalDate.now()

            val entity = repository.addEvent(
                title = "Hello",
                description = "Description",
                date = date,
                time = ZonedDateTime.now(),
            )

            repository.deleteEvent(entity.id)
            shouldThrow<NullPointerException> { repository.findById(entity.id) }
        }
    }

    private fun createDatabase(): CalendarDatabase {
        return Room.inMemoryDatabaseBuilder(
            context = RuntimeEnvironment.getApplication(),
            klass = CalendarDatabase::class.java,
        ).build()
    }
}
