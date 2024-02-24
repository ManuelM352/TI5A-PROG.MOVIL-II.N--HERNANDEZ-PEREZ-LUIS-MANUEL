import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.workers.CleanupWorker
import com.example.bluromatic.workers.SaveImageToFileWorker
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

private lateinit var context: Context

@Before
fun setUp() {
    context = ApplicationProvider.getApplicationContext()
}

@Test
fun saveImageToFileWorker_doWork_resultSuccessReturnsUrl() {
    val worker = TestListenableWorkerBuilder<SaveImageToFileWorker>(context)
        //.setInputData(workDataOf(mockUriInput))
        .build()
    runBlocking {
        val result = worker.doWork()
        val resultUri = result.outputData.getString(KEY_IMAGE_URI)
        assertTrue(result is ListenableWorker.Result.Success)
        assertTrue(result.outputData.keyValueMap.containsKey(KEY_IMAGE_URI))
        assertTrue(
            resultUri?.startsWith("content://media/external/images/media/")
                ?: false
        )
    }
}