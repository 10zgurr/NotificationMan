package com.notificationman.library

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.notificationman.library.worker.LocalNotificationPostWorker

class NotificationMan {

    companion object {
        const val NOTIFICATION_TYPE_TEXT = 0
        const val NOTIFICATION_TYPE_IMAGE = 1
    }

    data class Builder(
        private val context: Context,
        private var classNameWillBeOpen: String,
        private var title: String? = null,
        private var desc: String? = null,
        private var thumbnailImageUrl: String? = null,
        private var timeInterval: Long? = null,
        private var type: Int = NOTIFICATION_TYPE_TEXT
    ) {
        fun setTitle(title: String) = apply { this.title = title }
        fun setDescription(desc: String) = apply { this.desc = desc }
        fun setThumbnailImageUrl(thumbnailImageUrl: String?) = apply { this.thumbnailImageUrl = thumbnailImageUrl }
        fun setTimeInterval(timeInterval: Long) = apply { this.timeInterval = timeInterval }
        fun setNotificationType(type: Int) = apply { this.type = type }
        fun fire() = fireNotification()

        private fun fireNotification() {
            val data = Data.Builder().apply {
                putString(LocalNotificationPostWorker.NOTIFICATION_CLASS_NAME_KEY, classNameWillBeOpen)
                putString(LocalNotificationPostWorker.NOTIFICATION_TITLE_KEY, title)
                putString(LocalNotificationPostWorker.NOTIFICATION_DESC_KEY, desc)
                putString(LocalNotificationPostWorker.NOTIFICATION_THUMBNAIL_IMAGE_KEY, thumbnailImageUrl)
                putLong(LocalNotificationPostWorker.NOTIFICATION_TIME_INTERVAL_KEY, timeInterval ?: LocalNotificationPostWorker.LOCAL_NOTIFICATION_DEFAULT_TIME_INTERVAL)
                putInt(LocalNotificationPostWorker.NOTIFICATION_TYPE_KEY, type)
            }
            val localNotifPostWorkRequest = OneTimeWorkRequestBuilder<LocalNotificationPostWorker>()
                .setInputData(data.build())
                .build()
            WorkManager.getInstance(context).enqueue(localNotifPostWorkRequest)
        }
    }
}