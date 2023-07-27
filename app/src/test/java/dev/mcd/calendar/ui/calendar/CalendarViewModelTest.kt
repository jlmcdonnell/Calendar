package dev.mcd.calendar.ui.calendar

import dev.mcd.calendar.feature.calendar.domain.GetEventsForMonth
import dev.mcd.calendar.feature.calendar.domain.GetMonthDays
import dev.mcd.calendar.feature.calendar.domain.entity.DateEvents
import dev.mcd.calendar.feature.calendar.domain.entity.Event
import dev.mcd.calendar.test.viewmodel.testSideEffect
import dev.mcd.calendar.test.viewmodel.testState
import dev.mcd.calendar.ui.calendar.CalendarViewModel.SideEffect
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.coEvery
import io.mockk.mockk
import java.time.LocalDate
import java.time.ZonedDateTime

class CalendarViewModelTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    Given("The ViewModel is initialized") {
        val testDate = LocalDate.of(2023, 7, 25)
        val getEventsForMonth = mockk<GetEventsForMonth>()
        val viewModel = CalendarViewModel(
            dateProvider = { testDate },
            getMonthDays = GetMonthDays(),
            getEventsForMonth = getEventsForMonth,
        )

        with(viewModel) {
            Then("Emit default state on initialization") {
                testState(consumeInitialState = false) {
                    awaitItem() shouldBe CalendarViewModel.State()
                    cancelAndConsumeRemainingEvents()
                }
            }

            Then("Emit the current date") {
                getEventsForMonth.givenNoEvents()

                testState {
                    awaitItem().date shouldBe testDate
                }
            }

            Then("MonthData is generated") {
                getEventsForMonth.givenNoEvents()
                testState {
                    awaitItem().monthDays!!.shouldNotBeNull()
                }
            }

            When("Next month is pressed") {
                getEventsForMonth.givenNoEvents()
                testState {
                    val currentState = awaitItem()
                    viewModel.onNextMonth()

                    Then("The next month should be set") {
                        awaitItem().date shouldBe currentState.date?.plusMonths(1)
                    }

                    Then("The data should be updated") {
                        val state = awaitItem()
                        state.monthDays shouldNotBe currentState.monthDays
                    }
                }
            }
            When("Previous month is pressed") {
                getEventsForMonth.givenNoEvents()
                testState {
                    val currentState = awaitItem()
                    viewModel.onPreviousMonth()

                    Then("The previous month should be set") {
                        awaitItem().date shouldBe currentState.date?.minusMonths(1)
                    }

                    Then("The data should be updated") {
                        val state = awaitItem()
                        state.monthDays shouldNotBe currentState.monthDays
                    }
                }
            }

            When("The date is changed") {
                getEventsForMonth.givenNoEvents()
                testState {
                    val currentState = awaitItem()

                    And("That date is outside of the current month view") {
                        val newDate = testDate.plusMonths(1)
                        viewModel.onGoToDate(newDate)

                        Then("That date should be set") {
                            awaitItem().date shouldBe newDate
                        }

                        Then("The month data should be updated") {
                            val state = awaitItem()
                            state.monthDays shouldNotBe currentState.monthDays
                        }
                    }
                    And("That date is within the current month view") {
                        val newDate = testDate.plusDays(1)
                        viewModel.onGoToDate(newDate)

                        Then("That date should be set") {
                            awaitItem().date shouldBe newDate
                        }

                        Then("The month data should not be updated") {
                            val state = awaitItem()
                            state.monthDays shouldBeSameInstanceAs currentState.monthDays
                        }
                    }
                }
            }

            When("A day is clicked") {
                getEventsForMonth.givenNoEvents()
                testSideEffect {
                    viewModel.onDateClicked(testDate)

                    And("There are no events") {
                        Then("Navigate to Create Event with given date") {
                            awaitItem() shouldBe SideEffect.NavigateCreateEvent(testDate)
                        }
                    }
                }
            }

            // Given the ViewModel is initialized
            And("There events for the date are present") {
                val events = listOf(
                    Event(
                        id = 0,
                        title = "title",
                        description = "description",
                        date = testDate,
                        time = ZonedDateTime.now(),
                    ),
                )

                val expectedEventsState = mapOf(testDate to DateEvents(testDate, events))
                getEventsForMonth.givenEvents(events = expectedEventsState)

                Then("Emit the events") {
                    testState {
                        awaitItem().events shouldBe expectedEventsState
                    }
                }
            }
        }
    }
})

private suspend fun GetEventsForMonth.givenNoEvents() {
    coEvery { this@givenNoEvents.invoke(any()) } returns emptyMap()
}

private suspend fun GetEventsForMonth.givenEvents(
    events: Map<LocalDate, DateEvents> = emptyMap(),
) {
    coEvery { this@givenEvents.invoke(any()) } returns events
}
