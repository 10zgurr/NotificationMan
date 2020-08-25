package com.notification.man

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.notificationman.library.NotificationMan

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fireNotificationMan()
    }

    private fun fireNotificationMan() = NotificationMan
        .Builder(this, "com.notification.man.MainActivity") // make sure class path match with your project architecture
        .setTitle("test title") // optional
        .setDescription("test desc") // optional
        .setThumbnailImageUrl("https://digit-1.com/wp-content/uploads/sites/13/2019/10/Digit-1-News-How-to-enable-the-battery-percentage-icon-in.jpg") // optional
        .setTimeInterval(4L) // needs secs - default is 5 secs
        .setNotificationType(NotificationMan.NOTIFICATION_TYPE_IMAGE) // optional - default type is TEXT
        .fire()
}