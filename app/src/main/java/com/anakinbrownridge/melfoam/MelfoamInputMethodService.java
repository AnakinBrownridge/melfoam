package com.anakinbrownridge.melfoam;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.InputMethodService;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/** Minimal IME: it keeps no dictionary or process-wide text buffer. */
public final class MelfoamInputMethodService extends InputMethodService implements KeyboardView.Listener {
    private KeyboardView keyboard;
    private Vibrator vibrator;

    @Override public void onCreate() { super.onCreate(); vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); }
    @Override public View onCreateInputView() { keyboard = new KeyboardView(this, this); return keyboard; }
    private InputConnection connection() { return getCurrentInputConnection(); }
    private void tap() { if (vibrator != null) vibrator.vibrate(8); }
    @Override public void key(String value) { tap(); InputConnection c = connection(); if (c != null) c.commitText(value, 1); }
    @Override public void space() { key(" "); }
    @Override public void enter() { tap(); InputConnection c = connection(); if (c != null) c.sendKeyEvent(new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER)); }
    @Override public void backspace() { tap(); InputConnection c = connection(); if (c != null) c.deleteSurroundingText(1, 0); }

    /** Opens Grammarly's installed editor when available; never uploads text itself. */
    @Override public void action() {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_PROCESS_TEXT);
        intent.setType("text/plain"); intent.putExtra(Intent.EXTRA_PROCESS_TEXT, ""); intent.putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);
        if (intent.resolveActivity(pm) != null) {
            try { startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); } catch (Exception ignored) { }
        }
    }
}
