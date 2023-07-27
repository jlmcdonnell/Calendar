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
class GetEventCountsForMonthTest {

    private val testScope = TestScope()

    @get:Rule
    val databaseRule = calendarDatabaseRule()

    private lateinit var repository: EventsRepository
    private lateinit var getEventCountsForMonth: GetEventCountsForMonth
    private lateinit var getMonthDays: GetMonthDays

    @Before
    fun setUp() {
        getMonthDays = GetMonthDays()

        repository = EventsRepositoryImpl(
            events = databaseRule.database.events(),
            mapper = EventEntityMapper(),
            dispatcher = testScope.coroutineContext[CoroutineDispatcher.Key]!!,
        )
        getEventCountsForMonth = GetEventCountsForMonth(repository)
    }

    @Test
    fun `Result contains all dates from MonthData`() = testScope.runTest {
        val monthData = getMonthDays(LocalDate.now())
        val eventsMap = getEventCountsForMonth(monthData)

        monthData.onEach { calendarDate ->
            eventsMap shouldHaveKey calendarDate.date
        }
    }

    @Test
    fun `Result contains 1 event for date`() = testScope.runTest {
        // Given
        val date = LocalDate.now()

        repository.addEvent(
            title = "title",
            description = "description",
            date = date,
            time = ZonedDateTime.now(),
        )
        val monthData = getMonthDays(date)

        // When
        val result = getEventCountsForMonth(monthData)

        // Then
        result.getValue(date).events shouldBe 1
        result.getValue(date).date shouldBe date
    }
}
