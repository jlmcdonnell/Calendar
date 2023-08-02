package dev.mcd.calendar.ui.calendar.month.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.mcd.calendar.feature.calendar.domain.entity.CalendarDate
import dev.mcd.calendar.feature.calendar.domain.entity.MonthDays
import dev.mcd.calendar.ui.calendar.month.view.modifier.CalendarLayout
import dev.mcd.calendar.ui.calendar.month.view.modifier.calendarLayout
import dev.mcd.calendar.ui.theme.LocalAppColors
import java.time.LocalDate

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    monthDays: MonthDays,
    date: LocalDate,
    renderCell: @Composable BoxScope.(CalendarDate) -> Unit = {},
    onCellClicked: (CalendarDate) -> Unit = {},
) {
    var calendarLayout by remember { mutableStateOf<CalendarLayout?>(null) }
    val appColors = LocalAppColors.current

    Column {
        calendarLayout?.run {
            WeekDays(
                modifier = Modifier
                    .padding(
                        horizontal = 0.dp,
                        vertical = 16.dp,
                    ),
            )
        }
        FlowRow(
            modifier = modifier
                .calendarLayout { calendarLayout = it }
                .clip(RoundedCornerShape(3.dp))
                .background(appColors.calendarBackground),
        ) {
            calendarLayout?.run {
                monthDays.forEachIndexed { i, calendarDate ->
                    CalendarCell(
                        index = i,
                        cellSize = cellSize,
                        date = calendarDate,
                        isToday = calendarDate.date == date,
                        onCellClicked = onCellClicked,
                        renderCell = {
                            renderCell(calendarDate)
                        },
                    )
                }
            }
        }
    }
}
