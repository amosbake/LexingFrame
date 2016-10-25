package com.lexing.lrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;


/**
 * Created by Adam on 2015/11/26.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private int mHeight;
    private Paint mPaint;

    public DividerItemDecoration(Context context, @ColorRes int colorRes) {
        mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1f, context.getResources().getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(context, colorRes));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int count = parent.getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = parent.getChildAt(i);
            final int top = child.getTop();
            final int bottom = top + mHeight;
            final int left = child.getLeft();
            final int right = child.getRight();

            c.save();
            c.drawRect(left, top, right, bottom, mPaint);
            c.restore();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mHeight);
    }
}