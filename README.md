# melfoam

Melfoam is a deliberately small Android Input Method Service for Bare Launcher. It uses only platform APIs, a Holo theme, and a compact Java-built view (no support libraries, dictionary, network service, or background process).

## Build

Open this repository in Android Studio or run `./gradlew :app:assembleDebug` with Gradle installed. The app targets the legacy Android ICS API level (`minSdk 14`).

Install the APK, open **Melfoam Keyboard**, enable it in Input Method settings, then choose it from the keyboard picker.

## Grammarly

The **Grammarly** toolbar button uses Android's standard `ACTION_PROCESS_TEXT` contract. If a compatible Grammarly/editor activity is installed, Android offers it; otherwise the action safely does nothing. Melfoam does not collect, retain, or upload typed text and does not bundle Grammarly's proprietary SDK.
