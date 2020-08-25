# Notification Man

App is in the foreground:</br>
![](https://media1.giphy.com/media/ciweGllR6JM5e2xE4Y/giphy.gif)</br>
</br>
Exit the app:</br>
![](https://media0.giphy.com/media/JR6RcCu6pbEFNBMKtZ/giphy.gif)</br>
</br>
App is at the background:</br>
![](https://media1.giphy.com/media/RhBhUBYq771pIdHnlv/giphy.gif)</br>
</br>
App is killed:</br>
![](https://media1.giphy.com/media/VFNh8xq0e8VFxdJ3Wa/giphy.gif)</br>
</br>
This library's super power is firing scheduled local notifications. Even the app is killed.
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

Add it in the yoor app dependencies:
</br>
<pre>dependencies {
  implementation 'com.github.theozgurr:NotificationMan:v1.0.2'
}</pre>
</br>
