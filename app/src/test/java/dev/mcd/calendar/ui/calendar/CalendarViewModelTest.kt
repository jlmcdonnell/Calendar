package dev.mcd.calendar.ui.calendar

import app.cash.turbine.test
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeStrictlyIncreasing
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.firstDayOfMonth
import java.time.temporal.TemporalAdjusters.firstDayOfNextMonth
import java.time.temporal.TemporalAdjusters.nextOrSame
import java.time.temporal.TemporalAdjusters.previousOrSame

class CalendarViewModelTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val testDate = LocalDate.of(2023, 7, 25)
    val viewModel = CalendarViewModel(
        dateProvider = { testDate },
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

                Then("Month data should contain 42 days") {
                    awaitItem().data!!.days.size shouldBe 42
                }

                Then("The first day should be Monday") {
                    awaitItem().data!!.days.first().date.dayOfWeek shouldBe DayOfWeek.MONDAY
                }

                Then("The last day should be sunday") {
                    awaitItem().data!!.days.last().date.dayOfWeek shouldBe DayOfWeek.SUNDAY
                }

                Then("All dates in month should be increasing within range") {
                    val days = awaitItem().data!!.days.map { it.date }

                    val start = testDate
                        .with(firstDayOfMonth())
                        .with(previousOrSame(DayOfWeek.MONDAY))

                    val end = testDate
                        .with(firstDayOfNextMonth())
                        .with(nextOrSame(DayOfWeek.SUNDAY))

                    days.all {
                        it in start..end
                    }.shouldBeTrue()

                    days.shouldBeStrictlyIncreasing()
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
