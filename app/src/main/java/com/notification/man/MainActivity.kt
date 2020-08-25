package com.notification.man

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.notificationman.library.NotificationMan
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_fire.setOnClickListener { fireNotificationMan() }
    }

    private fun fireNotificationMan() = NotificationMan
        .Builder(this, "com.notification.man.MainActivity") // make sure class path match with your project architecture
        .setTitle(edit_text_title.text.toString().trim()) // optional
        .setDescription(edit_text_desc.text.toString().trim()) // optional
        .setThumbnailImageUrl("https://storage.googleapis.com/gweb-uniblog-publish-prod/images/Android_robot.max-500x500.png") // optional
        .setTimeInterval(edit_text_time_interval.text.toString().trim().toLong()) // needs secs - default is 5 secs
        .setNotificationType(NotificationMan.NOTIFICATION_TYPE_IMAGE) // optional - default type is TEXT
        .fire()
}