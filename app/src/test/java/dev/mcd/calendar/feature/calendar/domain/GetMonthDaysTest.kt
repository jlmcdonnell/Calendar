package dev.mcd.calendar.feature.calendar.domain

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeStrictlyIncreasing
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class GetMonthDaysTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val getMonthDays = GetMonthDays()

    Given("A test date") {
        val date = LocalDate.of(2023, 7, 26)

        When("invoked") {
            val result = getMonthDays(date)

            Then("Month data should contain 42 days") {
                result.size shouldBe 42
            }

            Then("The first day should be Monday") {
                result.first().date.dayOfWeek shouldBe DayOfWeek.MONDAY
            }

            Then("The last day should be sunday") {
                result.last().date.dayOfWeek shouldBe DayOfWeek.SUNDAY
            }

            Then("All dates in month should be increasing within range") {
                val days = result.map { it.date }

                val start = date
                    .with(TemporalAdjusters.firstDayOfMonth())
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

                val end = date
                    .with(TemporalAdjusters.firstDayOfNextMonth())
                    .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

                days.shouldNotBeEmpty()

                days.all {
                    it in start..end
                }.shouldBeTrue()

                days.shouldBeStrictlyIncreasing()
            }
        }
    }
})
