package dev.mcd.calendar.feature.backup.domain

interface BackupDatabase {
    suspend operator fun invoke()
}
