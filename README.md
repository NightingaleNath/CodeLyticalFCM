[![](https://jitpack.io/v/NightingaleNath/CodeLyticalFCM.svg)](https://jitpack.io/#NightingaleNath/CodeLyticalFCM)
# CodeLyticalFCM

CodeLyticalFCM is a [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging) Android library that demonstrates registering your Android app for notifications and handling the receipt of a message. Example Send Data Message using the HTTP protocol with [Postman](https://www.postman.com/).

## Getting Started

### Step 1

[Add firebase to your Android App](https://firebase.google.com/docs/android/setup)

#### Note: 
After completion of step one, your project should have a google-services.json file added to the root of your project along with the classpath, plugin and dependecies

#### Classpath in project level build.gradle
```
    classpath 'com.google.gms:google-services:latest-version'
```
or latest
```
    id 'com.google.gms.google-services' version 'latest-version' apply false
```
    
#### Plugin in App level build.gradle
```
    id 'com.google.gms.google-services'
```
#### Dependencies
no dependencies required

### Step 2

Add maven repository in project level build.gradle or in latest project setting.gradle file
```
    repositories {
        google()
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
```  

### Step 3

Add CodeLyticalFCM dependencies in App level build.gradle.
```
   dependencies {
	        implementation 'com.github.NightingaleNath:CodeLyticalFCM:1.0.0'
	}
```  


### Step 4

Finally intialize Firebase and setup FCM in application class or in your "MainActivity"

```
    LyticalFCM.setupFCM(this, "YourTopicName")
```

### Step 5

To enable notifications on Android 13 (Tiramisu) and above, you need to request the `POST_NOTIFICATIONS` permission to the AndroidManifest.xml.

```
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
```

### Remove

If you want to stop receiving notification from the subscribed topic simply call.
```
    LyticalFCM.removeFCM("YourTopicName")
```

## Permission Requirements (Android 13+)

To enable notifications on Android 13 (Tiramisu) and above, you need to request the `POST_NOTIFICATIONS` permission. Here's how to do it in your `MainActivity` or `Fragment`:

```
private val requestPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted: Boolean ->
    if (isGranted) {
        // You can now post notifications.
    } else {
        // Inform the user that your app will not be able to show notifications.
    }
}

private fun askNotificationPermission() {
    // This is only necessary for API level >= 33 (TIRAMISU)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // You already have the permission to post notifications.
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            // You should show an explanation to the user on why your app needs this permission.
            // You can display a dialog or a message to explain the importance of the permission.
        } else {
            // Request the permission from the user.
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

```

# For Activity add below to the onCreate
```
askNotificationPermission()
```

# Send Data Message using the HTTP protocol with POSTMAN

### Step 1

You have to copy Legacy Server Key from Firebase Console > Project Settings > Cloud Messaging

#### Note: 

Firebase has upgraded our server keys to a new version. You may continue to use your Legacy server key, but it is recommended that you upgrade to the newest version.
If anyone else is facing any issue then First Enabled "Firebase Cloud Messaging API" from Firebase console, follow the following points

* go to the google cloud platform website
* go to APIs and Services
* go to Enabled APIs & Services
* click + Enable APIs and Services
* search for Firebase In-App Messaging API and make sure it's enabled.

### Step 2

* Open Postman, click on Enter request URL textbox, enter firebase url
```
 https://fcm.googleapis.com/fcm/send
```
* Then change request type to "POST"
* Now to click on Header and add two params "Content-Type" and "Authorization"
```
 Content-Type: application/json
 Authorization: key=AAAAp5XtBPY:APA91bG_fypMd0j... //FCM SERVER KEY
```
* Now click on "Body" than select "Raw" and add value as JSON object like below
```
{
    "to": "/topics/YourTopicName",
    "data": {
        "title": "My Application Titile is Here",
        "short_desc": "My Application Short Description is here",
        "long_desc": "My Application Long Description is here",
        "icon": "https://play-lh.googleusercontent.com/_9o78ciKXmtl1gpvZBdRb1LEbnSA3ZzLNbPHBnBO2TevmKjDglIaGjVBQTwYpSUH5ak=s64-rw",
        "feature": "https://appinventiv.com/wp-content/uploads/sites/1/2018/11/How-to-Get-Your-App-Featured-in-Play-Store-Step-by-Step-Guide.jpg",
        "package": null // can be a package name you want users to go to google store to install eg com.tencent.ig
    }
}
```
### Postman Screen

#### Header

![alt text](https://github.com/NightingaleNath/CodeLyticalFCM/blob/master/Screenshots/postman_screen1.png?raw=true)

#### Body

![alt text](https://github.com/NightingaleNath/CodeLyticalFCM/blob/master/Screenshots/postman_screen2.png?raw=true)

### Note

These Three items are mandatory for notification
* title
* icon
* short_desc

These Three items are optional for notification
* long_desc
* feature
* package (in case of other app promotion)

### 📺 YouTube Channel

[Visit My YouTube Channel](https://www.youtube.com/@codelytical)

Check out video tutorials related to this project on my YouTube channel for more in-depth explanations and demos.


# LICENSE

Copyright 2023 Nathaniel Nkrumah

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

