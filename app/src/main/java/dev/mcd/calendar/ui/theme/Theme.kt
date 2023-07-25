package dev.mcd.calendar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
)

sealed interface AppColors {
    val calendarBackground: Color
    val calendarContentColor: Color
    val outOfMonthBackground: Color
    val inMonthBackground: Color

    object Light : AppColors {
        override val calendarContentColor = Color(0xFF4E4E4E)
        override val calendarBackground = Color(0xFFDFDFDF)
        override val outOfMonthBackground = Color(0xFFF1F1F1)
        override val inMonthBackground = Color.White
    }

    object Dark : AppColors {
        override val calendarContentColor = Color(0xFFAFAFAF)
        override val calendarBackground = Color(0xFF111111)
        override val outOfMonthBackground = Color(0xFF242424)
        override val inMonthBackground = Color(0xFF1D1D1D)
    }
}

val LocalAppColors = compositionLocalOf<AppColors> { AppColors.Dark }

@Composable
fun CalendarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val (colorScheme, appColors) = when {
        darkTheme -> DarkColorScheme to AppColors.Dark
        else -> LightColorScheme to AppColors.Light
    }

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}
