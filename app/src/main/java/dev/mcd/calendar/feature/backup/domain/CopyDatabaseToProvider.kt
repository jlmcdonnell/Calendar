package dev.mcd.calendar.feature.backup.domain

interface CopyDatabaseToProvider {
    suspend operator fun invoke()
}
