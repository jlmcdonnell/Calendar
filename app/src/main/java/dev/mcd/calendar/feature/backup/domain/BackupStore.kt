package dev.mcd.calendar.feature.backup.domain

interface BackupStore {
    suspend fun exportDirectoryUri(): String?
    suspend fun setExportDirectoryUri(uri: String?)
}
