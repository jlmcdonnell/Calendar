package dev.mcd.calendar.ui.calendar.month

import dev.mcd.calendar.feature.calendar.domain.GetEventCountsForDates
import dev.mcd.calendar.feature.calendar.domain.GetMonthDays
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test
import java.time.LocalDate

class CalendarMonthViewModelTest {

    private val defaultTestDate = LocalDate.of(2023, 7, 25)

    @Test
    fun `When initialized, Then emit current date`() = runTest {
        val viewModel = givenViewModel()
        viewModel.test(this, givenTestState(date = defaultTestDate)) {
            awaitState().currentDate shouldBe defaultTestDate
            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun `When initialized, Then generate month days`() = runTest {
        val viewModel = givenViewModel()
        viewModel.test(this, givenTestState()) {
            runOnCreate()
            expectInitialState()
            awaitState().monthDays.shouldNotBeNull()
        }
    }

    @Test
    fun `When next month is clicked, Then the data should be updated`() = runTest {
        val viewModel = givenViewModel()
        viewModel.test(this, givenTestState()) {
            runOnCreate()
            expectInitialState()
            val currentState = awaitState()

            // When
            viewModel.onNextMonth()

            // Then
            awaitState().run {
                monthDays shouldNotBe currentState.monthDays
            }
        }
    }

    @Test
    fun `When previous month is clicked, Then the data should be updated`() = runTest {
        val viewModel = givenViewModel()
        viewModel.test(this, givenTestState()) {
            val currentState = awaitState()

            // When
            viewModel.onPreviousMonth()

            // Then
            awaitState().run {
                monthDays shouldNotBe currentState.monthDays
            }
        }
    }

    @Test
    fun `When calendar month is reset, Then reset calendar date to current date`() = runTest {
        val date = LocalDate.now()
        val viewModel = givenViewModel()
        viewModel.test(this, givenTestState()) {
            expectInitialState()

            // When
            viewModel.onNextMonth()
            awaitState()

            viewModel.onResetCalendarDate()

            // Then
            awaitState().run {
                calendarDate shouldBe date
            }
        }
    }

    @Test
    fun `When a date with no Events is clicked, Then Navigate to Create Event`() = runTest {
        val viewModel = givenViewModel()
        viewModel.test(this) {
            expectInitialState()

            // When
            viewModel.onDateClicked(defaultTestDate)

            // Then
            awaitSideEffect() shouldBe CalendarMonthViewModel.SideEffect.NavigateCreateEvent(defaultTestDate)
            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun `When a date with Events is clicked, Then Navigate to Day`() = runTest {
        val events = mapOf(defaultTestDate to 1)

        val viewModel = givenViewModel(
            getEventCountsForDates = givenEvents(events),
        )

        viewModel.test(this, initialState = givenTestState(date = defaultTestDate)) {
            expectInitialState()
            runOnCreate()
            awaitState().events shouldBe events

            // When
            viewModel.onDateClicked(defaultTestDate)

            // Then
            awaitSideEffect() shouldBe CalendarMonthViewModel.SideEffect.NavigateToDay(defaultTestDate)
            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun `When events for date are present, Then emit events`() = runTest {
        val events = mapOf(defaultTestDate to 1)
        val viewModel = givenViewModel(
            getEventCountsForDates = givenEvents(events),
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
        getEventCountsForDates: GetEventCountsForDates = givenNoEvents(),
    ): CalendarMonthViewModel {
        return CalendarMonthViewModel(
            dateProvider = dateProvider,
            getMonthDays = getMonthDays,
            getEventCountsForDates = getEventCountsForDates,
        )
    }

    private fun givenNoEvents(): GetEventCountsForDates {
        return mockk {
            coEvery { this@mockk.invoke(any()) } returns emptyMap()
        }
    }

    private fun givenEvents(
        events: Map<LocalDate, Int> = emptyMap(),
    ): GetEventCountsForDates {
        return mockk {
            coEvery { this@mockk.invoke(any()) } returns events
        }
    }

    private fun givenTestState(date: LocalDate = LocalDate.now()) = CalendarMonthViewModel.State(
        currentDate = date,
    )
}
