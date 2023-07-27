package dev.mcd.calendar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

private val darkColors = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

private val lightColors = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
)

sealed interface AppColors {
    val calendarBackground: Color
    val calendarContentColor: Color
    val outOfMonthBackground: Color
    val inMonthBackground: Color
    val eventCountBackground: Color
    val eventCountForeground: Color

    data object Light : AppColors {
        override val calendarContentColor = Color(0xFF4E4E4E)
        override val calendarBackground = Color(0xFFDFDFDF)
        override val outOfMonthBackground = Color(0xFFF1F1F1)
        override val inMonthBackground = Color.White
        override val eventCountBackground = Color(0xFFFF3C3C)
        override val eventCountForeground = Color(0xFFFFFFFF)
    }

    data object Dark : AppColors {
        override val calendarContentColor = Color(0xFFAFAFAF)
        override val calendarBackground = Color(0xFF111111)
        override val outOfMonthBackground = Color(0xFF1D1D1D)
        override val inMonthBackground = Color(0xFF242424)
        override val eventCountBackground = Color(0xFFFF3C3C)
        override val eventCountForeground = Color(0xFFFFFFFF)
    }
}

val LocalAppColors = compositionLocalOf<AppColors> { AppColors.Dark }

@Composable
fun CalendarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val (colorScheme, appColors) = when {
        darkTheme -> darkColors to AppColors.Dark
        else -> lightColors to AppColors.Light
    }

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}
