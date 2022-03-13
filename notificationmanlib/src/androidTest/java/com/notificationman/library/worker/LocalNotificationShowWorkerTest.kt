package com.notificationman.library.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestWorkerBuilder
import androidx.work.workDataOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executors.newSingleThreadExecutor

class LocalNotificationShowWorkerTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    @Throws(Exception::class)
    fun test_localNotificationShowWorker_returns_success() {
        runBlocking {
            // Define input data
            val classPath = "com.notification.man.MainActivity"
            val title = "fake_title"
            val desc = "fake_desc"
            val timeInterval = 2
            val input = workDataOf(
                LocalNotificationPostWorker.CLASS_PATH_KEY to classPath,
                LocalNotificationPostWorker.TITLE_KEY to title,
                LocalNotificationPostWorker.DESC_KEY to desc,
                LocalNotificationPostWorker.TIME_INTERVAL_KEY to timeInterval
            )

            val worker = TestWorkerBuilder<LocalNotificationShowWorker>(
                context = context,
                executor = newSingleThreadExecutor(),
                inputData = input
            ).build()

            val result = worker.doWork()
            assertThat(result, `is`(ListenableWorker.Result.success()))
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_localNotificationShowWorker_returns_failure() {
        runBlocking {
            // Define input data without class path which should not be null
            val title = "fake_title"
            val desc = "fake_desc"
            val timeInterval = 2
            val input = workDataOf(
                LocalNotificationPostWorker.TITLE_KEY to title,
                LocalNotificationPostWorker.DESC_KEY to desc,
                LocalNotificationPostWorker.TIME_INTERVAL_KEY to timeInterval
            )

            val worker = TestWorkerBuilder<LocalNotificationShowWorker>(
                context = context,
                executor = newSingleThreadExecutor(),
                inputData = input
            ).build()

            val result = worker.doWork()
            assertThat(result, `is`(ListenableWorker.Result.failure()))
        }
    }
}