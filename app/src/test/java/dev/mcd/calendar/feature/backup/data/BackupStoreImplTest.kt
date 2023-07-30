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
    fun `Get null export URI`() = runTest {
        BackupStoreImpl(context).exportDirectoryUri() shouldBe null
    }

    @Test
    fun `Set export URI`() = runTest {
        with(BackupStoreImpl(context)) {
            setExportDirectoryUri("uri")
            exportDirectoryUri() shouldBe "uri"
        }
    }

    @Test
    fun `Clear export URI`() = runTest {
        with(BackupStoreImpl(context)) {
            setExportDirectoryUri("uri")
            setExportDirectoryUri(null)
            exportDirectoryUri() shouldBe null
        }
    }
}
