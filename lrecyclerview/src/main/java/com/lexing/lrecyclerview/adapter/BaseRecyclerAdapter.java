package com.lexing.lrecyclerview.adapter;

import android.animation.Animator;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.lexing.lrecyclerview.animation.AlphaInAnimation;
import com.lexing.lrecyclerview.animation.BaseAnimation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RecyclerAdapter 的基类, 继承需要实现其绑定数据的方法
 * Author: yanhao(amosbake@gmail.com)
 * Date : 2015-10-12
 * Time: 15:31
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> implements IAnimation, CRUD<T> {
    private static final String TAG = "BaseRecyclerAdapter";
    protected List<T> datas;
    private final int itemLayoutId;
    private boolean isScrolling;
    protected final Context context;

    private Interpolator mInterpolator = new LinearInterpolator();
    private long mDuration = 300;
    private boolean mLoadAnimationEnabled;
    private boolean mOnlyOnce = true;
    private BaseAnimation mLoadAnimation;
    private int mLastPosition = -1;

    public BaseRecyclerAdapter(RecyclerView v, Collection<T> datas, int itemLayoutId) {
        if (datas == null) {
            this.datas = new ArrayList<>();
        } else if (datas instanceof List) {
            this.datas = (List<T>) datas;
        } else {
            this.datas = new ArrayList<>(datas);
        }
        this.itemLayoutId = itemLayoutId;
        this.context = v.getContext();
        v.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isScrolling = !(newState == RecyclerView.SCROLL_STATE_IDLE);
            }
        });
    }

    /**
     * RecyclerView 适配器数据填充方法
     *
     * @param holder      viewholder
     * @param item        javabean
     * @param position    position
     * @param isScrolling 是否正在滚动
     */
    protected abstract void convert(RecyclerHolder holder, T item, int position, boolean isScrolling);

    /**
     * RecyclerView 适配器数据填充方法，带有payload，可以在需要时覆写
     *
     * @param holder      viewholder
     * @param item        javabean
     * @param position    position
     * @param isScrolling 是否正在滚动
     */
    protected void convert(RecyclerHolder holder, T item, int position, boolean isScrolling, List<Object> payloads) {
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerHolder(LayoutInflater.from(context).inflate(itemLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        convert(holder, datas.get(position), position, isScrolling);
        addLoadAnimation(holder); // Load animation
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position, List<Object> payloads) {
        convert(holder, datas.get(position), position, isScrolling, payloads);
    }

    @Override
    public int getItemCount() {
        if (null == datas) {
            return 0;
        }
        return datas.size();
    }


    public BaseRecyclerAdapter<T> refresh(Collection<T> datas) {
        if (datas == null) {
            this.datas = new ArrayList<>();
        } else if (datas instanceof List) {
            this.datas = (List<T>) datas;

        } else {
            this.datas = new ArrayList<>(datas);
        }
        return this;
    }


    /**
     * ----------------------- CRUD ---------------------------------
     **/
    @Override
    public void add(@NonNull T item) {
        datas.add(item);
    }


    @Override
    public void addAll(List<T> items) {
        if (items == null || items.isEmpty()) {
            Log.w(TAG, "addAll: The list you passed contains no elements.");
            return;
        }
        datas.addAll(items);
    }

    @Override
    public void remove(T item) {
        datas.remove(item);
    }

    @Override
    public void removeAll(List<T> items) {
        datas.removeAll(items);
    }

    @Override
    public void retainAll(List<T> items) {
        datas.retainAll(items);
    }


    @Override
    public void replaceAll(List<T> items) {
        if (items == null || items.isEmpty()) {
            clear();
            return;
        }
        if (datas.isEmpty()) {
            addAll(items);
        }
    }

    @Override
    public boolean contains(T item) {
        return datas.contains(item);
    }

    @Override
    public boolean containsAll(List<T> items) {
        return datas.containsAll(items);
    }

    @Override
    public void clear() {
        int count = getItemCount();
        if (count > 0) {
            datas.clear();
        }
    }

    @Override
    public void diff(final DefaultDiffCallback<T> callback) {

        if (callback == null || callback.getNewListSize() < 1) {
            Log.w(TAG, "Invalid size of the new list.");
            return;
        }

        if (!is24_2_0()) {
            Log.e(TAG, "This method only works on version 24.2.0or above.");
            return;
        }

        new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
            @Override
            protected DiffUtil.DiffResult doInBackground(Void... params) {
                return DiffUtil.calculateDiff(callback);
            }

            @Override
            protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                refresh(callback.getNewList());
                if (diffResult != null) {
                    diffResult.dispatchUpdatesTo(BaseRecyclerAdapter.this);
                }
            }
        }.execute();
    }

    private boolean is24_2_0() {
        try {
            Class.forName("android.support.v7.util.DiffUtil");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    /**
     * ------------------------------------ Load animation ------------------------------------
     */

    @Override
    public void enableLoadAnimation() {
        enableLoadAnimation(mDuration, new AlphaInAnimation());
    }

    @Override
    public void enableLoadAnimation(long duration, BaseAnimation animation) {
        if (duration > 0) {
            mDuration = duration;
        }
        mLoadAnimationEnabled = true;
        mLoadAnimation = animation;
    }

    @Override
    public void cancelLoadAnimation() {
        mLoadAnimationEnabled = false;
        mLoadAnimation = null;
    }

    @Override
    public void setOnlyOnce(boolean onlyOnce) {
        mOnlyOnce = onlyOnce;
    }

    @Override
    public void addLoadAnimation(RecyclerView.ViewHolder holder) {
        if (mLoadAnimationEnabled) {
            if (!mOnlyOnce || holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = mLoadAnimation == null ? new AlphaInAnimation() : mLoadAnimation;
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    anim.setInterpolator(mInterpolator);
                    anim.setDuration(mDuration).start();
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

}
