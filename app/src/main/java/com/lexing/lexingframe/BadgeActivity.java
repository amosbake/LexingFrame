package com.lexing.lexingframe;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;

public class BadgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);
    }

    Drawable wrap(@DrawableRes int icon,    ColorStateList tint){
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),icon, getTheme());
        if (tint == null) {
            tint = ResourcesCompat.getColorStateList(getResources(), R.color.colorPrimary, getTheme());
        }
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTintList(drawable, tint);
        }
        return drawable;
    }
}
