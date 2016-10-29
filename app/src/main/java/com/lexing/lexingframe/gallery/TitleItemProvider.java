package com.lexing.lexingframe.gallery;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lexing.lexingframe.R;
import com.lexing.lrecyclerview.adapter.RecyclerHolder;
import com.lexing.lrecyclerview.mutlitype.ItemViewProvider;

/**
 * Author: mopel
 * Date : 2016/10/29
 */
public class TitleItemProvider extends ItemViewProvider<TitleData, RecyclerHolder> {

    @NonNull
    @Override
    protected RecyclerHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_time, parent, false);
        return new RecyclerHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerHolder holder, @NonNull TitleData titleData) {
        ((TextView)holder.itemView).setText(titleData.title);
    }
}
