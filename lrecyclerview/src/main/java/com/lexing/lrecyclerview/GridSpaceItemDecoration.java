package com.lexing.lrecyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author: yanhao(amosbake@gmail.com)
 * Date : 2015-11-27
 * Time: 16:04
 * RecyclerView间隔生成器
 */
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;//列数
    private int horizontalOuterSpacing;//水平方向上外部间距
    private int verticalOuterSpacing;//竖直方向上外部间距
    private int horizontalInnerSpacing;//水平方向上内部间距
    private int verticalInnerSpacing;//竖直方向上内部间距

    public GridSpaceItemDecoration(int spanCount, int horizontalOuterSpacing, int verticalOuterSpacing, int horizontalInnerSpacing, int verticalInnerSpacing) {
        this.spanCount = spanCount;
        this.horizontalOuterSpacing = horizontalOuterSpacing;
        this.verticalOuterSpacing = verticalOuterSpacing;
        this.horizontalInnerSpacing = horizontalInnerSpacing;
        this.verticalInnerSpacing = verticalInnerSpacing;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        if (0 == column) {
            outRect.left = horizontalOuterSpacing;
        } else {
            outRect.left = horizontalInnerSpacing / 2;
        }
        if (spanCount - 1 == column) {
            outRect.right = horizontalOuterSpacing;
        } else {
            outRect.right = horizontalInnerSpacing / 2;
        }
        int itemCount = parent.getAdapter().getItemCount();
        int frontPos = 0;
        if (itemCount % spanCount == 0) {
            frontPos = (itemCount / spanCount - 1) * spanCount - 1;
        } else {
            frontPos = (itemCount / spanCount * spanCount - 1);
        }
        if (position < spanCount) {
            outRect.top = verticalOuterSpacing;
            outRect.bottom = verticalInnerSpacing;
        } else if (position > frontPos && position < itemCount) {
            outRect.bottom = verticalOuterSpacing;
        } else {
            outRect.bottom = verticalInnerSpacing;
        }
    }
}
