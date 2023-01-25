package com.notificationman.library.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import com.notificationman.library.datastore.AppDataStoreImpl
import com.notificationman.library.extensions.dataStore
import com.notificationman.library.model.NotificationImportanceLevel
import com.notificationman.library.model.NotificationTypes
import java.util.*
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

        const val NOTIFICATION_CHANNEL_ID_KEY = "notification_channel_id_key"
        const val NOTIFICATION_CHANNEL_NAME_KEY = "notification_channel_name_key"
        const val NOTIFICATION_IMPORTANCE_LEVEL_KEY = "notification_importance_level_key"
        const val NOTIFICATION_SHOW_BADGE_KEY = "notification_show_badge_key"
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

    private suspend fun enqueueNotification() {
        val classPath = inputData.getString(CLASS_PATH_KEY)
        val title = inputData.getString(TITLE_KEY)
        val desc = inputData.getString(DESC_KEY)
        val thumbnailUrl = inputData.getString(THUMBNAIL_URL_KEY)
        val timeInternal = inputData.getLong(TIME_INTERVAL_KEY, DEFAULT_TIME_INTERVAL)
        val type = inputData.getInt(TYPE_KEY, NotificationTypes.TEXT.type)

        val channelId = inputData.getString(NOTIFICATION_CHANNEL_ID_KEY)
        val channelName = inputData.getString(NOTIFICATION_CHANNEL_NAME_KEY)
        val importanceLevel = inputData.getInt(
            NOTIFICATION_IMPORTANCE_LEVEL_KEY,
            NotificationImportanceLevel.DEFAULT.level
        )
        val showBadge = inputData.getBoolean(NOTIFICATION_SHOW_BADGE_KEY, true)

        val data = Data.Builder().apply {
            putString(CLASS_PATH_KEY, classPath)
            putString(TITLE_KEY, title)
            putString(DESC_KEY, desc)
            putString(THUMBNAIL_URL_KEY, thumbnailUrl)
            putInt(TYPE_KEY, type)

            putString(
                NOTIFICATION_CHANNEL_ID_KEY,
                channelId
            )
            putString(
                NOTIFICATION_CHANNEL_NAME_KEY,
                channelName
            )
            putInt(
                NOTIFICATION_IMPORTANCE_LEVEL_KEY,
                importanceLevel
            )
            putBoolean(
                NOTIFICATION_SHOW_BADGE_KEY,
                showBadge
            )
        }
        val localNotifShowWorkRequest = OneTimeWorkRequestBuilder<LocalNotificationShowWorker>()
            .setInitialDelay(timeInternal, TimeUnit.SECONDS)
            .setInputData(data.build())
            .build()
        saveWorkerId(id = localNotifShowWorkRequest.id)
        WorkManager.getInstance(context).enqueue(localNotifShowWorkRequest)
    }

    private suspend fun saveWorkerId(id: UUID) {
        AppDataStoreImpl(context.dataStore)
            .saveWorkerId(id = id)
    }
}