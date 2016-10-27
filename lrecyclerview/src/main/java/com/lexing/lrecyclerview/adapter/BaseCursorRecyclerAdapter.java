package com.lexing.lrecyclerview.adapter;

import android.animation.Animator;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.lexing.lrecyclerview.animation.AlphaInAnimation;
import com.lexing.lrecyclerview.animation.BaseAnimation;

/**
 * Author: mopel
 * Date : 2016/2/24
 */
public abstract class BaseCursorRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHolder>  implements IAnimation{
    private Context mContext;
    private Cursor mCursor;
    private final int itemLayoutId;
    private boolean mDataValid;
    private int mRowIdColumn;
    private DataSetObserver mDataSetObserver;
    /** animation **/
    private Interpolator mInterpolator = new LinearInterpolator();
    private long mDuration = 300;
    private boolean mLoadAnimationEnabled;
    private boolean mOnlyOnce = true;
    private BaseAnimation mLoadAnimation;
    private int mLastPosition = -1;

    public BaseCursorRecyclerAdapter(RecyclerView v, Cursor cursor, @LayoutRes int itemLayoutId) {
        mContext = v.getContext();
        mCursor = cursor;
        this.itemLayoutId = itemLayoutId;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }
    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerHolder(LayoutInflater.from(mContext).inflate(itemLayoutId, parent, false));
    }

    public abstract void onBindViewHolder(RecyclerHolder holder, Cursor cursor, int position);

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(holder, mCursor,position);
        addLoadAnimation(holder);
    }


    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
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
