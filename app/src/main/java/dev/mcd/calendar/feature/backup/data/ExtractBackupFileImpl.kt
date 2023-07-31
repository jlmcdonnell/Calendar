package dev.mcd.calendar.feature.backup.data

import dev.mcd.calendar.feature.backup.domain.ExtractBackupFile
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class ExtractBackupFileImpl(
    private val importFile: File,
    private val targetDir: File,
) : ExtractBackupFile {

    override suspend fun invoke() {
        if (!importFile.exists()) {
            return
        }

        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }

        ZipInputStream(importFile.inputStream()).use { input ->
            var count = 0
            var entry: ZipEntry? = input.nextEntry

            while (entry != null) {
                val entryFile = File(targetDir, entry.name)
                entryFile.createNewFile()
                input.copyTo(entryFile.outputStream())
                input.closeEntry()
                count++
                entry = input.nextEntry
            }
        }

        importFile.delete()
    }
}
