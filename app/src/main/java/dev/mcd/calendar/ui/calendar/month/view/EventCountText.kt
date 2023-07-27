package dev.mcd.calendar.ui.calendar.month.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mcd.calendar.ui.theme.LocalAppColors

@Composable
fun EventCountText(
    modifier: Modifier = Modifier,
    count: Int,
) {
    Box(
        modifier = modifier
            .padding(top = 10.dp)
            .clip(CircleShape)
            .size(20.dp)
            .background(LocalAppColors.current.eventCountBackground),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$count",
            textAlign = TextAlign.Center,
            color = LocalAppColors.current.eventCountForeground,
            fontSize = 12.sp,
            lineHeight = 0.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
