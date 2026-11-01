# Melfoam Keyboard

Melfoam is a compact Android input method with a lightweight Holo-style interface. It uses platform widgets and has no third-party runtime dependencies.

## Compatibility

- Compile SDK: 35
- Target SDK: 35
- Minimum SDK: 21
- Java: 17

## Build

Open the project in Android Studio with Android Gradle Plugin 8.5.x and Gradle 8.7, or run:

```sh
./gradlew assembleDebug
```

Install the APK, open **Melfoam Keyboard**, enable it in Android's input method settings, then choose it from the keyboard picker.

## Keyboard actions

- Letter keys support one-shot Shift and a `?123` symbols layout.
- Backspace deletes a selection or one Unicode character (including surrogate-pair characters).
- Enter uses the active editor action when one is configured, otherwise it sends a complete Enter key event.
- The Grammarly/text-tools key offers selected text to installed Android `ACTION_PROCESS_TEXT` handlers. You must select text first and choose a compatible app. Melfoam does not upload text or transmit it automatically; the receiving app controls processing and any replacement workflow.

## Privacy

Melfoam does not maintain a text history, dictionary, or network connection. Keystrokes are committed directly to the focused editor. Selected text is shared with another app only after you tap the text-tools key and choose a handler.
