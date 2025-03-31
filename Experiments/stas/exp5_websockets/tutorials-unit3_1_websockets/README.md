# websocket_unit3_1

### What is this example

Simple Implementation of websocket - Chat room.
For this example, the user has three options to connect to the public chat room
1. Open the index.html file in `Client_JS` folder
2. Run the adroid app in `WebSocketAndorid-singleton-approach` or `WebSocketAndorid-service-approach` folder
3. Connect to `ws://localhost:8080/chat/{username}` in Postman

There are two different apps in this folder:
1. WebSocketAndorid-singleton-approach :

		A Singleton Solution for WebSockets maintains an open connection through a globally available instance, thereby enhancing simplicity and performance at the operational stage of the application. This solution results in disconnection upon closure or minimization of the application and therefore is less appropriate for establishing persistent connections.

2. WebSocketAndorid-service-approach

		A Service Approach operates the WebSocket connection in a background service, thereby keeping it alive even when the app is not in view. This arrangement is quite useful for real-time apps.


### Important Notes

- To run the server on your local machine to test the public chat room, you can
	- Run the server in IntelliJ normally, OR
	- Execute the command 'java -jar WebSocketServer.jar' in the terminal to run the included .jar server (for frontend students)

- Use `10.0.2.2` instead of `localhost` IF the server program is running on the same host as the Android Enmulator

- `WebSocketAndroid-signleton-approach` and `WebSocketAndroid-service-approach` are alternatives, use accordingly.

Read the Websockets.pdf to understand the inner workings of the frontend and backend for this public chat room (only for concepts, ignore examples as they may be out-dated).

### Dependencies and Configurations

#### Backend

- pom.xml:
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

#### Frontend

- AndroidManifest.xml
    - add `<uses-permission android:name="android.permission.INTERNET" />` before `<application>`
    - add `android:usesCleartextTraffic="true"` inside `<application>`

- build.gradle (Module :app)
    - add `implementation 'org.java-websocket:Java-WebSocket:1.5.1'` inside `dependencies{}`, then sync gradle.

### Version Tested
|Android Studio            | Android SDK | Gradle | Gradle Plugin | Gradle JDK |                    Emulator                     |
|--------------------------|-------------|--------|---------------|------------|-------------------------------------------------|
|Ladybug 2024.2.2 Patch 1  |  35.3.12    | 8.10.2 |    8.8.1      |    21      | Pixel 3a API 34, Pixel 5 API 34, Pixel 7 API 32 |


|IntelliJ  | Project SDK | Springboot | Maven |
|----------|-------------|------------|-------|
|2023.2.2  |     17      | 3.1.4      | 3.6.3 |


Last Modified by : Dhvani Mistry
Last Modified on : March 9 2025