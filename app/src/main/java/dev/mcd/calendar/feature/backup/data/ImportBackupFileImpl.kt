package dev.mcd.calendar.feature.backup.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mcd.calendar.feature.backup.di.ImportFile
import dev.mcd.calendar.feature.backup.domain.ImportBackupFile
import java.io.File

class ImportBackupFileImpl(
    @ApplicationContext
    private val context: Context,
    @ImportFile
    private val importFile: File,
) : ImportBackupFile {

    override suspend fun invoke(uriString: String) {
        val uri = Uri.parse(uriString)

        takePermission(uri)

        context.contentResolver.openInputStream(uri)?.use {
            it.copyTo(importFile.outputStream())
        } ?: throw Exception("NULL InputStream when opening URI: $uri")
    }

    private fun takePermission(uri: Uri) {
        val modeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        context.contentResolver.takePersistableUriPermission(uri, modeFlags)
    }
}
