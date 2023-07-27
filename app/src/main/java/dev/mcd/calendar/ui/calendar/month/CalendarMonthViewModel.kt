package dev.mcd.calendar.ui.calendar.month

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mcd.calendar.feature.calendar.domain.GetEventCountsForMonth
import dev.mcd.calendar.feature.calendar.domain.GetMonthDays
import dev.mcd.calendar.feature.calendar.domain.entity.DateEventCount
import dev.mcd.calendar.feature.calendar.domain.entity.MonthDays
import dev.mcd.calendar.ui.calendar.month.CalendarMonthViewModel.SideEffect
import dev.mcd.calendar.ui.calendar.month.CalendarMonthViewModel.State
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarMonthViewModel @Inject constructor(
    private val dateProvider: () -> LocalDate,
    private val getMonthDays: GetMonthDays,
    private val getEventCountsForMonth: GetEventCountsForMonth,
) : ViewModel(), ContainerHost<State, SideEffect> {

    override val container = container<State, SideEffect>(
        initialState = State(),
        onCreate = {
            intent {
                val date = dateProvider()
                selectDate(date)
            }
        },
    )

    fun onNextMonth() {
        intent {
            val date = state.date ?: return@intent
            selectDate(date.plusMonths(1))
        }
    }

    fun onPreviousMonth() {
        intent {
            val date = state.date ?: return@intent
            selectDate(date.minusMonths(1))
        }
    }

    fun onGoToDate(date: LocalDate) {
        intent {
            selectDate(date)
        }
    }

    fun onDateClicked(date: LocalDate) {
        intent {
            if ((state.events[date]?.count ?: 0) > 0) {
                postSideEffect(SideEffect.NavigateToDay(date))
            } else {
                postSideEffect(SideEffect.NavigateCreateEvent(date))
            }
        }
    }

    context(SimpleSyntax<State, SideEffect>)
    private suspend fun selectDate(date: LocalDate) {
        val newMonthDays = if (state.monthDays?.any { it.date == date } != true) {
            getMonthDays(date)
        } else {
            null
        }
        val events = if (newMonthDays != null) {
            getEventCountsForMonth(newMonthDays)
        } else {
            null
        }

        reduce {
            state.copy(
                date = date,
                monthDays = newMonthDays ?: state.monthDays,
                events = events ?: state.events,
            )
        }
    }

    data class State(
        val date: LocalDate? = null,
        val monthDays: MonthDays? = null,
        val events: Map<LocalDate, DateEventCount> = emptyMap(),
    )

    sealed interface SideEffect {
        data class NavigateCreateEvent(
            val date: LocalDate,
        ) : SideEffect

        data class NavigateToDay(
            val date: LocalDate,
        ) : SideEffect
    }
}
