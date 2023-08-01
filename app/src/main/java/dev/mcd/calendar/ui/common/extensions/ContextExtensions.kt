package dev.mcd.calendar.ui.common.extensions

import android.content.Context
import android.content.Intent
import kotlin.system.exitProcess

fun Context.restartApplication() {
    val intent = packageManager.getLaunchIntentForPackage(packageName)!!
    val mainIntent = Intent.makeRestartActivityTask(intent.component)
    startActivity(mainIntent)
    exitProcess(0)
}
