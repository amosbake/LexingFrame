package com.lexing.common.base;

import android.content.Context;
import android.widget.Toast;

import java.util.Date;

/**
 * 侵入式View的调用工具类
 */
public class ViewInject {

    private static final int TOAST_INTERVAL = 2 * 1000;
    private static String oldToast;
    private static long oldToastTime;

    private ViewInject() {}

    private static class ClassHolder {
        private static final ViewInject instance = new ViewInject();
    }

    /**
     * 类对象创建方法
     * 
     * @return 本类的对象
     */
    public static ViewInject create() {
        return ClassHolder.instance;
    }

    /**
     * 显示一个toast
     * 
     * @param msg
     */
    public static void toast(String msg) {
        try {
            toast(BaseApplication.getInstance().getApplicationContext(), msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 长时间显示一个toast
     * 
     * @param msg
     */
    public static void longToast(String msg) {
        try {
            longToast(BaseApplication.getInstance().getApplicationContext(), msg);
        } catch (Exception e) {
        }
    }

    /**
     * 长时间显示一个toast
     * 
     * @param msg
     */
    public static void longToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示一个toast
     * 
     * @param msg
     */
    public static void toast(Context context, String msg) {
        long newToastTime = new Date().getTime();
        if(newToastTime - oldToastTime < TOAST_INTERVAL && msg.equals(oldToast)){
            return;
        }
        oldToastTime = newToastTime;
        oldToast = msg;
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
