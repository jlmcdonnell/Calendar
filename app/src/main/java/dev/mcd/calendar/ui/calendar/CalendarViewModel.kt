package dev.mcd.calendar.ui.calendar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mcd.calendar.feature.calendar.domain.GetMonthData
import dev.mcd.calendar.feature.calendar.domain.MonthData
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val dateProvider: () -> LocalDate,
    private val getMonthData: GetMonthData,
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

    data class State(
        val date: LocalDate? = null,
        val data: MonthData? = null,
    )
}
