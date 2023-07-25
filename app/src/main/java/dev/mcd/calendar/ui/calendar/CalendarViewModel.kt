package dev.mcd.calendar.ui.calendar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mcd.calendar.calendar.domain.CalendarDate
import dev.mcd.calendar.calendar.domain.MonthData
import dev.mcd.calendar.calendar.domain.precedingMonthDays
import dev.mcd.calendar.calendar.domain.succeedingMonthDays
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.firstDayOfMonth
import java.time.temporal.TemporalAdjusters.previousOrSame
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val dateProvider: () -> LocalDate,
) : ViewModel(), ContainerHost<CalendarViewModel.State, Unit> {

    override val container = container<State, Unit>(
        initialState = State(),
        onCreate = {
            intent {
                val date = dateProvider()
                updateDate(date)
            }
        },
    )

    fun onNextMonth() {
        intent {
            val date = state.date ?: return@intent
            updateDate(date.plusMonths(1))
        }
    }

    fun onPreviousMonth() {
        intent {
            val date = state.date ?: return@intent
            updateDate(date.minusMonths(1))
        }
    }

    fun onGoToDate(date: LocalDate) {
        intent {
            updateDate(date)
        }
    }

    context(SimpleSyntax<State, Unit>)
    private suspend fun updateDate(date: LocalDate) {
        val newMonthData = if (state.data?.days?.any { it.date == date } != true) {
            getMonthData(date)
        } else {
            null
        }

        reduce {
            state.copy(
                date = date,
                data = newMonthData ?: state.data,
            )
        }
    }

    private fun getMonthData(date: LocalDate): MonthData {
        val days = createCalendarDays(date)
        return MonthData(days = days)
    }

    private fun createCalendarDays(date: LocalDate): List<CalendarDate> {
        val start = date
            .with(firstDayOfMonth())
            .with(previousOrSame(DayOfWeek.MONDAY))

        val days = buildList {
            add(start)

            1.until(42).onEach {
                add(start.plusDays(it.toLong()))
            }
        }

        val precedingDays = days.precedingMonthDays()
        val succeedingDays = days.succeedingMonthDays()

        return days.map {
            CalendarDate(
                date = it,
                isPrecedingMonth = it in precedingDays,
                isSucceedingMonth = it in succeedingDays,
            )
        }
    }

    data class State(
        val date: LocalDate? = null,
        val data: MonthData? = null,
    )
}
