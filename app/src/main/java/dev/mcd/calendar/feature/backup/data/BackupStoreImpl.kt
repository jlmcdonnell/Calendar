package dev.mcd.calendar.feature.backup.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.mcd.calendar.feature.backup.domain.BackupStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "backup_prefs")

class BackupStoreImpl(context: Context) : BackupStore {

    private val store = context.dataStore
    private val backupDirKey = stringPreferencesKey("backup-dir")

    override suspend fun backupDirectoryUri(): String? {
        return store.data.first()[backupDirKey].takeIf { !it.isNullOrBlank() }
    }

    override suspend fun setBackupDirectoryUri(uri: String?) {
        store.edit {
            if (uri == null) {
                it.remove(backupDirKey)
            } else {
                it[backupDirKey] = uri
            }
        }
    }
}
