package dev.mcd.calendar.feature.backup.domain

interface ImportBackupFile {
    suspend operator fun invoke(uriString: String)
}
