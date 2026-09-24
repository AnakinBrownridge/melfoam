package com.anakinbrownridge.melfoam;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

/** Builds the keyboard using platform widgets so there is no layout/runtime dependency. */
final class KeyboardView extends LinearLayout {
    interface Listener { void key(String value); void backspace(); void enter(); void space(); void action(); }
    private final Listener listener;
    private boolean shifted;

    KeyboardView(Context context, Listener listener) {
        super(context);
        this.listener = listener;
        setOrientation(VERTICAL);
        setPadding(2, 2, 2, 2);
        setBackgroundColor(Color.rgb(24, 24, 24));
        addRow("qwertyuiop");
        addRow("asdfghjkl");
        addBottomRow();
    }

    private void addRow(String letters) {
        LinearLayout row = row();
        for (int i = 0; i < letters.length(); i++) addLetter(row, String.valueOf(letters.charAt(i)));
        addView(row);
    }

    private void addBottomRow() {
        LinearLayout row = row();
        addButton(row, "↑", new View.OnClickListener() { public void onClick(View v) { shifted = !shifted; } }, 1.2f);
        addLetter(row, "z"); addLetter(row, "x"); addLetter(row, "c"); addLetter(row, "v"); addLetter(row, "b"); addLetter(row, "n"); addLetter(row, "m");
        addButton(row, "⌫", new View.OnClickListener() { public void onClick(View v) { listener.backspace(); } }, 1.2f);
        addView(row);
        LinearLayout controls = row();
        addButton(controls, ",", new View.OnClickListener() { public void onClick(View v) { listener.key(","); } }, 1);
        addButton(controls, "Grammarly", new View.OnClickListener() { public void onClick(View v) { listener.action(); } }, 2.2f);
        addButton(controls, "space", new View.OnClickListener() { public void onClick(View v) { listener.space(); } }, 3.1f);
        addButton(controls, ".", new View.OnClickListener() { public void onClick(View v) { listener.key("."); } }, 1);
        addButton(controls, "↵", new View.OnClickListener() { public void onClick(View v) { listener.enter(); } }, 1.2f);
        addView(controls);
    }

    private void addLetter(LinearLayout row, final String letter) {
        addButton(row, letter, new View.OnClickListener() { public void onClick(View v) { listener.key(shifted ? letter.toUpperCase() : letter); if (shifted) shifted = false; } }, 1);
    }
    private LinearLayout row() { LinearLayout r = new LinearLayout(getContext()); r.setGravity(Gravity.CENTER); r.setWeightSum(10); r.setLayoutParams(new LayoutParams(-1, 0, 1)); return r; }
    private void addButton(LinearLayout row, String text, OnClickListener click, float weight) {
        Button b = new Button(getContext()); b.setText(text); b.setTextSize(text.length() > 1 ? 11 : 16); b.setAllCaps(false); b.setTextColor(Color.WHITE); b.setOnClickListener(click);
        GradientDrawable bg = new GradientDrawable(); bg.setColor(Color.rgb(55,55,55)); bg.setStroke(1, Color.rgb(100,100,100)); b.setBackgroundDrawable(bg);
        row.addView(b, new LinearLayout.LayoutParams(0, -1, weight));
    }
}
