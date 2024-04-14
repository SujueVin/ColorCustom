package com.android.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

public class NavigationView extends LinearLayout {
    private TextView mLast;
    private TextView mCurrent;
    private TextView mThird;
    View.OnFocusChangeListener listener = (v, hasFocus) -> {
        if (hasFocus) {
            final String newText = "  " + ((TextView) v).getText().toString() + "  ";
            ((TextView) v).setText(newText);
            ViewCompat.animate(v).scaleX(1.1f).scaleY(1.1f).start();
            v.setSelected(true);
        } else {
            final String newText = ((TextView) v).getText().toString().trim();
            ((TextView) v).setText(newText);
            ViewCompat.animate(v).scaleX(1.0f).scaleY(1.0f).start();
            v.setSelected(false);
        }
    };

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.navigation_view, this);
        mLast = findViewById(R.id.last);
        mCurrent = findViewById(R.id.current);
        mThird = findViewById(R.id.third);
        String last, current;
        try (TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.color)) {
            last = typedArray.getString(R.styleable.color_last);
            current = typedArray.getString(R.styleable.color_current);
        }
        if (last != null && !last.isEmpty()) {
            mLast.setText(last);
        }
        if (current != null && !current.isEmpty()) {
            mCurrent.setText(current);
        }
        mLast.setOnFocusChangeListener(listener);
        mCurrent.setOnFocusChangeListener(listener);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mLast.setOnClickListener(l);
    }

    public void setLast(String last) {
        mLast.setText(last);
    }

    public void setCurrent(String current) {
        mCurrent.setText(current);
    }
    public void setThird(String third) {
        findViewById(R.id.img2).setVisibility(VISIBLE);
        mThird.setVisibility(VISIBLE);
        mThird.setText(third);
    }

    public void getFocus() {
        mLast.requestFocus();
    }

    public void setCurrentClickListener(OnClickListener onClickListener) {
        mCurrent.setOnClickListener(onClickListener);
    }
}
