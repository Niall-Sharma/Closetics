# android_unit2_2

### What is this example

A Volley example that contains the following example requests:

1. String request
StringRequest can be used to fetch any kind of string data. The response can be json, xml, html,text.

2. JsonObject request
Use JsonObjectRequest if you are expecting json object in the response.
**also parse JSON Object in response**

3. JsonArray request
Use JsonArrayRequest if you are expecting an array of json objects in the response.
**also populate ListView from JSON Array in response**

4. Image request
Download an bitmap image and display

### Important Notes

- Use `10.0.2.2` instead of `localhost` IF the server program is running on the same host as the Android Enmulator

- AndroidManifest.xml
    - add `<uses-permission android:name="android.permission.INTERNET" />` before `<application>`
    - add `android:usesCleartextTraffic="true"` inside `<application>`

- build.gradle (Module :app)
    - add `implementation 'com.android.volley:volley:1.2.1'` inside `dependencies{}`, then sync gradle.

- files for list_view:
    - add `ListAdapter.java`, `ListItemObject.java`
    - add `list_item.xml` under `res.layout`

### Version Tested
|Android Studio            | Android SDK | Gradle | Gradle Plugin | Gradle JDK | Emulator |
|--------------------------|-------------|--------|---------------|------------|----------|
|Giraffe 2022.3.1 Patch 2  |     33      | 8.0.0  |    8.0.0      |    17      | Pixel 3a |



