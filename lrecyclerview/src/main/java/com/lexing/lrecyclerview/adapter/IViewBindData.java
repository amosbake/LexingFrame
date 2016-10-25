package com.lexing.lrecyclerview.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Create and bind data to item view.
 * <p>
 * Created by Cheney on 16/3/31.
 */
interface IViewBindData<T, VH> {

    /**
     * @param convertView Support by {@link ListSupportAdapter#getView(int, View, ViewGroup)}.
     * @param parent      Target container(ListView, GridView, RecyclerView,Spinner, etc.).
     * @param viewType    Choose the layout resource according to view type.
     * @return Created view holder.
     */
    VH onCreate(View convertView, ViewGroup parent, int viewType);

    /**
     * Method for binding data to view.
     *
     * @param holder         ViewHolder
     * @param viewType       {@link RecyclerSupportAdapter#getItemViewType(int)}
     * @param layoutPosition position
     * @param item           data
     */
    void onBind(VH holder, int viewType, int layoutPosition, T item);
}
