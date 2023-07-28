package dev.mcd.calendar.feature.backup.domain

interface BackupDatabase {

    sealed interface Result {
        data object Success : Result
        data object NoBackupUri : Result
        data object DocumentCreateError : Result
    }

    suspend operator fun invoke(): Result
}
