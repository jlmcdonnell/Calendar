package dev.mcd.calendar.feature.backup.data

import androidx.test.platform.app.InstrumentationRegistry
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BackupStoreImplTest {

    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun `Get null backup URI`() = runTest {
        BackupStoreImpl(context).backupDirectoryUri() shouldBe null
    }

    @Test
    fun `Set backup URI`() = runTest {
        with(BackupStoreImpl(context)) {
            setBackupDirectoryUri("uri")
            backupDirectoryUri() shouldBe "uri"
        }
    }

    @Test
    fun `Clear backup URI`() = runTest {
        with(BackupStoreImpl(context)) {
            setBackupDirectoryUri("uri")
            setBackupDirectoryUri(null)
            backupDirectoryUri() shouldBe null
        }
    }
}
