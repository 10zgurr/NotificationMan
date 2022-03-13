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
  implementation 'com.github.theozgurr:NotificationMan:v1.0.4'
}</pre>
</br>

Usage:</br>

<pre>NotificationMan
        .Builder(context, "the activity's path that you want to open when the notification is clicked")
        .setTitle("test title") // optional
        .setDescription("test desc") // optional
        .setThumbnailUrl("image url") // optional
        .setTimeInterval(10L) // needs secs - default is 5 secs
        .setNotificationType(NotificationTypes.IMAGE.type) // optional - default type is TEXT
        .fire()</pre> 



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
