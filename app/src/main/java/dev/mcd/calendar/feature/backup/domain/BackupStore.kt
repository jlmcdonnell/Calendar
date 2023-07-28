package dev.mcd.calendar.feature.backup.domain

interface BackupStore {
    suspend fun backupDirectoryUri(): String?
    suspend fun setBackupDirectoryUri(uri: String?)
}
