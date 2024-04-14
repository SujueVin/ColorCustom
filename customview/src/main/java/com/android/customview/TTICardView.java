package com.android.customview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

public class TTICardView extends RelativeLayout{
   private TextView mTitleTV;
   private ImageView mIconIV;
   private TextView mContentTV;
   private boolean isState;
   private StateListDrawable focusBackground;
   private GradientDrawable defaultDg;
   private GradientDrawable focusBg;
   public static final int CORNER_NONE = 0;
   public static final int CORNER_UP = 1;
   public static final int CORNER_DOWN = 2;
   public static final int CORNER_ALL = 3;
   private static final int CORNER = 8;

   public TTICardView(Context context) {
      super(context);
      LayoutInflater.from(context).inflate(R.layout.tti_cardview, this);
      mTitleTV = findViewById(R.id.title);
      mIconIV = findViewById(R.id.icon);
      mContentTV = findViewById(R.id.content);

      // 设置圆角背景
      defaultDg = new GradientDrawable();
      defaultDg.setShape(GradientDrawable.RECTANGLE);
      defaultDg.setColor(getResources().getColor(R.color.card_bg_default, null));
      focusBg = new GradientDrawable();
      focusBg.setColor(getResources().getColor(R.color.card_bg_selected, null));
      focusBg.setShape(GradientDrawable.RECTANGLE);

      float[] radii = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
      defaultDg.setCornerRadii(radii);
      focusBg.setCornerRadii(new float[]{CORNER, CORNER, CORNER, CORNER, CORNER, CORNER, CORNER, CORNER});
      // 设置默认背景和获取焦点时的背景
      focusBackground = new StateListDrawable();


      focusBackground.addState(new int[]{android.R.attr.state_focused}, focusBg); // 设置获取焦点时的背景颜色
      focusBackground.addState(new int[]{},defaultDg);
      setBackground(focusBackground);
      setFocusable(true);
   }

   public TTICardView(Context context, AttributeSet attrs) {
      super(context, attrs);
      LayoutInflater.from(context).inflate(R.layout.tti_cardview, this);
      mTitleTV = findViewById(R.id.title);
      mIconIV = findViewById(R.id.icon);
      mContentTV = findViewById(R.id.content);

      // 获取xml中配置的属性
      Drawable titleIcon;
      Drawable icon;
      String title;
      int corner;
      String content;
      try (TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.color)) {
         titleIcon = typedArray.getDrawable(R.styleable.color_title_icon);
         icon = typedArray.getDrawable(R.styleable.color_icon_src);
         isState = typedArray.getBoolean(R.styleable.color_icon_is_state,false);
         title = typedArray.getString(R.styleable.color_title);
         corner = typedArray.getInt(R.styleable.color_background_corner, 3);
         content = typedArray.getString(R.styleable.color_description);
      }
      if (!isState) {
         mIconIV.setDuplicateParentStateEnabled(true);
      }
      if (titleIcon != null) {
         int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.x26);
         titleIcon.setBounds(0,0,dimensionPixelSize,dimensionPixelSize);
         mTitleTV.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.x10));
         mTitleTV.setCompoundDrawables(titleIcon,null,null,null);

      }
      if (mTitleTV != null) {
         mTitleTV.setText(title);
      }

      if (icon == null) {
         mIconIV.setVisibility(GONE);
      } else {
         mIconIV.setImageDrawable(icon);
      }
      if (content != null && !content.isEmpty()) {
         mContentTV.setVisibility(VISIBLE);
         mContentTV.setText(content);
      }

      // 设置圆角背景
      defaultDg = new GradientDrawable();
      defaultDg.setShape(GradientDrawable.RECTANGLE);
      defaultDg.setColor(getResources().getColor(R.color.card_bg_default, null));
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
      defaultDg.setCornerRadii(radii);
      focusBg.setCornerRadii(new float[]{CORNER, CORNER, CORNER, CORNER, CORNER, CORNER, CORNER, CORNER});
      // 设置默认背景和获取焦点时的背景
      focusBackground = new StateListDrawable();


      focusBackground.addState(new int[]{android.R.attr.state_focused}, focusBg); // 设置获取焦点时的背景颜色
      focusBackground.addState(new int[]{},defaultDg);
      setBackground(focusBackground);
      setFocusable(true);
   }

   public void startAnimation(boolean hasFocus) {
      if (hasFocus){
         ViewCompat.animate(this).scaleX(1.02f).scaleY(1.05f).start();
         if (!isState) {
            mIconIV.setSelected(true);
         }
      }else {
         ViewCompat.animate(this).scaleX(1.0f).scaleY(1.0f).start();
         if (!isState) {
            mIconIV.setSelected(false);
         }
      }
   }

   public void startAnimation(boolean hasFocus, int selectedColor, int defaultColor) {
      if (hasFocus){
         ViewCompat.animate(this).scaleX(1.02f).scaleY(1.05f).start();
         if (!isState) {
            mIconIV.setSelected(true);
         }
         mTitleTV.setTextColor(getResources().getColor(selectedColor, null));
         mContentTV.setTextColor(getResources().getColor(selectedColor, null));
      }else {
         ViewCompat.animate(this).scaleX(1.0f).scaleY(1.0f).start();
         if (!isState) {
            mIconIV.setSelected(false);
         }
         mTitleTV.setTextColor(getResources().getColor(defaultColor, null));
         mContentTV.setTextColor(getResources().getColor(defaultColor, null));
      }
   }

   @Override
   public void setSelected(boolean selected) {
      if (mIconIV.getVisibility() == View.VISIBLE) {
         mIconIV.setSelected(selected);
         //super.setSelected(selected); 加上会导致闪烁
      } else {
         super.setSelected(selected);
      }
   }

   @Override
   public boolean isSelected() {
      return mIconIV.isSelected();
   }
   public void setState(Boolean isState) {
      this.isState = isState;
   }
   public void setTitle(String title) {
      mTitleTV.setText(title);
   }

   public void setDescription(String text) {
      mContentTV.setVisibility(VISIBLE);
      mContentTV.setText(text);
   }
   public void setDescription(CharSequence textChar) {
      mContentTV.setVisibility(VISIBLE);
      mContentTV.setText(textChar);
   }

   public void setDescription(int textRes) {
      mContentTV.setVisibility(VISIBLE);
      mContentTV.setText(textRes);
   }

   public void setTextColor(int color) {
      mTitleTV.setTextColor(color);
      mContentTV.setTextColor(color);
   }

   public void setIconResource(int icon) {
      mIconIV.setImageResource(icon);
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
       defaultDg.setCornerRadii(radii);
       focusBackground.addState(new int[]{},defaultDg);
    }

    public void setTitleText(String string) {
      mTitleTV.setText(string);
    }
}
