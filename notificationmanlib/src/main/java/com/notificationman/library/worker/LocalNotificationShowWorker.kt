package com.notificationman.library.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_MAX
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.notificationman.library.NotificationTypes
import com.notificationman.library.R
import com.notificationman.library.utils.getBitmapFromURL
import com.notificationman.library.utils.getCroppedBitmap

class LocalNotificationShowWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        private const val TAG = "LNShowWorker"
    }

    override fun doWork(): Result {
        return try {
            val notificationID = System.currentTimeMillis().toInt()
            val classPath = inputData.getString(LocalNotificationPostWorker.CLASS_PATH_KEY)
            val title = inputData.getString(LocalNotificationPostWorker.TITLE_KEY)
            val desc = inputData.getString(LocalNotificationPostWorker.DESC_KEY)
            val thumbnailUrl = inputData.getString(LocalNotificationPostWorker.THUMBNAIL_URL_KEY)
            val type = inputData.getInt(LocalNotificationPostWorker.TYPE_KEY, NotificationTypes.TEXT.type)
            val notification = createNotification(context, classPath!!, title, desc, thumbnailUrl, notificationID, type)
            Log.d(TAG, "created local notification -> $notification")
            showNotification(context, notification, notificationID)
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
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_SOUND)
            .setColorized(true)
            .setContentTitle(title ?: context.getString(R.string.app_name))
            .setContentText(body ?: "")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    IMPORTANCE_MAX
                else
                    Notification.PRIORITY_MAX
            )
        if (Build.VERSION.SDK_INT >= 21)
            notificationBuilder.setVibrate(longArrayOf(0))

        return if (thumbnailUrl.isNullOrEmpty()) {
            notificationBuilder
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .build()
        } else {
            val roundedBitmap = thumbnailUrl
                .getBitmapFromURL()
                ?.getCroppedBitmap()
            roundedBitmap?.let { notificationBuilder.setLargeIcon(it) }
            when (notificationType) {
                NotificationTypes.TEXT.type ->
                    notificationBuilder
                        .setStyle(
                            NotificationCompat
                                .BigTextStyle()
                                .bigText(body)
                        )
                NotificationTypes.IMAGE.type ->
                    roundedBitmap?.let {
                        notificationBuilder
                            .setStyle(
                                NotificationCompat
                                    .BigPictureStyle()
                                    .bigPicture(it)
                                    .bigLargeIcon(null)
                            )
                    }
            }
            notificationBuilder.build()
        }
    }

    private fun showNotification(
        context: Context,
        notification: Notification,
        notificationID: Int
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = context.getString(R.string.default_notification_channel_id)
        val channelName = context.getString(R.string.default_notification_channel_name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.setShowBadge(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(notificationID, notification)
        }

        notificationManager.notify(notificationID, notification)
    }
}