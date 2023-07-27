package dev.mcd.calendar.ui.routing

import java.time.LocalDate

fun LocalDate.navArg() = toString()

fun String.localDateArg() = LocalDate.parse(this)
