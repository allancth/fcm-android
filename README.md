# fcm-android
Basic Android application to demonstrates the exchange of messages with 
Firebase Cloud Messaging.

See also [fcm-camel](https://github.com/allancth/fcm-camel).

## Pre-requisites
1. google-services.json
2. FCM sender ID
3. Android SDK

## Configuration
1. Edit [build.gradle](https://github.com/allancth/fcm-android/blob/master/build.gradle) and replace the necessary values.
2. Edit [google-services.json](https://support.google.com/firebase/answer/7015592?hl=en#android) with necessary credentials.
3. Edit [local.properties](https://github.com/allancth/fcm-android/blob/master/local.properties) and point _sdk.dir_

## Build
    gradle build
