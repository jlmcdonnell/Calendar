package dev.mcd.calendar.test.viewmodel

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import org.orbitmvi.orbit.ContainerHost

context(VM)
suspend inline fun <State : Any, VM : ContainerHost<State, *>> testState(
    consumeInitialState: Boolean = true,
    crossinline block: suspend TurbineTestContext<State>.() -> Unit,
) {
    container.stateFlow.test {
        if (consumeInitialState) {
            awaitItem()
        }
        block(this)
    }
}

context(VM)
suspend inline fun <SideEffect : Any, VM : ContainerHost<*, SideEffect>> testSideEffect(
    crossinline block: suspend TurbineTestContext<SideEffect>.() -> Unit,
) {
    container.sideEffectFlow.test {
        block(this)
    }
}
