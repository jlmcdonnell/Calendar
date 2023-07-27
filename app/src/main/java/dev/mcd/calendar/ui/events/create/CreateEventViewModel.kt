package dev.mcd.calendar.ui.events.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mcd.calendar.ui.routing.localDateArg
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<CreateEventViewModel.State, CreateEventViewModel.SideEffect> {

    override val container = container<State, SideEffect>(
        initialState = State(),
        onCreate = {
            val initialDate = savedStateHandle.get<String>("date")?.localDateArg()
            reduce {
                state.copy(date = initialDate)
            }
        },
    )

    data class State(
        val date: LocalDate? = null,
    )

    sealed interface SideEffect
}
