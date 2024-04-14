package com.android.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

public class TITCardView extends RelativeLayout{
   private TextView mTitleTV;
   private boolean titleIconIsState;
   private boolean iconIsState;
   private ImageView mIconIV;
   private TextView mDescriptionTV;
   private StateListDrawable focusBackground = new StateListDrawable();
   private GradientDrawable defaultBg;
   private GradientDrawable focusBg;
   private static final int CORNER_NONE = 0;
   public static final int CORNER_UP = 1;
   public static final int CORNER_DOWN = 2;
   public static final int CORNER_ALL = 3;
   private static final int CORNER = 8;

   public static final int ICON_CENTER_VERTICAL = 0;
   public static final int ICON_HEIGHT_MATCH_PARENT = 1;
   public static final int ICON_ALIGN_TITLE = 2;

   public TITCardView(Context context, AttributeSet attrs) {
      super(context, attrs);
      // 获取xml中配置的属性
      Drawable titleIcon;
      Drawable icon;
      String title;
      int corner;
      String description;
      Boolean descriptionSingleLine;
      int xmlType;
      try (TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.color)) {
         titleIcon = typedArray.getDrawable(R.styleable.color_title_icon);
         icon = typedArray.getDrawable(R.styleable.color_icon_src);
         iconIsState = typedArray.getBoolean(R.styleable.color_icon_is_state,false);
         titleIconIsState = typedArray.getBoolean(R.styleable.color_title_icon_is_state,false);
         title = typedArray.getString(R.styleable.color_title);
         corner = typedArray.getInt(R.styleable.color_background_corner, CORNER_ALL);
         description = typedArray.getString(R.styleable.color_description);
         descriptionSingleLine = typedArray.getBoolean(R.styleable.color_description_singleLine,false);
         xmlType = typedArray.getInt(R.styleable.color_xml_type,ICON_CENTER_VERTICAL);
      }
      if (xmlType == ICON_HEIGHT_MATCH_PARENT) {
         LayoutInflater.from(context).inflate(R.layout.tit1_cardview, this);
      } else if (xmlType == ICON_ALIGN_TITLE) {
         LayoutInflater.from(context).inflate(R.layout.itit_cardview, this);
      } else {
         LayoutInflater.from(context).inflate(R.layout.tit_cardview, this);
      }
      mTitleTV = findViewById(R.id.title);
      mIconIV = findViewById(R.id.icon);
      mDescriptionTV = findViewById(R.id.description);

      mTitleTV.setText(title);
      if (titleIcon != null) {
         int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.x26);
         titleIcon.setBounds(0,0,dimensionPixelSize,dimensionPixelSize);
         mTitleTV.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.x10));
         mTitleTV.setCompoundDrawables(titleIcon,null,null,null);
      }

      if (icon == null) {
         mIconIV.setVisibility(GONE);
      } else {
         mIconIV.setImageDrawable(icon);
      }
      if (description == null || description.isEmpty()) {
         mDescriptionTV.setVisibility(GONE);
      } else {
         mDescriptionTV.setVisibility(VISIBLE);
         mDescriptionTV.setText(description);
         mDescriptionTV.setSingleLine(descriptionSingleLine);
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
      focusBackground.addState(new int[]{android.R.attr.state_focused}, focusBg); // 设置获取焦点时的背景颜色
      focusBackground.addState(new int[]{},defaultBg);
      setBackground(focusBackground);
      setFocusable(true);
   }

   public void startAnimation(boolean hasFocus) {
      if (hasFocus){
         ViewCompat.animate(this).scaleX(1.02f).scaleY(1.05f).start();
         if (!iconIsState) {
            mIconIV.setSelected(true);
         }
         if (!titleIconIsState) {
            mTitleTV.setSelected(true);
         }

         mTitleTV.setTextColor(getResources().getColor(R.color.card_title_color_selected, null));
         mDescriptionTV.setTextColor(getResources().getColor(R.color.card_content_color_selected, null));
      }else {
         ViewCompat.animate(this).scaleX(1.0f).scaleY(1.0f).start();
         if (!iconIsState) {
            mIconIV.setSelected(false);
         }
         if (!titleIconIsState) {
            mTitleTV.setSelected(false);
         }
         mTitleTV.setTextColor(getResources().getColor(R.color.card_title_color_default, null));
         mDescriptionTV.setTextColor(getResources().getColor(R.color.card_content_color_default, null));
      }
   }

   public void startAnimation(boolean hasFocus, int selectedColor, int defaultColor) {
      if (hasFocus){
         ViewCompat.animate(this).scaleX(1.02f).scaleY(1.05f).start();
         if (!iconIsState) {
            mIconIV.setSelected(true);
         }
         if (!titleIconIsState) {
            mTitleTV.setSelected(true);
         }
         mTitleTV.setTextColor(getResources().getColor(selectedColor, null));
         mDescriptionTV.setTextColor(getResources().getColor(selectedColor, null));
      }else {
         ViewCompat.animate(this).scaleX(1.0f).scaleY(1.0f).start();
         if (!iconIsState) {
            mIconIV.setSelected(false);
         }
         if (!titleIconIsState) {
            mTitleTV.setSelected(false);
         }
         mTitleTV.setTextColor(getResources().getColor(defaultColor, null));
         mDescriptionTV.setTextColor(getResources().getColor(defaultColor, null));
      }
   }

   @Override
   public void setSelected(boolean selected) {
      if (mIconIV.getVisibility() == View.VISIBLE && iconIsState) {
         mIconIV.setSelected(selected);
      }
      if(titleIconIsState){
         super.setSelected(selected);
         mTitleTV.setSelected(selected);
      }
   }

   @Override
   public boolean isSelected() {
      return mIconIV.isSelected();
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
      defaultBg.setCornerRadii(radii);
      focusBackground.addState(new int[]{},defaultBg);
   }
}
