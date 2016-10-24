package com.lexing.common.base;

import android.app.Activity;
import android.app.Dialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出<br>
 * 
 * <b>创建时间</b> 2014-2-28
 * @version 1.1
 */
final public class ActivityStack {
    private static Stack<FrameActivity> activityStack;
    private static Map<Activity, Dialog> dialogMap;
    private static final ActivityStack instance = new ActivityStack();

    private ActivityStack() {}

    public static ActivityStack getInstance() {
        return instance;
    }

    /**
     * 获取当前Activity栈中元素个数
     */
    public int getCount() {
        if(activityStack == null){
            return 0;
        }
        return activityStack.size();
    }

    public void bindDialog(Dialog dialog){
        if (dialogMap == null) {
            dialogMap = new HashMap<>();
        }
        if(topActivity() != null){
            Dialog oldDialog = dialogMap.get(topActivity());
            if(oldDialog != null && oldDialog.isShowing()){
                oldDialog.dismiss();
            }
            dialogMap.put(topActivity(), dialog);
        }
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(FrameActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<FrameActivity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public Activity topActivity() {
        if (activityStack == null) {
            throw new NullPointerException(
                    "Activity stack is Null,your Activity must extend FrameActivity");
        }
        if (activityStack.isEmpty()) {
            return null;
        }
        FrameActivity activity = activityStack.lastElement();
        return (Activity) activity;
    }

    /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    public Activity findActivity(Class<?> cls) {
        FrameActivity activity = null;
        for (FrameActivity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return (Activity) activity;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void removeActivity() {
        FrameActivity activity = activityStack.lastElement();
        removeActivity((Activity) activity);
    }

    /**
     * FrameActivity销毁之前调用，用来处理一些需要ActivityStack统一处理的事务。
     * @param activity
     */
    public void beforeDestroy(Activity activity){
        if(dialogMap != null){
            Dialog dialog = dialogMap.get(activity);
            if(dialog != null){
                dialog.dismiss();
            }
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            // activity.finish();//此处不用finish
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void removeActivity(Class<?> cls) {
        for (FrameActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                removeActivity((Activity) activity);
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     */
    public void finishAllActivityExceptTop() {
        Activity top = topActivity();
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (!(activity.equals(top))) {
                activity.finish();
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                ((Activity) activityStack.get(i)).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 应用程序退出
     * 
     */
    public void AppExit() {
        try {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            Runtime.getRuntime().exit(-1);
        }
    }
}