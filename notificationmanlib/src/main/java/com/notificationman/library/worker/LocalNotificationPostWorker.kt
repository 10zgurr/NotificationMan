package com.notificationman.library.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import com.notificationman.library.NotificationMan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LocalNotificationPostWorker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        private val TAG = LocalNotificationPostWorker::class.java.simpleName

        const val LOCAL_NOTIFICATION_DEFAULT_TIME_INTERVAL = 5L // 5 secs

        const val NOTIFICATION_CLASS_NAME_KEY = "notification_class_name_key"
        const val NOTIFICATION_TITLE_KEY = "notification_title_key"
        const val NOTIFICATION_DESC_KEY = "notification_desc_key"
        const val NOTIFICATION_THUMBNAIL_IMAGE_KEY = "notification_thumbnail_image_key"
        const val NOTIFICATION_TIME_INTERVAL_KEY = "notification_time_interval_key"
        const val NOTIFICATION_TYPE_KEY = "notification_type_key"
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun doWork(): Result {
        return try {
            coroutineScope.launch { enqueueNotification() }
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "local notification post worker has failed: ${e.message}")
            Result.failure()
        }
    }

    private fun enqueueNotification() {
        val className = inputData.getString(NOTIFICATION_CLASS_NAME_KEY)
        val title = inputData.getString(NOTIFICATION_TITLE_KEY)
        val desc = inputData.getString(NOTIFICATION_DESC_KEY)
        val thumbnailImageUrl = inputData.getString(NOTIFICATION_THUMBNAIL_IMAGE_KEY)
        val timeInternal = inputData.getLong(NOTIFICATION_TIME_INTERVAL_KEY, LOCAL_NOTIFICATION_DEFAULT_TIME_INTERVAL)
        val type = inputData.getInt(NOTIFICATION_TYPE_KEY, NotificationMan.NOTIFICATION_TYPE_TEXT)
        val data = Data.Builder().apply {
            putString(NOTIFICATION_CLASS_NAME_KEY, className)
            putString(NOTIFICATION_TITLE_KEY, title)
            putString(NOTIFICATION_DESC_KEY, desc)
            putString(NOTIFICATION_THUMBNAIL_IMAGE_KEY, thumbnailImageUrl)
            putInt(NOTIFICATION_TYPE_KEY, type)
        }
        val localNotifShowWorkRequest = OneTimeWorkRequestBuilder<LocalNotificationShowWorker>()
            .setInitialDelay(timeInternal, TimeUnit.SECONDS)
            .setInputData(data.build())
            .build()
        WorkManager.getInstance(context).enqueue(localNotifShowWorkRequest)
        Log.d(TAG, "Post Worker started")
    }
}