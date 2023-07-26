package dev.mcd.calendar.feature.calendar.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class MonthDataTest : StringSpec({
    "First day is the first day of the month" {
        val dates = buildList {
            add(LocalDate.of(2023, 5, 1))
            add(LocalDate.of(2023, 5, 15))
            add(LocalDate.of(2023, 6, 11))
        }

        dates.precedingMonthDays() shouldBe emptyList()
    }

    "First day is not the first day of the month" {
        val dates = buildList {
            add(LocalDate.of(2023, 6, 26))
            add(LocalDate.of(2023, 7, 25))
        }

        dates.precedingMonthDays() shouldBe listOf(LocalDate.of(2023, 6, 26))
    }

    "Last day is the last day of the month" {
        val dates = buildList {
            add(LocalDate.of(2023, 7, 26))
            add(LocalDate.of(2023, 7, 31))
        }

        dates.succeedingMonthDays() shouldBe emptyList()
    }

    "Last day is not the last day of the month" {
        val dates = buildList {
            add(LocalDate.of(2023, 7, 26))
            add(LocalDate.of(2023, 8, 5))
            add(LocalDate.of(2023, 8, 6))
        }

        dates.succeedingMonthDays() shouldBe listOf(
            LocalDate.of(2023, 8, 5),
            LocalDate.of(2023, 8, 6),
        )
    }
})
