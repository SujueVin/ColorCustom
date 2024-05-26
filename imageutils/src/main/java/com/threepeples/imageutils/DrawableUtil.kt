package com.threepeples.imageutils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build


object DrawableUtil {
    /**
     * BitmapFactory.decodeResource方法返回为null,此代码在4.4上运行正常，但在5.0以上的系统会出现空指针。
     * 原因在于此本来方法不能将vector转化为bitmap，而apk编译时为了向下兼容，会根据vector生产相应的png，而4.4的系统运行此代码时其实用的是png资源。
     */
    fun getBitmapByDrawableId(context: Context, vectorDrawableId: Int): Bitmap {
        var bitmap: Bitmap
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val vectorDrawable = context.getDrawable(vectorDrawableId)
            bitmap = getBitmapByDrawable(vectorDrawable)
        } else {
            bitmap = BitmapFactory.decodeResource(context.resources, vectorDrawableId)
        }
        return bitmap
    }

    fun getBitmapByDrawable(drawable: Drawable): Bitmap{
        var bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}