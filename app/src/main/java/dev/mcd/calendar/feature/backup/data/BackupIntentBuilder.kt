package dev.mcd.calendar.feature.backup.data

import android.content.Context
import android.content.Intent
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import dev.mcd.calendar.R
import dev.mcd.calendar.feature.backup.provider.BackupFileProvider
import java.io.File

fun buildBackupIntent(
    backupFile: File,
    context: Context,
): Intent {
    val uri = FileProvider
        .getUriForFile(
            context,
            BackupFileProvider.AUTHORITY,
            backupFile,
        )

    return ShareCompat.IntentBuilder(context)
        .addStream(uri)
        .setChooserTitle(context.getString(R.string.backup_intent_title))
        .setType("application/zip")
        .createChooserIntent()
        .apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
}
