package com.notificationman.library.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

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
            val channelId = "fake_channel_id"
            val channelName = "fake_channel_name"
            val input = workDataOf(
                LocalNotificationPostWorker.CLASS_PATH_KEY to classPath,
                LocalNotificationPostWorker.TITLE_KEY to title,
                LocalNotificationPostWorker.DESC_KEY to desc,
                LocalNotificationPostWorker.TIME_INTERVAL_KEY to timeInterval,
                LocalNotificationPostWorker.NOTIFICATION_CHANNEL_ID_KEY to channelId,
                LocalNotificationPostWorker.NOTIFICATION_CHANNEL_NAME_KEY to channelName
            )

            val worker = TestListenableWorkerBuilder<LocalNotificationShowWorker>(
                context = context,
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
            val channelId = "fake_channel_id"
            val channelName = "fake_channel_name"
            val input = workDataOf(
                LocalNotificationPostWorker.TITLE_KEY to title,
                LocalNotificationPostWorker.DESC_KEY to desc,
                LocalNotificationPostWorker.TIME_INTERVAL_KEY to timeInterval,
                LocalNotificationPostWorker.NOTIFICATION_CHANNEL_ID_KEY to channelId,
                LocalNotificationPostWorker.NOTIFICATION_CHANNEL_NAME_KEY to channelName
            )

            val worker = TestListenableWorkerBuilder<LocalNotificationShowWorker>(
                context = context,
                inputData = input
            ).build()

            val result = worker.doWork()
            assertThat(result, `is`(ListenableWorker.Result.failure()))
        }
    }
}