package dev.mcd.calendar.ui.common.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import dev.mcd.calendar.R

@Composable
fun TopBar(
    title: String,
    onNavigateBack: () -> Unit,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Rounded.ArrowBack),
                    contentDescription = stringResource(id = R.string.back),
                )
            }
        },
        title = {
            Text(text = stringResource(id = R.string.create_event_title))
        },
    )
}
