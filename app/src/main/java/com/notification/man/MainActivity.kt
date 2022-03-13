package com.notification.man

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.notification.man.databinding.ActivityMainBinding
import com.notificationman.library.NotificationMan
import com.notificationman.library.NotificationTypes

class MainActivity : AppCompatActivity() {

    companion object {
        private const val THUMBNAIL_URL =
            "https://storage.googleapis.com/gweb-uniblog-publish-prod/images/Android_robot.max-500x500.png"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonFire.setOnClickListener {
            fireNotificationMan()
        }
    }

    private fun fireNotificationMan() {
        val classPath = "com.notification.man.MainActivity" // make sure class path match with your project architecture
        val title = binding.editTextTitle.text.toString().trim()
        val desc = binding.editTextDesc.text.toString().trim()
        val timeInterval = binding.editTextTimeInterval.text.toString().trim().toLong()
        NotificationMan
            .Builder(this, classPath)
            .setTitle(title) // optional
            .setDescription(desc) // optional
            .setThumbnailUrl(THUMBNAIL_URL) // optional
            .setTimeInterval(timeInterval) // needs secs - default is 5 secs
            .setNotificationType(NotificationTypes.IMAGE.type) // optional - default type is TEXT
            .fire()
    }
}