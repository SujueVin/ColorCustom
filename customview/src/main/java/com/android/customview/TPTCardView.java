package com.android.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

public class TPTCardView extends RelativeLayout{
   private TextView mTitleTV;
   private SeekBar mSeekBar;
   private TextView mDescriptionTV;
   private StateListDrawable focusBackground;
   private GradientDrawable defaultBg;
   private GradientDrawable focusBg;
   private static final int CORNER_NONE = 0;
   public static final int CORNER_UP = 1;
   public static final int CORNER_DOWN = 2;
   public static final int CORNER_ALL = 3;
   private static final int CORNER = 8;

   public TPTCardView(Context context, AttributeSet attrs) {
      super(context, attrs);
      LayoutInflater.from(context).inflate(R.layout.tpt_cardview, this);
      mTitleTV = findViewById(R.id.title);
      mSeekBar = findViewById(R.id.progress);
      mDescriptionTV = findViewById(R.id.description);

      // 获取xml中配置的属性
      String title;
      int corner;
      String description;
      int max;
      int min;
      Drawable thumb;
      Drawable progressDrawable;
      try (TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.color)) {
         title = typedArray.getString(R.styleable.color_title);
         corner = typedArray.getInt(R.styleable.color_background_corner, 3);
         description = typedArray.getString(R.styleable.color_description);
         min = typedArray.getInt(R.styleable.color_minValue, Integer.MAX_VALUE);
         max = typedArray.getInt(R.styleable.color_maxValue, Integer.MAX_VALUE);
         thumb = typedArray.getDrawable(R.styleable.color_thumb);
         progressDrawable = typedArray.getDrawable(R.styleable.color_progressDrawable);
      }

      //初始化进度条
      if (min != Integer.MAX_VALUE) {
         mSeekBar.setMin(min);
      }
      if (max != Integer.MAX_VALUE) {
         mSeekBar.setMax(max);
      }

      if (thumb != null) {
         mSeekBar.setThumb(thumb);
      }
      if (progressDrawable != null) {
         mSeekBar.setProgressDrawable(progressDrawable);
      }

      mTitleTV.setText(title);
      if (description != null && !description.isEmpty()) {
         mDescriptionTV.setVisibility(VISIBLE);
         mDescriptionTV.setText(description);
      }

      // 设置圆角背景
      defaultBg = new GradientDrawable();
      defaultBg.setShape(GradientDrawable.RECTANGLE);
      defaultBg.setColor(getResources().getColor(R.color.card_bg_default, null));
      focusBg = new GradientDrawable();
      focusBg.setColor(getResources().getColor(R.color.card_bg_selected, null));
      focusBg.setShape(GradientDrawable.RECTANGLE);

      float[] radii = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
      switch (corner) {
         case CORNER_UP: radii = new float[]{CORNER, CORNER, CORNER, CORNER, 0, 0, 0, 0}; break;
         case CORNER_DOWN: radii = new float[]{0, 0, 0, 0, CORNER, CORNER, CORNER, CORNER}; break;
         case CORNER_ALL: radii = new float[]{CORNER, CORNER, CORNER, CORNER, CORNER, CORNER, CORNER, CORNER}; break;
         case CORNER_NONE:
         default: break;
      }
      defaultBg.setCornerRadii(radii);
      focusBg.setCornerRadii(new float[]{CORNER, CORNER, CORNER, CORNER, CORNER, CORNER, CORNER, CORNER});
      // 设置默认背景和获取焦点时的背景
      focusBackground = new StateListDrawable();


      focusBackground.addState(new int[]{android.R.attr.state_focused}, focusBg); // 设置获取焦点时的背景颜色
      focusBackground.addState(new int[]{},defaultBg);
      setBackground(focusBackground);
      setFocusable(true);
      this.setOnKeyListener(new OnKeyListener() {
         @Override
         public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN){
               switch (keyCode){
                  case KeyEvent.KEYCODE_DPAD_LEFT:
                  case KeyEvent.KEYCODE_DPAD_RIGHT:
                     mSeekBar.dispatchKeyEvent(event);
                     return true;
               }
            }
            return false;
         }
      });
   }

   public void startAnimation(boolean hasFocus) {
      if (hasFocus){
         ViewCompat.animate(this).scaleX(1.02f).scaleY(1.05f).start();
      }else {
         ViewCompat.animate(this).scaleX(1.0f).scaleY(1.0f).start();
      }
   }

   

   public void setDescription(String text) {
      mDescriptionTV.setVisibility(VISIBLE);
      mDescriptionTV.setText(text);
   }
   public void setDescription(CharSequence textChar) {
      mDescriptionTV.setVisibility(VISIBLE);
      mDescriptionTV.setText(textChar);
   }

   public void setDescription(int textRes) {
      mDescriptionTV.setVisibility(VISIBLE);
      mDescriptionTV.setText(textRes);
   }

   public void setTextColor(int color) {
      mTitleTV.setTextColor(color);
      mDescriptionTV.setTextColor(color);
   }
   
    public void setBackgroundCorner(int corner) {
       float[] radii = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
       switch (corner) {
          case CORNER_UP: radii = new float[]{CORNER, CORNER, CORNER, CORNER, 0, 0, 0, 0}; break;
          case CORNER_DOWN: radii = new float[]{0, 0, 0, 0, CORNER, CORNER, CORNER, CORNER}; break;
          case CORNER_ALL: radii = new float[]{CORNER, CORNER, CORNER, CORNER, CORNER, CORNER, CORNER, CORNER}; break;
          case CORNER_NONE:
          default: break;
       }
       defaultBg.setCornerRadii(radii);
       focusBackground.addState(new int[]{},defaultBg);
    }

   public void setProgress(int progress) {
      mSeekBar.setProgress(progress);
   }

   public void setMax(int max) {
      mSeekBar.setMax(max);
   }

   public void setMin(int min) {
      mSeekBar.setMin(min);
   }

   public SeekBar getSeekBar() {
      return mSeekBar;
   }

   public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
      mSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
   }

   public void setKeyProgressIncrement(int i) {
      mSeekBar.setKeyProgressIncrement(i);
   }
}
