package com.lexing.lrecyclerview.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 *
 * Author: mopel
 * Date : 2016/10/25
 */
public class SlideInRightAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0)
        };
    }
}
