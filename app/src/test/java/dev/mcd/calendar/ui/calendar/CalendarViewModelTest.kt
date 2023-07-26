package dev.mcd.calendar.ui.calendar

import app.cash.turbine.test
import dev.mcd.calendar.feature.calendar.domain.GetMonthData
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import java.time.LocalDate

class CalendarViewModelTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val testDate = LocalDate.of(2023, 7, 25)
    val viewModel = CalendarViewModel(
        dateProvider = { testDate },
        getMonthData = GetMonthData(),
    )

    Given("The ViewModel is initialized") {
        viewModel.container.stateFlow.test {
            And("The container is created") {
                val initialState = awaitItem()

                Then("Emit default state on initialization") {
                    initialState shouldBe CalendarViewModel.State()
                    cancelAndConsumeRemainingEvents()
                }

                Then("Emit the current date") {
                    awaitItem().date shouldBe testDate
                }

                Then("MonthData is generated") {
                    awaitItem().data!!.shouldNotBeNull()
                }

                When("Next month is pressed") {
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
                When("Previous month is pressed") {
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
                When("The date is changed to a new date") {
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
        }
    }
})
