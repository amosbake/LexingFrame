package com.lexing.lrecyclerview.animation;

import android.animation.Animator;
import android.view.View;

/**
 * Base animation for item loading.
 * Author: mopel
 * Date : 2016/10/25
 */
public interface BaseAnimation {
    Animator[] getAnimators(View view);
}
