package com.lexing.lrecyclerview;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Asura on 2016/9/28.
 */

public class LoadMoreAdapterWrapper extends RecyclerView.Adapter {

    private RecyclerView.Adapter realAdapter;
    private final static int TYPE_LOAD_MORE = Integer.MAX_VALUE-2;
    private int loadMoreResId;
    private OnLoadMoreListener loadMoreListener;
    private Boolean isCanLoadMore = true;
    private State state = State.DEFAULT;
    private int footerVar = 1;
    private RecyclerView.OnScrollListener onScrollListener;

    public enum State {
        DEFAULT, LOADING, FAIL
    }

    public LoadMoreAdapterWrapper(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("RecyclerView adapter can`t be null");
        }
        this.realAdapter = adapter;
        this.realAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                LoadMoreAdapterWrapper.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                LoadMoreAdapterWrapper.this.notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                LoadMoreAdapterWrapper.this.notifyItemRangeChanged(positionStart, itemCount, payload);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                LoadMoreAdapterWrapper.this.notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                LoadMoreAdapterWrapper.this.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                LoadMoreAdapterWrapper.this.notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOAD_MORE) {
            return new LoadMoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(loadMoreResId,parent,false));
        }
        return realAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            if (loadMoreListener != null && state != State.LOADING) {
                loadMoreListener.onLoadMore();
                state = State.LOADING;
            }
        } else {
            realAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return realAdapter.getItemCount() + (canLoadMore() ? footerVar : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return TYPE_LOAD_MORE;
        }
        return realAdapter.getItemViewType(position);
    }

    public void setLoadMoreView(@LayoutRes int resId) {
        this.loadMoreResId = resId;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setCanLoadMore(Boolean isCanLoadMore) {
        this.isCanLoadMore = isCanLoadMore;
    }

    public void setLoadMoreSuccess() {
        state = State.DEFAULT;
    }

    public void setLoadMoreFail() {
        state = State.FAIL;
        //移除Loading
        footerVar = 0;
        notifyItemRemoved(realAdapter.getItemCount());
    }

    public void setLoadMoreEmpty() {
        state = State.DEFAULT;
        //移除Loading
        footerVar = 0;
        notifyItemRemoved(realAdapter.getItemCount());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //当前状态为error或empty
                //底部向上
                if (state != State.LOADING) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        if (lastVisibleItemPosition == getItemCount() - 1) {
                            footerVar = 1;
                            notifyItemInserted(getItemCount() - 1);
                        }
                    }
                }
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        recyclerView.removeOnScrollListener(onScrollListener);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    private boolean isShowLoadMore(int position) {
        return canLoadMore() && position > 0 && position == realAdapter.getItemCount();
    }

    private boolean canLoadMore() {
        return loadMoreResId != 0 && isCanLoadMore;
    }

    private static class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
