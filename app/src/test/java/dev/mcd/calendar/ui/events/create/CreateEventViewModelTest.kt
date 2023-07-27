package dev.mcd.calendar.ui.events.create

import androidx.lifecycle.SavedStateHandle
import dev.mcd.calendar.feature.calendar.domain.EventsRepository
import dev.mcd.calendar.ui.routing.navArg
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class CreateEventViewModelTest {

    @Test
    fun `When date is provided by SavedStateHandle, Emit date`() = runTest {
        val date = LocalDate.now()
        val vm = givenViewModel(
            savedStateHandle = SavedStateHandle(
                initialState = mapOf("date" to date.navArg()),
            ),
        )
        vm.test(this) {
            expectInitialState()
            runOnCreate()
            awaitState().date shouldBe date
        }
    }

    @Test
    fun `When no date is provided by SavedStateHandle, Emit null date`() = runTest {
        val vm = givenViewModel()
        vm.test(this) {
            expectInitialState()
            runOnCreate()
            // No more events
        }
    }

    @Test
    fun `When the date is updated, Emit state with new date`() = runTest {
        val date = LocalDate.now()
        val vm = givenViewModel()
        vm.test(this) {
            expectInitialState()
            runOnCreate()

            vm.onUpdateDate(date)
            awaitState().date shouldBe date
        }
    }

    @Test
    fun `When the time is updated, Emit state with new time`() = runTest {
        val time = LocalTime.now()
        val vm = givenViewModel()
        vm.test(this) {
            expectInitialState()
            runOnCreate()

            vm.onUpdateTime(time)
            awaitState().time shouldBe time
        }
    }

    @Test
    fun `When add event is clicked, Add event to repository`() = runTest {
        val date = LocalDate.now()
        val repository = mockk<EventsRepository> {
            coEvery { addEvent(any(), any(), any(), any()) } returns mockk()
        }
        val vm = givenViewModel(eventsRepository = repository)

        vm.test(this) {
            expectInitialState()
            runOnCreate().join()

            vm.onUpdateDate(date)
            awaitState()

            vm.onAddEvent(
                title = "title",
                description = "description",
            )
        }

        coVerify {
            repository.addEvent(
                title = "title",
                description = "description",
                date = date,
                time = date.atStartOfDay(ZoneId.systemDefault()),
            )
        }
    }

    private fun givenViewModel(
        savedStateHandle: SavedStateHandle = SavedStateHandle(),
        eventsRepository: EventsRepository = mockk(),
    ) = CreateEventViewModel(
        eventsRepository = eventsRepository,
        savedStateHandle = savedStateHandle,
    )
}
