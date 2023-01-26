# NotificationMan [![](https://jitpack.io/v/theozgurr/NotificationMan.svg)](https://jitpack.io/#theozgurr/NotificationMan)


This library's superpower is firing scheduled local notifications. Even the app is killed.
You can easily implement it to your android project:</br>
</br>
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
  implementation 'com.github.theozgurr:NotificationMan:1.0.8'
}</pre>
</br>

Firing:</br>
<pre>NotificationMan
        .Builder(context, "the activity's path that you want to open when the notification is clicked")
        .setTitle("test title") // optional
        .setDescription("test desc") // optional
        .setThumbnailUrl("image url") // optional
        .setTimeInterval(10L) // needs secs - default is 5 secs
        .setNotificationType(NotificationTypes.IMAGE.type) // optional - default type is TEXT
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

<b>***One important note, with Android13, make sure that your app has the post notification permission to have full functionality of the app. You can check example of requesting the permission in this project.***</b>



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
