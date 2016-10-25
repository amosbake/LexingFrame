package com.lexing.lrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Asura on 2016/3/17.
 */
public class RecyclerStatusList extends SwipeRefreshLayout {

    private final int loadingMoreViewId;
    private int state;
    public static final int STATE_LOADING = 0;
    public static final int STATE_ERROR = -1;
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_EMPTY = 2;
    public static final int STATE_LOAD_MORE_EMPTY = 3;
    public static final int STATE_LOAD_MORE_ERROR = 4;
    public static final int STATE_LOAD_MORE_SUCCESS = 5;
    public static final int STATE_REFRESH_LOADING = 6;

    private int emptyViewId;
    private int loadingViewId;
    private int errorViewId;

    private View emptyView;
    private View errorView;
    private View loadingView;
    private RecyclerView recyclerView;

    private FrameLayout frameLayout;
    private LoadMoreAdapterWrapper loadMoreAdapterWrapper;

    @IntDef({
            STATE_ERROR,
            STATE_SUCCESS,
            STATE_EMPTY,
            STATE_LOADING,
            STATE_REFRESH_LOADING,
            STATE_LOAD_MORE_EMPTY,
            STATE_LOAD_MORE_ERROR,
            STATE_LOAD_MORE_SUCCESS
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }

    public RecyclerStatusList(Context context) {
        this(context, null);
    }

    public RecyclerStatusList(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RecyclerStatusList, 0, 0);
        emptyViewId = typedArray.getResourceId(R.styleable.RecyclerStatusList_emptyView, R.layout.view_empty);
        loadingViewId = typedArray.getResourceId(R.styleable.RecyclerStatusList_loadingView, R.layout.view_loading);
        errorViewId = typedArray.getResourceId(R.styleable.RecyclerStatusList_errorView, R.layout.view_load_error);
        loadingMoreViewId = typedArray.getResourceId(R.styleable.RecyclerStatusList_moreLoadingView, R.layout.view_load_more);
        typedArray.recycle();
        init();
    }

    private void init() {
        frameLayout = new FrameLayout(getContext());
        frameLayout.setClickable(true);
        frameLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(frameLayout);
        //recycleView
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        frameLayout.addView(recyclerView);

        setProgressBackgroundColorSchemeColor(0xFFFFFFFF);
        // 设置小圈圈里面的旋转箭头颜色，这里是不确定数目参数...，可以设置旋转第一圈的颜色，第二圈的颜色...
        setColorSchemeColors(0xFFFF4364, 0xFF198397,
                0xFF00FFCC);
        setState(STATE_LOADING);
    }

    public void setState(@State int state) {
        this.state = state;
        if (state == STATE_REFRESH_LOADING) {
            setRefreshing(true);
        } else {
            setRefreshing(false);
        }
        setEnabled(true);
        int count = recyclerView.getAdapter() != null ? recyclerView.getAdapter().getItemCount() : 0;
        if (state == STATE_LOADING && count == 0) {
            showLoadingView();
        } else if (state == STATE_ERROR && count == 0) {
            showErrorView();
        } else if (state == STATE_SUCCESS) {
            showSuccessView();
        } else if (state == STATE_EMPTY) {
            showEmptyView();
        } else if (state == STATE_LOAD_MORE_EMPTY) {
            if (loadMoreAdapterWrapper != null) {
                loadMoreAdapterWrapper.setLoadMoreEmpty();
            }
        } else if (state == STATE_LOAD_MORE_ERROR) {
            if (loadMoreAdapterWrapper != null) {
                loadMoreAdapterWrapper.setLoadMoreFail();
            }
        } else if (state == STATE_LOAD_MORE_SUCCESS) {
            if (loadMoreAdapterWrapper != null) {
                loadMoreAdapterWrapper.setLoadMoreSuccess();
            }
        }
    }

    public int getState() {
        return state;
    }

    private void showLoadingView() {
        if (loadingView == null) {
            loadingView = inflate(getContext(), loadingViewId, null);
            frameLayout.addView(loadingView);
        }
        if (emptyView != null) {
            emptyView.setVisibility(GONE);
        }
        if (errorView != null) {
            errorView.setVisibility(GONE);
        }
        recyclerView.setVisibility(GONE);
        loadingView.setVisibility(VISIBLE);
        setEnabled(false);
    }

    private void showErrorView() {
        if (errorView == null) {
            errorView = inflate(getContext(), errorViewId, null);
            frameLayout.addView(errorView);
        }
        if (emptyView != null) {
            emptyView.setVisibility(GONE);
        }
        if (loadingView != null) {
            loadingView.setVisibility(GONE);
        }
        recyclerView.setVisibility(GONE);
        errorView.setVisibility(VISIBLE);
    }

    private void showSuccessView() {
        if (emptyView != null) {
            emptyView.setVisibility(GONE);
        }
        if (loadingView != null) {
            loadingView.setVisibility(GONE);
        }
        if (errorView != null) {
            errorView.setVisibility(GONE);
        }
        recyclerView.setVisibility(VISIBLE);
    }

    private void showEmptyView() {
        if (emptyView == null) {
            emptyView = inflate(getContext(), emptyViewId, null);
            frameLayout.addView(emptyView);
        }
        if (errorView != null) {
            errorView.setVisibility(GONE);
        }
        if (loadingView != null) {
            loadingView.setVisibility(GONE);
        }
        recyclerView.setVisibility(GONE);
        emptyView.setVisibility(VISIBLE);
    }

    public View getEmptyView() {
        return emptyView;
    }

    public View getErrorView() {
        return errorView;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter, LoadMoreAdapterWrapper.OnLoadMoreListener loadMoreListener) {
        loadMoreAdapterWrapper = new LoadMoreAdapterWrapper(adapter);
        if (loadingMoreViewId != 0) {
            loadMoreAdapterWrapper.setLoadMoreView(loadingMoreViewId);
            loadMoreAdapterWrapper.setLoadMoreListener(loadMoreListener);
        }
        recyclerView.setAdapter(loadMoreAdapterWrapper);
    }
}
