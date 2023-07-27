package dev.mcd.calendar.ui.calendar

import dev.mcd.calendar.feature.calendar.domain.GetMonthData
import dev.mcd.calendar.test.viewmodel.testSideEffect
import dev.mcd.calendar.test.viewmodel.testState
import dev.mcd.calendar.ui.calendar.CalendarViewModel.SideEffect
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import java.time.LocalDate

class CalendarViewModelTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    Given("The ViewModel is initialized") {
        val testDate = LocalDate.of(2023, 7, 25)
        val viewModel = CalendarViewModel(
            dateProvider = { testDate },
            getMonthData = GetMonthData(),
        )
        with(viewModel) {
            Then("Emit default state on initialization") {
                testState(consumeInitialState = false) {
                    awaitItem() shouldBe CalendarViewModel.State()
                    cancelAndConsumeRemainingEvents()
                }
            }

            Then("Emit the current date") {
                testState {
                    awaitItem().date shouldBe testDate
                }
            }

            Then("MonthData is generated") {
                testState {
                    awaitItem().data!!.shouldNotBeNull()
                }
            }

            When("Next month is pressed") {
                testState {
                    val currentState = awaitItem()
                    viewModel.onNextMonth()

                    Then("The next month should be set") {
                        awaitItem().date shouldBe currentState.date?.plusMonths(1)
                    }

                    Then("The data should be updated") {
                        val state = awaitItem()
                        state.data shouldNotBe currentState.data
                    }
                }
            }
            When("Previous month is pressed") {
                testState {
                    val currentState = awaitItem()
                    viewModel.onPreviousMonth()

                    Then("The previous month should be set") {
                        awaitItem().date shouldBe currentState.date?.minusMonths(1)
                    }

                    Then("The data should be updated") {
                        val state = awaitItem()
                        state.data shouldNotBe currentState.data
                    }
                }
            }

            When("The date is changed") {
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
                            state.data shouldNotBe currentState.data
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
                            state.data shouldBeSameInstanceAs currentState.data
                        }
                    }
                }
            }

            When("A day is clicked") {
                testSideEffect {
                    viewModel.onDateClicked(testDate)

                    And("There are no events") {
                        Then("Navigate to Create Event with given date") {
                            awaitItem() shouldBe SideEffect.NavigateCreateEvent(testDate)
                        }
                    }
                }
            }
        }
    }
})
