package dev.mcd.calendar.feature.calendar.domain

import dev.mcd.calendar.feature.calendar.data.EventsRepositoryImpl
import dev.mcd.calendar.feature.calendar.data.mapper.EventEntityMapper
import dev.mcd.calendar.test.feature.calendar.data.database.calendarDatabaseRule
import io.kotest.matchers.maps.shouldHaveKey
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDate
import java.time.ZonedDateTime

@RunWith(RobolectricTestRunner::class)
class GetEventsForMonthTest {

    private val testScope = TestScope()

    @get:Rule
    val databaseRule = calendarDatabaseRule()

    private lateinit var repository: EventsRepository
    private lateinit var getEventsForMonth: GetEventsForMonth
    private lateinit var getMonthData: GetMonthData

    @Before
    fun setUp() {
        getMonthData = GetMonthData()

        repository = EventsRepositoryImpl(
            events = databaseRule.database.events(),
            mapper = EventEntityMapper(),
            dispatcher = testScope.coroutineContext[CoroutineDispatcher.Key]!!,
        )
        getEventsForMonth = GetEventsForMonth(repository)
    }

    @Test
    fun `Result contains all dates from MonthData`() = testScope.runTest {
        val monthData = getMonthData(LocalDate.now())
        val eventsMap = getEventsForMonth(monthData)

        monthData.days.onEach { calendarDate ->
            eventsMap shouldHaveKey calendarDate.date
        }
    }

    @Test
    fun `Result contains event for date`() = testScope.runTest {
        // Given
        val date = LocalDate.now()
        val event = repository.addEvent(
            title = "title",
            description = "description",
            date = date,
            time = ZonedDateTime.now(),
        )
        val monthData = getMonthData(date)

        // When
        val result = getEventsForMonth(monthData)

        // Then
        result.getValue(date).events[0] shouldBe event
        result.getValue(date).date shouldBe date
    }
}
