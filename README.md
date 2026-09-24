# melfoam

Melfoam is a compact Android keyboard designed for Bare Launcher and built to stay light on memory while still using a modern Android SDK target. The UI intentionally keeps the classic Holo look from the ICS-era without depending on heavy support libraries or a large background service.

## Compatibility

- Compile SDK: 35
- Target SDK: 35
- Min SDK: 21
- Visual style: Holo Light

## Build

Open this repository in Android Studio with the Android Gradle Plugin 8.5.x or use Gradle 8.7+ to build the app.

Install the APK, open Melfoam Keyboard, enable the keyboard in system input settings, and select it from the keyboard chooser.

## Grammarly

The Grammarly button uses the Android `ACTION_PROCESS_TEXT` intent when a text editor or Grammarly-compatible app is available. Melfoam does not embed Grammarly's SDK or send text automatically.
