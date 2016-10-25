package com.lexing.lrecyclerview.adapter;

import android.animation.Animator;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;

import com.lexing.lrecyclerview.animation.AlphaInAnimation;
import com.lexing.lrecyclerview.animation.BaseAnimation;

/**
 * Author: mopel
 * Date : 2016/10/25
 */
public abstract class SuperCursorAdapter extends RecyclerView.Adapter<SuperViewHolder>  implements IAnimation, ILayoutManager, IHeaderFooter {
    private Context mContext;
    private Cursor mCursor;
    private final int itemLayoutId;
    private boolean mDataValid;
    private int mRowIdColumn;
    private DataSetObserver mDataSetObserver;


    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    protected RecyclerView mRecyclerView;

    protected static final int TYPE_HEADER = -0x100;
    protected static final int TYPE_FOOTER = -0x101;
    protected View mHeader;
    protected View mFooter;


    private Interpolator mInterpolator = new LinearInterpolator();
    private long mDuration = 300;
    private boolean mLoadAnimationEnabled;
    private boolean mOnlyOnce = true;
    private BaseAnimation mLoadAnimation;
    private int mLastPosition = -1;


    public SuperCursorAdapter(RecyclerView v, Cursor cursor, @LayoutRes int itemLayoutId) {
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

    public Context getContext() {
        return mContext;
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }


    /**
     * How many items are represented by this RecyclerView.Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getItemCount() {
        int size = mCursor == null ? 0 : mCursor.getCount();
        if (hasHeaderView())
            size++;
        if (hasFooterView())
            size++;
        return size;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }


    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (isHeaderView(position)) {
            viewType = TYPE_HEADER;
        } else if (isFooterView(position)) {
            viewType = TYPE_FOOTER;
        } else {
            viewType = 0;
        }
        return viewType;
    }


    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final SuperViewHolder holder;
        if (viewType == TYPE_HEADER && hasHeaderView()) {
            return new SuperViewHolder(getHeaderView());
        } else if (viewType == TYPE_FOOTER && hasFooterView()) {
            return new SuperViewHolder(getFooterView());
        } else {
            holder = new SuperViewHolder(LayoutInflater.from(mContext).inflate(itemLayoutId,parent,false));
        }
        if (!(holder.itemView instanceof AdapterView)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, viewType, holder.getAdapterPosition());
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(v, viewType, holder.getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType != TYPE_HEADER && viewType != TYPE_FOOTER) {
            onBindViewHolder(holder,mCursor, hasHeaderView() ? --position : position);
            addLoadAnimation(holder); // Load animation
        }
    }

    public abstract void onBindViewHolder(SuperViewHolder holder, Cursor cursor,int position);

    /**
     * ------------------------------------ Header / Footer ------------------------------------
     */

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (mRecyclerView != null && mRecyclerView != recyclerView)
            mRecyclerView = recyclerView;
        // Ensure a situation that add header or footer before setAdapter().
        ifGridLayoutManager();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = null;
    }

    @Override
    public void onViewAttachedToWindow(SuperViewHolder holder) {
        if (isHeaderView(holder.getLayoutPosition()) || isFooterView(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
            }
        }
    }

    @Override
    public boolean hasLayoutManager() {
        return mRecyclerView != null && mRecyclerView.getLayoutManager() != null;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return hasLayoutManager() ? mRecyclerView.getLayoutManager() : null;
    }

    @Override
    public View getHeaderView() {
        return mHeader;
    }

    @Override
    public View getFooterView() {
        return mFooter;
    }

    @Override
    public void addHeaderView(View header) {
        if (hasHeaderView())
            throw new IllegalStateException("You have already added a header view.");
        mHeader = header;
        setLayoutParams(mHeader);
        ifGridLayoutManager();
        notifyItemInserted(0);
    }

    @Override
    public void addFooterView(View footer) {
        if (hasFooterView())
            throw new IllegalStateException("You have already added a footer view.");
        mFooter = footer;
        setLayoutParams(mFooter);
        ifGridLayoutManager();
        notifyItemInserted(getItemCount() - 1);
    }

    private void setLayoutParams(View view) {
        if (hasHeaderView() || hasFooterView()) {
            RecyclerView.LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                view.setLayoutParams(new StaggeredGridLayoutManager.LayoutParams(
                        StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT,
                        StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT));
            } else if (layoutManager instanceof GridLayoutManager) {
                view.setLayoutParams(new GridLayoutManager.LayoutParams(
                        GridLayoutManager.LayoutParams.MATCH_PARENT,
                        GridLayoutManager.LayoutParams.WRAP_CONTENT));
            } else {
                view.setLayoutParams(new RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    @Override
    public boolean removeHeaderView() {
        if (hasHeaderView()) {
            mHeader = null;
            notifyItemRemoved(0);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeFooterView() {
        if (hasFooterView()) {
            int footerPosition = getItemCount() - 1;
            mFooter = null;
            notifyItemRemoved(footerPosition);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasHeaderView() {
        return getHeaderView() != null;
    }

    @Override
    public boolean hasFooterView() {
        return getFooterView() != null;
    }

    @Override
    public boolean isHeaderView(int position) {
        return hasHeaderView() && position == 0;
    }

    @Override
    public boolean isFooterView(int position) {
        return hasFooterView() && position == getItemCount() - 1;
    }

    private void ifGridLayoutManager() {
        if (hasHeaderView() || hasFooterView()) {
            final RecyclerView.LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager.SpanSizeLookup originalSpanSizeLookup =
                        ((GridLayoutManager) layoutManager).getSpanSizeLookup();
                ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeaderView(position) || isFooterView(position)) ?
                                ((GridLayoutManager) layoutManager).getSpanCount() :
                                originalSpanSizeLookup.getSpanSize(position);
                    }
                });
            }
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
}
