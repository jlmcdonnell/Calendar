package dev.mcd.calendar.ui.calendar.day

import androidx.lifecycle.SavedStateHandle
import dev.mcd.calendar.feature.calendar.domain.EventsRepository
import dev.mcd.calendar.feature.calendar.domain.entity.Event
import dev.mcd.calendar.ui.calendar.day.DayEventsViewModel.SideEffect.NavigateCreateEvent
import dev.mcd.calendar.ui.routing.navArg
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test
import java.time.LocalDate

class DayEventsViewModelTest {

    @Test
    fun `Emit date from SavedStateHandle`() = runTest {
        val date = LocalDate.now()
        val vm = givenViewModel(date = date)

        vm.test(this) {
            expectInitialState()
            runOnCreate()
            awaitState().date shouldBe date
            cancelAndIgnoreRemainingItems()
        }
    }

    @Test
    fun `Given events for date, Emit events`() = runTest {
        val date = LocalDate.now()
        val events = listOf(mockk<Event>())
        val repository = mockk<EventsRepository> {
            coEvery { findByDate(date) } returns events
        }
        val vm = givenViewModel(
            date = date,
            repository = repository,
        )
        vm.test(this) {
            expectInitialState()
            runOnCreate()

            awaitState()
            awaitState().events shouldBe events
        }
    }

    @Test
    fun `When add event is clicked, Navigate to Create Event screen for date`() = runTest {
        val date = LocalDate.now()
        val vm = givenViewModel(
            date = date,
        )
        vm.test(this) {
            expectInitialState()
            runOnCreate()
            awaitState()

            vm.onAddEvent()
            awaitSideEffect() shouldBe NavigateCreateEvent(date = date)
        }
    }

    @Test
    fun `When delete event is clicked, Delete the event and reload events`() = runTest {
        val date = LocalDate.now()
        val event = mockk<Event> {
            every { this@mockk.id } returns 0
        }
        val repository = mockk<EventsRepository> {
            coEvery { deleteEvent(id = 0) } just Runs
            coEvery { findByDate(date) } returns emptyList()
        }
        val vm = givenViewModel(
            date = date,
            repository = repository,
        )

        vm.test(this) {
            expectInitialState()
            runOnCreate()
            awaitState()

            vm.onDeleteEvent(event)
        }

        coVerify { repository.deleteEvent(id = 0) }
        coVerify(exactly = 2) { repository.findByDate(date) }
    }

    private fun givenViewModel(
        date: LocalDate = LocalDate.now(),
        repository: EventsRepository = mockk {
            coEvery { findByDate(any()) } returns emptyList()
        },
    ): DayEventsViewModel {
        return DayEventsViewModel(
            savedStateHandle = SavedStateHandle(initialState = mapOf("date" to date.navArg())),
            repository = repository,
        )
    }
}
