package com.notificationman.library.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_MAX
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.notificationman.library.R
import com.notificationman.library.datastore.AppDataStoreImpl
import com.notificationman.library.extensions.dataStore
import com.notificationman.library.extensions.getBitmapFromURL
import com.notificationman.library.extensions.getCroppedBitmap
import com.notificationman.library.model.NotificationImportanceLevel
import com.notificationman.library.model.NotificationTypes
import java.util.*

class LocalNotificationShowWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "LNShowWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            val notificationID = System.currentTimeMillis().toInt()
            val classPath = inputData.getString(LocalNotificationPostWorker.CLASS_PATH_KEY)
            val title = inputData.getString(LocalNotificationPostWorker.TITLE_KEY)
            val desc = inputData.getString(LocalNotificationPostWorker.DESC_KEY)
            val thumbnailUrl = inputData.getString(LocalNotificationPostWorker.THUMBNAIL_URL_KEY)
            val type = inputData.getInt(LocalNotificationPostWorker.TYPE_KEY, NotificationTypes.TEXT.type)

            val channelId = inputData.getString(LocalNotificationPostWorker.NOTIFICATION_CHANNEL_ID_KEY)!!
            val channelName = inputData.getString(LocalNotificationPostWorker.NOTIFICATION_CHANNEL_NAME_KEY)!!
            val importanceLevel = inputData.getInt(
                LocalNotificationPostWorker.NOTIFICATION_IMPORTANCE_LEVEL_KEY,
                NotificationImportanceLevel.DEFAULT.level
            )
            val showBadge = inputData.getBoolean(
                LocalNotificationPostWorker.NOTIFICATION_SHOW_BADGE_KEY, true)
            val notification = createNotification(
                context = context,
                classPath = classPath!!,
                title = title,
                body = desc,
                thumbnailUrl = thumbnailUrl,
                notificationID = notificationID,
                notificationType = type
            )
            showNotification(
                context = context,
                notification = notification,
                notificationID = notificationID,
                channelId = channelId,
                channelName = channelName,
                importanceLevel = importanceLevel,
                showBadge = showBadge,
            )
            deleteWorkerId(
                id = workerParams.id
            )
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "local notification show worker has failed: ${e.message}")
            Result.failure()
        }
    }

    private fun createNotification(
        context: Context,
        classPath: String,
        title: String?,
        body: String?,
        thumbnailUrl: String?,
        notificationID: Int,
        notificationType: Int
    ): Notification {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            component = ComponentName(context.packageName, classPath)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationID,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            else
                PendingIntent.FLAG_ONE_SHOT
        )
        val channelId = context.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat
            .Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_SOUND)
            .setColorized(true)
            .setContentTitle(
                if (title.isNullOrBlank().not())
                    title
                else
                    context.getString(R.string.app_name)
            )
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0))
            .setPriority(IMPORTANCE_MAX)

        return if (thumbnailUrl.isNullOrBlank()) {
            notificationBuilder
                .setStyle(getBigTextNotificationStyle(body))
                .build()
        } else {
            val roundedBitmap = thumbnailUrl
                .getBitmapFromURL()
                ?.getCroppedBitmap()
            val style = when (notificationType) {
                NotificationTypes.TEXT.type ->
                    getBigTextNotificationStyle(body)
                NotificationTypes.IMAGE.type ->
                    getBigPictureNotificationStyle(roundedBitmap)
                else -> null
            }
            notificationBuilder
                .setLargeIcon(roundedBitmap)
                .setStyle(style)
                .build()
        }
    }

    private fun getBigTextNotificationStyle(
        body: String?
    ) = NotificationCompat
        .BigTextStyle()
        .bigText(body)

    private fun getBigPictureNotificationStyle(
        bitmap: Bitmap?
    ) = NotificationCompat
        .BigPictureStyle()
        .bigPicture(bitmap)
        .bigLargeIcon(null)

    private fun showNotification(
        context: Context,
        notification: Notification,
        notificationID: Int,
        channelId: String,
        channelName: String,
        importanceLevel: Int,
        showBadge: Boolean
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                getNotificationImportance(level = importanceLevel)
            ).apply {
                setShowBadge(showBadge)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationID, notification)
    }

    private suspend fun deleteWorkerId(id: UUID) {
        AppDataStoreImpl(context.dataStore)
            .deleteWorkerId(id = id)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getNotificationImportance(level: Int): Int =
        when (level) {
            NotificationImportanceLevel.LOW.level ->
                NotificationManager.IMPORTANCE_LOW
            NotificationImportanceLevel.DEFAULT.level ->
                NotificationManager.IMPORTANCE_DEFAULT
            NotificationImportanceLevel.HIGH.level ->
                NotificationManager.IMPORTANCE_HIGH
            else ->
                NotificationManager.IMPORTANCE_HIGH
        }
}