package com.notificationman.library.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import com.notificationman.library.NotificationTypes
import java.util.concurrent.TimeUnit

class LocalNotificationPostWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "LNPostWorker"

        const val DEFAULT_TIME_INTERVAL = 5L // 5 secs

        const val CLASS_PATH_KEY = "class_name_key"
        const val TITLE_KEY = "title_key"
        const val DESC_KEY = "desc_key"
        const val THUMBNAIL_URL_KEY = "thumbnail_url_key"
        const val TIME_INTERVAL_KEY = "time_interval_key"
        const val TYPE_KEY = "type_key"
    }

    override suspend fun doWork(): Result {
        return try {
            enqueueNotification()
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "local notification post worker has failed: ${e.message}")
            Result.failure()
        }
    }

    private fun enqueueNotification() {
        val classPath = inputData.getString(CLASS_PATH_KEY)
        val title = inputData.getString(TITLE_KEY)
        val desc = inputData.getString(DESC_KEY)
        val thumbnailUrl = inputData.getString(THUMBNAIL_URL_KEY)
        val timeInternal = inputData.getLong(TIME_INTERVAL_KEY, DEFAULT_TIME_INTERVAL)
        val type = inputData.getInt(TYPE_KEY, NotificationTypes.TEXT.type)
        val data = Data.Builder().apply {
            putString(CLASS_PATH_KEY, classPath)
            putString(TITLE_KEY, title)
            putString(DESC_KEY, desc)
            putString(THUMBNAIL_URL_KEY, thumbnailUrl)
            putInt(TYPE_KEY, type)
        }
        val localNotifShowWorkRequest = OneTimeWorkRequestBuilder<LocalNotificationShowWorker>()
            .setInitialDelay(timeInternal, TimeUnit.SECONDS)
            .setInputData(data.build())
            .build()
        WorkManager.getInstance(context).enqueue(localNotifShowWorkRequest)
    }
}