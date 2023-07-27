package dev.mcd.calendar.ui.calendar.month.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mcd.calendar.feature.calendar.domain.entity.CalendarDate
import dev.mcd.calendar.feature.calendar.domain.entity.MonthDays
import dev.mcd.calendar.feature.calendar.domain.entity.isInMonth
import dev.mcd.calendar.ui.calendar.month.view.extension.CalendarViewIndices
import dev.mcd.calendar.ui.calendar.month.view.extension.calendarCellPadding
import dev.mcd.calendar.ui.calendar.month.view.extension.calendarLayout
import dev.mcd.calendar.ui.theme.LocalAppColors
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    monthDays: MonthDays,
    renderCell: @Composable (CalendarDate) -> Unit = {},
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
                monthDays.forEachIndexed { i, date ->
                    CalendarViewIndices.run {
                        CalendarCell(
                            index = i,
                            date = date,
                            onCellClicked = onCellClicked,
                            renderCell = {
                                Column {
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                                renderCell(date)
                            },
                        )
                    }
                }
            }
        }
    }
}

context(CalendarLayout)
@Composable
fun WeekDays(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = cellSize / 2)
            .alpha(.6f),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        DayOfWeek.values().forEach { day ->
            Text(
                modifier = Modifier.width(10.dp),
                text = day.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                fontSize = 12.sp,
            )
        }
    }
}

context(CalendarLayout, CalendarViewIndices)
@Composable
fun CalendarCell(
    index: Int,
    date: CalendarDate,
    renderCell: @Composable () -> Unit = {},
    onCellClicked: (CalendarDate) -> Unit = {},
) {
    val appColors = LocalAppColors.current

    Box(
        modifier = Modifier
            .size(cellSize)
            .calendarCellPadding(index)
            .clip(RoundedCornerShape(2.dp))
            .let {
                if (date.isInMonth) {
                    it.background(appColors.inMonthBackground)
                } else {
                    it.background(appColors.outOfMonthBackground)
                }
            }
            .clickable { onCellClicked(date) },
    ) {
        Text(
            modifier = Modifier.padding(start = 3.dp),
            text = "${date.date.dayOfMonth}",
            color = appColors.calendarContentColor,
            fontSize = 11.sp,
        )
        renderCell()
    }
}
