package dev.mcd.calendar.ui.calendar.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mcd.calendar.calendar.domain.CalendarDate
import dev.mcd.calendar.calendar.domain.MonthData
import dev.mcd.calendar.calendar.domain.isInMonth

private val topEdgeIndices = listOf(0, 1, 2, 3, 4, 5, 6)
private val bottomEdgeIndices = listOf(35, 36, 37, 38, 39, 40, 41)
private val startEdgeIndices = listOf(0, 7, 14, 21, 28, 35)
private val endEdgeIndices = listOf(6, 13, 20, 27, 34, 41)

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    monthData: MonthData,
    renderCell: @Composable (CalendarDate) -> Unit = {},
) {
    var calendarLayout by remember { mutableStateOf<CalendarLayout?>(null) }
    Box(
        modifier = modifier
            .calendarLayout { calendarLayout = it }
            .clip(RoundedCornerShape(3.dp))
            .background(Color(0xFFDFDFDF)),
    ) {
        FlowRow(
            modifier = Modifier,

        ) {
            calendarLayout?.run {
                monthData.days.forEachIndexed { i, date ->
                    CalendarCell(
                        index = i,
                        date = date,
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

context(CalendarLayout)
@Composable
fun CalendarCell(
    index: Int,
    date: CalendarDate,
    renderCell: @Composable () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .size(cellSize)
            .padding(
                top = if (index in topEdgeIndices) 2.dp else 1.dp,
                bottom = if (index in bottomEdgeIndices) 2.dp else 1.dp,
                start = if (index in startEdgeIndices) 2.dp else 1.dp,
                end = if (index in endEdgeIndices) 2.dp else 1.dp,
            )
            .clip(RoundedCornerShape(2.dp))
            .let {
                if (date.isInMonth) {
                    it.background(Color.White)
                } else {
                    it.background(Color(0xFFF1F1F1))
                }
            },
    ) {
        Text(
            modifier = Modifier.padding(start = 3.dp),
            text = "${date.date.dayOfMonth}",
            color = Color(0xFF4D4D4D),
            fontSize = 11.sp,
        )
        renderCell()
    }
}
