# Notification Man

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
  implementation 'com.github.theozgurr:NotificationMan:v1.0.2'
}</pre>
</br>

Usage:</br>

<pre>NotificationMan
        .Builder(context, "the activity's path that you want to open when the notification is clicked")
        .setTitle("test title") // optional
        .setDescription("test desc") // optional
        .setThumbnailImageUrl("image url") // optional
        .setTimeInterval(10L) // needs secs - default is 5 secs
        .setNotificationType(NotificationMan.NOTIFICATION_TYPE_IMAGE) // optional - default type is TEXT
        .fire()</pre> 



The app is in the foreground:</br>
![](https://media1.giphy.com/media/ciweGllR6JM5e2xE4Y/giphy.gif)</br>
</br>
Exit the app:</br>
![](https://media0.giphy.com/media/JR6RcCu6pbEFNBMKtZ/giphy.gif)</br>
</br>
The app is in the background:</br>
![](https://media1.giphy.com/media/RhBhUBYq771pIdHnlv/giphy.gif)</br>
</br>
The app is killed:</br>
![](https://media1.giphy.com/media/VFNh8xq0e8VFxdJ3Wa/giphy.gif)</br>
</br>
