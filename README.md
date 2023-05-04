# NotificationMan [![](https://jitpack.io/v/theozgurr/NotificationMan.svg)](https://jitpack.io/#theozgurr/NotificationMan)


This library's superpower is firing scheduled local notifications. Even the app is killed.</br></br>
Implementation:</br></br>

<b>Groovy .gradle:</b>

Add it in your root build.gradle at the end of repositories:
</br>

<pre>allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
    }
}</pre> 

Add it in the your app dependencies:
</br>
<pre>dependencies {
  ...
  implementation 'com.github.theozgurr:NotificationMan:1.0.8'
}</pre>
</br>

<b>Kotlin .gradle.kts:</b>


Add it in your root build.gradle.kts at the end of repositories:
</br>

<pre>allprojects {
  repositories {
    ...
    maven(url = "https://jitpack.io")
    }
}</pre> 

Add it in the your app dependencies:
</br>
<pre>dependencies {
  ...
  implementation("com.github.theozgurr:NotificationMan:1.0.8")
}</pre>
</br>

Usage:</br>
<pre>NotificationMan
        .Builder(context = this, classPathWillBeOpen = "com.notification.man.MainActivity") // the activity's path that you want to open when the notification is clicked
        .setTitle(title = "test title") // optional
        .setDescription(desc = "test desc") // optional
        .setThumbnailUrl(thumbnailUrl = "image url") // optional
        .setTimeInterval(timeInterval = 10L) // needs secs - default is 5 secs
        .setNotificationType(type = NotificationTypes.IMAGE.type) // optional - default type is TEXT
        .setNotificationChannelConfig(config = createNotificationManChannelConfig()) // optional
        .fire()</pre> 
        
Canceling the latest added worker in the queue:</br>
<pre>NotificationMan
        .coolDownLatestFire(context)</pre>
        
Canceling all workers:</br>
<pre>NotificationMan
        .coolDownAllFires(context)</pre>
        
You can also set your custom notification channel configuration. This is optional to set:</br>
<pre>NotificationManChannelConfig
        .Builder()
        .setChannelId(id = "notification-man-channel")
        .setChannelName(name = "custom-channel-name")
        .setImportanceLevel(level = NotificationImportanceLevel.HIGH)
        .setShowBadge(shouldShow = true)
        .build()</pre> 

<b>***One important note, with Android13, make sure that your app has the post notification permission to have functionality of the app. You can check requesting the permission [here](https://github.com/10zgurr/NotificationMan/blob/master/app/src/main/java/com/notification/man/MainActivity.kt#L58-L63).***</b>



App is in the foreground:</br>
![](https://media1.giphy.com/media/ciweGllR6JM5e2xE4Y/giphy.gif)</br>
</br>
Exit the app:</br>
![](https://media0.giphy.com/media/JR6RcCu6pbEFNBMKtZ/giphy.gif)</br>
</br>
App is in the background:</br>
![](https://media1.giphy.com/media/RhBhUBYq771pIdHnlv/giphy.gif)</br>
</br>
App is killed:</br>
![](https://media1.giphy.com/media/VFNh8xq0e8VFxdJ3Wa/giphy.gif)</br>
</br>
