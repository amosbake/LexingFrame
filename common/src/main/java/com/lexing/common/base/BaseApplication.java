package com.lexing.common.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.LinkedList;

/**
 * Author: mopel
 * Date : 2016/10/31
 */
public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private LinkedList<ActivityInfo> mExistedActivitys = new LinkedList<>();
    private static BaseApplication mInstance;
    public static  synchronized BaseApplication getInstance() {
        return mInstance;
    }
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        mInstance = this;
    }
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (null != mExistedActivitys && null != activity) {
            // 把新的 activity 添加到最前面，和系统的 activity 堆栈保持一致
            mExistedActivitys.offerFirst(new ActivityInfo(activity,ActivityInfo.STATE_CREATE));
        }
    }
    @Override
    public void onActivityStarted(Activity activity) {
    }
    @Override
    public void onActivityResumed(Activity activity) {
    }
    @Override
    public void onActivityPaused(Activity activity) {
    }
    @Override
    public void onActivityStopped(Activity activity) {
    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }
    @Override
    public void onActivityDestroyed(Activity activity) {
        if (null != mExistedActivitys && null != activity) {
            ActivityInfo info = findActivityInfo(activity);
            if (null != info) {
                mExistedActivitys.remove(info);
            }
        }
    }
    class ActivityInfo {
        private final static int STATE_NONE = 0;
        private final static int STATE_CREATE = 1;
        Activity mActivity;
        int mState;
        ActivityInfo() {
            mActivity = null;
            mState = STATE_NONE;
        }
        ActivityInfo(Activity activity, int state) {
            mActivity = activity;
            mState = state;
        }
    }

    public Activity topActivity(){
        if (null != mExistedActivitys && mExistedActivitys.size()>0) {
            ActivityInfo _first = mExistedActivitys.getFirst();
            if (_first!=null && _first.mActivity!=null){
                return _first.mActivity;
            }
        }
        return null;
    }

    public void exitAllActivity() {
        if (null != mExistedActivitys) {
            // 先暂停监听（省得同时在2个地方操作列表）
            unregisterActivityLifecycleCallbacks( this );
            // 弹出的时候从头开始弹，和系统的 activity 堆栈保持一致
            for (ActivityInfo info : mExistedActivitys) {
                if (null == info || null == info.mActivity) {
                    continue;
                }
                try {
                    info.mActivity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mExistedActivitys.clear();
            // 退出完之后再添加监听
            registerActivityLifecycleCallbacks( this );
        }
    }
    private ActivityInfo findActivityInfo(Activity activity) {
        if (null == activity || null == mExistedActivitys) {
            return null;
        }
        for (ActivityInfo info : mExistedActivitys) {
            if (null == info) {
                continue;
            }
            if (activity.equals(info.mActivity)) {
                return info;
            }
        }
        return null;
    }
}
