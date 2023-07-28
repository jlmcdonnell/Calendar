package dev.mcd.calendar.feature.backup.provider

import androidx.core.content.FileProvider
import dev.mcd.calendar.R

class BackupFileProvider : FileProvider(R.xml.backup_file_paths) {
    companion object {
        const val AUTHORITY = "dev.mcd.calendar.provider"
    }
}
