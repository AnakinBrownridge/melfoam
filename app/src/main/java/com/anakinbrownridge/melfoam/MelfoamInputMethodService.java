package com.anakinbrownridge.melfoam;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

/** Minimal IME: text is committed directly to the active editor, not retained by the app. */
public final class MelfoamInputMethodService extends InputMethodService implements KeyboardView.Listener {
    private Vibrator vibrator;

    @Override public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override public View onCreateInputView() {
        return new KeyboardView(this, this);
    }

    private InputConnection connection() { return getCurrentInputConnection(); }

    private void tap() {
        if (vibrator == null || !vibrator.hasVibrator()) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(8, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //noinspection deprecation
            vibrator.vibrate(8);
        }
    }

    @Override public void key(String value) {
        tap();
        InputConnection c = connection();
        if (c != null) c.commitText(value, 1);
    }

    @Override public void space() { key(" "); }

    @Override public void enter() {
        tap();
        InputConnection c = connection();
        if (c == null) return;
        EditorInfo info = getCurrentInputEditorInfo();
        int action = info == null ? EditorInfo.IME_ACTION_NONE
                : (info.actionId != 0 ? info.actionId : (info.imeOptions & EditorInfo.IME_MASK_ACTION));
        if (info != null && action != EditorInfo.IME_ACTION_NONE
                && (info.imeOptions & EditorInfo.IME_FLAG_NO_ENTER_ACTION) == 0) {
            c.performEditorAction(action);
        } else {
            c.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            c.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
        }
    }

    @Override public void backspace() {
        tap();
        InputConnection c = connection();
        if (c == null) return;
        CharSequence selected = c.getSelectedText(0);
        if (selected != null && selected.length() > 0) {
            c.commitText("", 1);
            return;
        }
        CharSequence before = c.getTextBeforeCursor(2, 0);
        if (before != null && before.length() > 0) {
            int deleteLength = Character.isLowSurrogate(before.charAt(before.length() - 1))
                    && before.length() > 1 && Character.isHighSurrogate(before.charAt(before.length() - 2)) ? 2 : 1;
            c.deleteSurroundingText(deleteLength, 0);
        }
    }

    /** Offers selected text to compatible text-processing apps; Melfoam never sends it silently. */
    @Override public void action() {
        InputConnection c = connection();
        if (c == null) return;
        CharSequence selected = c.getSelectedText(0);
        if (selected == null || selected.length() == 0) {
            Toast.makeText(this, "Select text first to use a text editor action.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent processText = new Intent(Intent.ACTION_PROCESS_TEXT);
        processText.setType("text/plain");
        processText.putExtra(Intent.EXTRA_PROCESS_TEXT, selected.toString());
        processText.putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);
        Intent chooser = Intent.createChooser(processText, "Process selected text");
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(chooser);
        } catch (Exception e) {
            Toast.makeText(this, "No compatible text-processing app is available.", Toast.LENGTH_SHORT).show();
        }
    }
}
