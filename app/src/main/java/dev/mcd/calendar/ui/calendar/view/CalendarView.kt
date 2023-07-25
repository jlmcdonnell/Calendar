package dev.mcd.calendar.ui.calendar.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mcd.calendar.ui.theme.CalendarTheme
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.temporal.TemporalAdjusters

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    renderCell: @Composable (OffsetDateTime) -> Unit = {},
) {
    var calendarLayout by remember { mutableStateOf<CalendarLayout?>(null) }
    val today = remember { OffsetDateTime.now() }
    val days = remember {
        today
            .with(TemporalAdjusters.dayOfWeekInMonth(0, DayOfWeek.MONDAY))
            .let { start ->
                listOf(start) + 1L.until(42).map {
                    start.plusDays(it)
                }
            }
    }

    FlowRow(
        modifier = modifier
            .calendarLayout { calendarLayout = it }
            .border(2.dp, color = Color(0xFFBBBBBB)),
    ) {
        calendarLayout?.run {
            days.forEach { date ->
                date.monthValue
                CalendarCell(
                    date = date,
                    inMonth = today.monthValue == date.monthValue,
                    renderCell = {
                        renderCell(date)
                    },
                )
            }
        }
    }
}

context(CalendarLayout)
@Composable
fun CalendarCell(
    date: OffsetDateTime,
    inMonth: Boolean,
    renderCell: @Composable () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .size(cellSize)
            .let {
                if (inMonth) {
                    it
                } else {
                    it.background(Color(0xFFD6D6D6))
                }
            }
            .border(1.dp, color = Color(0xFFBBBBBB)),
    ) {
        Text(
            modifier = Modifier.padding(start = 3.dp),
            text = "${date.dayOfMonth}/${date.monthValue}",
            fontSize = 11.sp,
        )
        renderCell()
    }
}

@Preview
@Composable
fun CalendarPreview() {
    CalendarTheme {
        Surface {
            Box(modifier = Modifier.width(400.dp)) {
                CalendarView(
                    modifier = Modifier,
                ) { date ->
                }
            }
        }
    }
}
