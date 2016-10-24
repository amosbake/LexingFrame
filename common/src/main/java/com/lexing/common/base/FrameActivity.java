package com.lexing.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * TianwanFramework Activity基类，实现了Activity栈
 */
public abstract class FrameActivity extends AppCompatActivity {

    protected Context mContext;

    /**
     * 当前Activity状态
     */
    public enum ActivityState {
        RESUME, PAUSE, STOP, DESTROY
    }

    /** Activity状态 */
    public ActivityState activityState = ActivityState.DESTROY;

    public void finishOnClick(View view){
        if(view != null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }


    /***************************************************************************
     *
     * print Activity callback methods
     *
     ***************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ActivityStack.getInstance().addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        LogUtil.d("---------onStart ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = ActivityState.RESUME;
//        LogUtil.d("---------onResume ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityState = ActivityState.PAUSE;
//        LogUtil.d("---------onPause ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityState = ActivityState.STOP;
//        LogUtil.d("---------onStop ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        LogUtil.d("---------onRestart ");
    }

    @Override
    protected void onDestroy() {
        ActivityStack.getInstance().beforeDestroy(this);
        activityState = ActivityState.DESTROY;
//        LogUtil.d("---------onDestroy ");
        super.onDestroy();
        ActivityStack.getInstance().removeActivity(this);
    }
    public void finishApp(){
        ActivityStack.getInstance().finishAllActivity();
    }
}
