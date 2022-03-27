package com.notification.man

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.notification.man.databinding.ActivityMainBinding
import com.notificationman.library.NotificationMan
import com.notificationman.library.model.NotificationTypes
import kotlinx.coroutines.launch

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
            val classPath = "com.notification.man.MainActivity"
            val title = binding.editTextTitle.text.toString().trim()
            val desc = binding.editTextDesc.text.toString().trim()
            val timeInterval = binding.editTextTimeInterval.text.toString().trim().toLongOrNull()
            fireNotificationMan(
                classPath = classPath,
                title = title,
                desc = desc,
                timeInterval = timeInterval
            )
        }

        binding.buttonCoolDownLast.setOnClickListener {
            lifecycleScope.launch {
                NotificationMan.coolDownLatestFire(this@MainActivity)
            }
        }

        binding.buttonCoolDownAll.setOnClickListener {
            lifecycleScope.launch {
                NotificationMan.coolDownAllFires(this@MainActivity)
            }
        }
    }

    private fun fireNotificationMan(
        classPath: String,
        title: String?,
        desc: String?,
        timeInterval: Long?,
    ) {
        NotificationMan
            .Builder(this, classPath)
            .setTitle(title)
            .setDescription(desc)
            .setThumbnailUrl(THUMBNAIL_URL)
            .setTimeInterval(timeInterval)
            .setNotificationType(NotificationTypes.IMAGE.type)
            .fire()
    }
}