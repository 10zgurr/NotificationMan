package com.notificationman.library.worker

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.notificationman.library.NotificationMan
import com.notificationman.library.R
import com.notificationman.library.utils.BitmapUtility

class LocalNotificationShowWorker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        private val TAG = LocalNotificationShowWorker::class.java.simpleName
    }

    override fun doWork(): Result {
        return try {
            val notificationID = System.currentTimeMillis().toInt()
            val classPath = inputData.getString(LocalNotificationPostWorker.NOTIFICATION_CLASS_PATH_KEY)
            val title = inputData.getString(LocalNotificationPostWorker.NOTIFICATION_TITLE_KEY)
            val desc = inputData.getString(LocalNotificationPostWorker.NOTIFICATION_DESC_KEY)
            val thumbnailImageUrl = inputData.getString(LocalNotificationPostWorker.NOTIFICATION_THUMBNAIL_IMAGE_KEY)
            val type = inputData.getInt(LocalNotificationPostWorker.NOTIFICATION_TYPE_KEY, NotificationMan.NOTIFICATION_TYPE_TEXT)
            val notification = getNotification(context, classPath!!, title, desc, thumbnailImageUrl, notificationID, type)
            Log.d(TAG, "created local notification -> $notification")
            showNotification(context, notification, notificationID)
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "local notification show worker has failed: ${e.message}")
            Result.failure()
        }
    }

    private fun getNotification(context: Context, classPath: String, title: String?, body: String?, thumbnailImageUrl: String?, notificationID: Int, notificationType: Int): Notification {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            component = ComponentName(context.packageName, classPath)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(context, notificationID, intent, PendingIntent.FLAG_ONE_SHOT)

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
            .setPriority(Notification.PRIORITY_MAX)
        if (Build.VERSION.SDK_INT >= 21)
            notificationBuilder.setVibrate(longArrayOf(0))

        return if (thumbnailImageUrl.isNullOrEmpty()) {
            notificationBuilder
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .build()
        } else {
            val bitmap = BitmapUtility.getBitmapFromURL(thumbnailImageUrl)
            val roundedBitmap = bitmap?.let { BitmapUtility.getCroppedBitmap(it) }
            roundedBitmap?.let { notificationBuilder.setLargeIcon(it) }
            when (notificationType) {
                NotificationMan.NOTIFICATION_TYPE_TEXT -> notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(body))
                NotificationMan.NOTIFICATION_TYPE_IMAGE -> roundedBitmap?.let { notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(it).bigLargeIcon(null)) }
            }
            notificationBuilder.build()
        }
    }

    private fun showNotification(context: Context, notification: Notification, notificationID: Int) {
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