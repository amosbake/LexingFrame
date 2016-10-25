package com.lexing.lrecyclerview.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 *
 * Author: mopel
 * Date : 2016/10/25
 */
public class SlideBottomAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0)
        };
    }
}
