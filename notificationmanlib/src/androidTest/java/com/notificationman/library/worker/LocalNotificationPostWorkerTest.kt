package com.notificationman.library.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestWorkerBuilder
import androidx.work.workDataOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executors.newSingleThreadExecutor

@RunWith(AndroidJUnit4::class)
class LocalNotificationPostWorkerTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    @Throws(Exception::class)
    fun test_localNotificationPostWorker_returns_success() {
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

        val worker = TestWorkerBuilder<LocalNotificationPostWorker>(
            context = context,
            executor = newSingleThreadExecutor(),
            inputData = input
        ).build()

        val result = worker.doWork()
        assertThat(result, `is`(ListenableWorker.Result.success()))
    }
}