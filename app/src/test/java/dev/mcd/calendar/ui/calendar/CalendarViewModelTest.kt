package dev.mcd.calendar.ui.calendar

import dev.mcd.calendar.feature.calendar.domain.GetEventsForMonth
import dev.mcd.calendar.feature.calendar.domain.GetMonthDays
import dev.mcd.calendar.feature.calendar.domain.entity.DateEvents
import dev.mcd.calendar.ui.calendar.CalendarViewModel.SideEffect.NavigateCreateEvent
import dev.mcd.calendar.ui.calendar.CalendarViewModel.SideEffect.NavigateToDay
import dev.mcd.calendar.ui.calendar.CalendarViewModel.State
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test
import java.time.LocalDate

class CalendarViewModelTest {

    private val defaultTestDate = LocalDate.of(2023, 7, 25)

    @Test
    fun `When initialized, Then emit current date`() = runTest {
        val viewModel = givenViewModel()
        viewModel.test(this, State()) {
            runOnCreate()
            expectInitialState()
            awaitState().date shouldBe defaultTestDate
        }
    }

    @Test
    fun `When initialized, Then generate month days`() = runTest {
        val viewModel = givenViewModel()
        viewModel.test(this, State()) {
            runOnCreate()
            expectInitialState()
            awaitState().monthDays.shouldNotBeNull()
        }
    }

    @Test
    fun `When next month is clicked, Then the data should be updated`() = runTest {
        val viewModel = givenViewModel()
        viewModel.test(this, State()) {
            runOnCreate()
            expectInitialState()
            val currentState = awaitState()

            // When
            viewModel.onNextMonth()

            // Then
            awaitState().run {
                date shouldBe defaultTestDate.plusMonths(1)
                monthDays shouldNotBe currentState.monthDays
            }
        }
    }

    @Test
    fun `When previous month is clicked, Then the data should be updated`() = runTest {
        val viewModel = givenViewModel()
        viewModel.test(this, State()) {
            runOnCreate()
            expectInitialState()
            val currentState = awaitState()

            // When
            viewModel.onPreviousMonth()

            // Then
            awaitState().run {
                date shouldBe defaultTestDate.minusMonths(1)
                monthDays shouldNotBe currentState.monthDays
            }
        }
    }

    @Test
    fun `When the date is changed within the month, Then only update the date`() = runTest {
        val viewModel = givenViewModel()
        viewModel.test(this, State()) {
            runOnCreate()
            expectInitialState()
            val currentState = awaitState()

            // When
            viewModel.onGoToDate(defaultTestDate.plusDays(1))

            // Then
            awaitState().run {
                date shouldBe defaultTestDate.plusDays(1)
                monthDays shouldBe currentState.monthDays
            }
        }
    }

    @Test
    fun `When a date with no Events is clicked, Then Navigate to Create Event`() = runTest {
        val viewModel = givenViewModel()
        viewModel.test(this) {
            expectInitialState()
            runOnCreate()

            // When
            viewModel.onDateClicked(defaultTestDate)

            // Then
            awaitSideEffect() shouldBe NavigateCreateEvent(defaultTestDate)
            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun `When a date with Events is clicked, Then Navigate to Day`() = runTest {
        val events = mapOf(defaultTestDate to DateEvents(defaultTestDate, listOf(mockk())))

        val viewModel = givenViewModel(
            getEventsForMonth = givenEvents(events),
        )

        viewModel.test(this) {
            expectInitialState()
            runOnCreate()
            awaitState()

            // When
            viewModel.onDateClicked(defaultTestDate)

            // Then
            awaitSideEffect() shouldBe NavigateToDay(defaultTestDate)
            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun `When events for date are present, Then emit events`() = runTest {
        val events = mapOf(defaultTestDate to DateEvents(defaultTestDate, listOf(mockk())))
        val viewModel = givenViewModel(
            getEventsForMonth = givenEvents(events),
        )

        viewModel.test(this) {
            expectInitialState()

            // When
            runOnCreate()

            // Then
            awaitState().events shouldBe events
        }
    }

    private fun givenViewModel(
        dateProvider: () -> LocalDate = { defaultTestDate },
        getMonthDays: GetMonthDays = GetMonthDays(),
        getEventsForMonth: GetEventsForMonth = givenNoEvents(),
    ): CalendarViewModel {
        return CalendarViewModel(
            dateProvider = dateProvider,
            getMonthDays = getMonthDays,
            getEventsForMonth = getEventsForMonth,
        )
    }

    private fun givenNoEvents(): GetEventsForMonth {
        return mockk {
            coEvery { this@mockk.invoke(any()) } returns emptyMap()
        }
    }

    private fun givenEvents(
        events: Map<LocalDate, DateEvents> = emptyMap(),
    ): GetEventsForMonth {
        return mockk {
            coEvery { this@mockk.invoke(any()) } returns events
        }
    }
}
