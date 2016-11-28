package com.lexing.lprogressbarcollection;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 系统屏幕的一些操作<br>
 * 
 * <b>创建时间</b> 2014-8-14
 * @version 1.1
 */
 final class DensityUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
     static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
     static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
     static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     */
     static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取dialog宽度
     */
     static int getDialogW(Context cxt) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = cxt.getResources().getDisplayMetrics();
        int w = dm.widthPixels - 100;
        // int w = aty.getWindowManager().getDefaultDisplay().getWidth() - 100;
        return w;
    }

     static float getDensity(Context cxt){
        return cxt.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕宽度
     */
     static int getScreenW(Context cxt) {
        return cxt.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     */
     static int getScreenH(Context cxt) {
        return cxt.getResources().getDisplayMetrics().heightPixels;
    }
    /**
     * 获取屏幕高度(去除虚拟键盘)
     */
     static int getScreenRealH(Context context) {
        int h;
        WindowManager winMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = winMgr.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealMetrics(dm);
            h = dm.heightPixels;
        } else {
            try {
                Method method = Class.forName("android.view.Display").getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, dm);
                h = dm.heightPixels;
            } catch (Exception e) {
                display.getMetrics(dm);
                h = dm.heightPixels;
            }
        }
        return h;
    }


    /**
     * 获取状态栏高度
     */
     static int getStatusBarHeight(Context cxt){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0,
        statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = cxt.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
    /**
     * 获取导航条高度
     */
     static int getNavigationBarH(Context c) {
        Resources resources = c.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(identifier);
    }
}