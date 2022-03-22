package com.notificationman.library

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.notificationman.library.worker.LocalNotificationPostWorker

enum class NotificationTypes(val type: Int) {
    TEXT(0),
    IMAGE(1)
}

class NotificationMan {

    data class Builder(
        private val context: Context,
        private val classPathWillBeOpen: String,
        private var title: String? = null,
        private var desc: String? = null,
        private var thumbnailUrl: String? = null,
        private var timeInterval: Long? = null,
        private var type: Int = NotificationTypes.TEXT.type
    ) {
        fun setTitle(title: String) = apply { this.title = title }
        fun setDescription(desc: String) = apply { this.desc = desc }
        fun setThumbnailUrl(thumbnailUrl: String?) = apply { this.thumbnailUrl = thumbnailUrl }
        fun setTimeInterval(timeInterval: Long?) = apply { this.timeInterval = timeInterval }
        fun setNotificationType(type: Int) = apply { this.type = type }
        fun fire() {
            fireNotification()
        }

        private fun fireNotification() {
            val data = Data.Builder().apply {
                putString(LocalNotificationPostWorker.CLASS_PATH_KEY, classPathWillBeOpen)
                putString(LocalNotificationPostWorker.TITLE_KEY, title)
                putString(LocalNotificationPostWorker.DESC_KEY, desc)
                putString(LocalNotificationPostWorker.THUMBNAIL_URL_KEY, thumbnailUrl)
                putLong(LocalNotificationPostWorker.TIME_INTERVAL_KEY, timeInterval ?: LocalNotificationPostWorker.DEFAULT_TIME_INTERVAL)
                putInt(LocalNotificationPostWorker.TYPE_KEY, type)
            }
            val localNotifPostWorkRequest = OneTimeWorkRequestBuilder<LocalNotificationPostWorker>()
                .setInputData(data.build())
                .build()
            WorkManager.getInstance(context).enqueue(localNotifPostWorkRequest)
        }
    }
}