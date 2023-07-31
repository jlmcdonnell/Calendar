package dev.mcd.calendar.ui.common.extensions

import android.content.Context
import android.content.Intent

fun Context.restartApplication() {
    val intent = packageManager.getLaunchIntentForPackage(packageName)!!
    val mainIntent = Intent.makeRestartActivityTask(intent.component)
    startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}
