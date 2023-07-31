package dev.mcd.calendar.feature.backup.domain

interface ExtractBackupFile {
    suspend operator fun invoke()
}
