package com.anakinbrownridge.melfoam;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/** Lightweight platform-widget keyboard; no third-party runtime dependencies. */
final class KeyboardView extends LinearLayout {
    interface Listener {
        void key(String value);
        void backspace();
        void enter();
        void space();
        void action();
    }

    private final Listener listener;
    private boolean shifted;
    private boolean symbols;
    private Button shiftButton;
    private final Button[][] letterButtons = new Button[3][];

    KeyboardView(Context context, Listener listener) {
        super(context);
        this.listener = listener;
        setOrientation(VERTICAL);
        setPadding(dp(3), dp(4), dp(3), dp(4));
        setBackgroundColor(Color.rgb(24, 24, 24));
        build();
    }

    private void build() {
        removeAllViews();
        if (!symbols) {
            addLetterRow("qwertyuiop", 0);
            addLetterRow("asdfghjkl", 1);
            addLetterRow("zxcvbnm", 2);
        } else {
            addTextRow("1234567890");
            addTextRow("@#$%&*-+()");
            addTextRow("!\"':;/?");
        }
        addControls();
    }

    private void addLetterRow(String letters, int rowIndex) {
        LinearLayout row = row();
        letterButtons[rowIndex] = new Button[letters.length()];
        if (rowIndex == 2) {
            shiftButton = addButton(row, shifted ? "⇧" : "↑", 1.25f, () -> {
                shifted = !shifted;
                updateLetters();
            });
        }
        for (int i = 0; i < letters.length(); i++) {
            final String letter = String.valueOf(letters.charAt(i));
            Button key = addButton(row, shifted ? letter.toUpperCase() : letter, 1f, () -> {
                listener.key(shifted ? letter.toUpperCase() : letter);
                if (shifted) {
                    shifted = false;
                    updateLetters();
                }
            });
            letterButtons[rowIndex][i] = key;
        }
        if (rowIndex == 2) {
            addButton(row, "⌫", 1.25f, listener::backspace);
        }
        addView(row);
    }

    private void addTextRow(String keys) {
        LinearLayout row = row();
        for (int i = 0; i < keys.length(); i++) {
            final String value = String.valueOf(keys.charAt(i));
            addButton(row, value, 1f, () -> listener.key(value));
        }
        addView(row);
    }

    private void addControls() {
        LinearLayout row = row();
        addButton(row, symbols ? "ABC" : "?123", 1.45f, () -> {
            symbols = !symbols;
            shifted = false;
            build();
        });
        addButton(row, ",", 0.8f, () -> listener.key(","));
        addButton(row, "Grammarly", 2.1f, listener::action);
        addButton(row, "space", 3.2f, listener::space);
        addButton(row, ".", 0.8f, () -> listener.key("."));
        addButton(row, "↵", 1.2f, listener::enter);
        addView(row);
    }

    private void updateLetters() {
        if (letterButtons[0] == null) return;
        String[] rows = {"qwertyuiop", "asdfghjkl", "zxcvbnm"};
        for (int r = 0; r < rows.length; r++) {
            if (letterButtons[r] == null) continue;
            for (int i = 0; i < rows[r].length(); i++) {
                String value = String.valueOf(rows[r].charAt(i));
                letterButtons[r][i].setText(shifted ? value.toUpperCase() : value);
            }
        }
        if (shiftButton != null) shiftButton.setText(shifted ? "⇧" : "↑");
    }

    private LinearLayout row() {
        LinearLayout row = new LinearLayout(getContext());
        row.setGravity(Gravity.CENTER);
        row.setOrientation(HORIZONTAL);
        row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
        return row;
    }

    private Button addButton(LinearLayout row, String text, float weight, Runnable action) {
        Button button = new Button(getContext());
        button.setText(text);
        button.setTextSize(text.length() > 3 ? 10 : 16);
        button.setAllCaps(false);
        button.setSingleLine(true);
        button.setMinWidth(0);
        button.setMinHeight(0);
        button.setTextColor(Color.WHITE);
        button.setPadding(dp(1), 0, dp(1), 0);
        button.setOnClickListener(v -> action.run());
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.rgb(55, 55, 55));
        background.setCornerRadius(dp(4));
        background.setStroke(dp(1), Color.rgb(100, 100, 100));
        button.setBackground(background);
        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, weight);
        params.setMargins(dp(1), dp(2), dp(1), dp(2));
        row.addView(button, params);
        return button;
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
