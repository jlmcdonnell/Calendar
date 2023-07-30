package dev.mcd.calendar.feature.backup.domain

interface ExportDatabase {

    sealed interface Result {
        data object Success : Result
        data object NoExportUri : Result
        data object DocumentCreateError : Result
    }

    suspend operator fun invoke(): Result
}
