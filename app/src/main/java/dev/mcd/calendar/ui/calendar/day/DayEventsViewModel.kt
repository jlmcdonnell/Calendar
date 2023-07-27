package dev.mcd.calendar.ui.calendar.day

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mcd.calendar.feature.calendar.domain.EventsRepository
import dev.mcd.calendar.feature.calendar.domain.entity.Event
import dev.mcd.calendar.ui.calendar.day.DayEventsViewModel.SideEffect
import dev.mcd.calendar.ui.calendar.day.DayEventsViewModel.State
import dev.mcd.calendar.ui.routing.localDateArg
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DayEventsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: EventsRepository,
) : ViewModel(), ContainerHost<State, SideEffect> {

    override val container = container(
        initialState = State(),
        onCreate = {
            val date = savedStateHandle.get<String>("date")!!.localDateArg()

            reduce {
                state.copy(
                    date = date,
                )
            }

            repeatOnSubscription {
                loadEvents(date)
            }
        },
    )

    fun onAddEvent() {
        intent {
            val date = state.date ?: return@intent

            postSideEffect(SideEffect.NavigateCreateEvent(date))
        }
    }

    fun onDeleteEvent(event: Event) {
        intent {
            println(event.id)
            repository.deleteEvent(event.id)

            val date = state.date ?: return@intent
            loadEvents(date)
        }
    }

    context(SimpleSyntax<State, SideEffect>)
    private suspend fun loadEvents(date: LocalDate) {
        val events = repository.findByDate(date)
        reduce {
            state.copy(
                events = events,
            )
        }
    }

    data class State(
        val date: LocalDate? = null,
        val events: List<Event> = emptyList(),
    )

    sealed interface SideEffect {
        data class NavigateCreateEvent(val date: LocalDate) : SideEffect
    }
}
