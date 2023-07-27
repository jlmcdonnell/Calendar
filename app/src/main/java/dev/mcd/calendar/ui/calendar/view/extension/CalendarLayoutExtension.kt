package dev.mcd.calendar.ui.calendar.view.extension

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import dev.mcd.calendar.ui.calendar.view.CalendarLayout

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
