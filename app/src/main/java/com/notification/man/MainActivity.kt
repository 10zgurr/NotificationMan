package com.notification.man

import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.notification.man.databinding.ActivityMainBinding
import com.notificationman.library.NotificationMan
import com.notificationman.library.config.NotificationManChannelConfig
import com.notificationman.library.model.NotificationImportanceLevel
import com.notificationman.library.model.NotificationTypes
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val THUMBNAIL_URL =
            "https://storage.googleapis.com/gweb-uniblog-publish-prod/images/Android_robot.max-500x500.png"
    }

    private lateinit var binding: ActivityMainBinding

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()

        binding.buttonFire.setOnClickListener {
            val title = binding.editTextTitle.text.toString().trim()
            val desc = binding.editTextDesc.text.toString().trim()
            val timeInterval = binding.editTextTimeInterval.text.toString().trim().toLongOrNull()
            fireNotificationMan(
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

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            notificationPermissionLauncher.launch(
                android.Manifest.permission.POST_NOTIFICATIONS
            )
    }

    private fun fireNotificationMan(
        title: String?,
        desc: String?,
        timeInterval: Long?,
    ) {
        NotificationMan
            .Builder(context = this, classPathWillBeOpen = "com.notification.man.MainActivity")
            .setTitle(title = title)
            .setDescription(desc = desc)
            .setThumbnailUrl(thumbnailUrl = THUMBNAIL_URL)
            .setTimeInterval(timeInterval = timeInterval)
            .setNotificationType(type = NotificationTypes.IMAGE.type)
            .setNotificationChannelConfig(config = createNotificationManChannelConfig())
            .fire()
    }

    private fun createNotificationManChannelConfig() =
        NotificationManChannelConfig
            .Builder()
            .setChannelId(id = "notification-man-channel")
            .setChannelName(name = "custom-channel-name")
            .setImportanceLevel(level = NotificationImportanceLevel.HIGH)
            .setShowBadge(shouldShow = true)
            .build()
}