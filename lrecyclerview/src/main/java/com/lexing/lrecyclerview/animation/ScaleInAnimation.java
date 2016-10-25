package com.lexing.lrecyclerview.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 *
 * Author: mopel
 * Date : 2016/10/25
 */
public class ScaleInAnimation implements BaseAnimation {
    private static final float DEFAULT_SCALE_FROM = .5f;
    private final float mFrom;

    public ScaleInAnimation() {
        this(DEFAULT_SCALE_FROM);
    }

    public ScaleInAnimation(float from) {
        mFrom = from;
    }

    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator _scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f);
        ObjectAnimator _scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f);
        return new Animator[]{_scaleX,_scaleY};
    }
}
