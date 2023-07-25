package dev.mcd.calendar.ui.calendar.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

fun Modifier.calendarLayout(
    onLayout: (CalendarLayout) -> Unit = {},
) = composed {
    var layout by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val density = LocalDensity.current

    Modifier.onGloballyPositioned { layoutCoordinates ->
        layout = layoutCoordinates
        with(density) {
            onLayout(
                CalendarLayout(
                    cellSize = (layout!!.size.width / 7f).toDp(),
                ),
            )
        }
    }
}