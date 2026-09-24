package com.anakinbrownridge.melfoam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Small, dependency-free setup screen. The keyboard itself does not need an Activity. */
public final class SettingsActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(20, 20, 20, 20);

        TextView title = new TextView(this);
        title.setText("Melfoam Keyboard\n\nA small Holo keyboard for Bare Launcher.");
        title.setTextSize(18);
        box.addView(title, new LinearLayout.LayoutParams(-1, -2));

        Button enable = new Button(this);
        enable.setText("Enable keyboard");
        enable.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
            }
        });
        box.addView(enable, new LinearLayout.LayoutParams(-1, -2));

        Button picker = new Button(this);
        picker.setText("Choose Melfoam");
        picker.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ((android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showInputMethodPicker();
            }
        });
        box.addView(picker, new LinearLayout.LayoutParams(-1, -2));

        TextView note = new TextView(this);
        note.setText("Grammarly integration is optional. If Grammarly is installed, use its editor action from the keyboard toolbar; no text is sent by Melfoam automatically.");
        note.setPadding(0, 18, 0, 0);
        box.addView(note, new LinearLayout.LayoutParams(-1, -2));
        setContentView(box);
    }
}
