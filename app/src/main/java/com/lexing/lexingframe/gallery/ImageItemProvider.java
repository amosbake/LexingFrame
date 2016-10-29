package com.lexing.lexingframe.gallery;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lexing.lexingframe.R;
import com.lexing.lrecyclerview.adapter.RecyclerHolder;
import com.lexing.lrecyclerview.mutlitype.ItemViewProvider;

/**
 * Author: mopel
 * Date : 2016/10/29
 */
public class ImageItemProvider extends ItemViewProvider<ImageDatas, RecyclerHolder> {

    @NonNull
    @Override
    protected RecyclerHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_galley, parent, false);
        return new RecyclerHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerHolder holder, @NonNull ImageDatas imageData) {
        holder.setImageResource(R.id.iv1, R.mipmap.ic_launcher)
                .setImageResource(R.id.iv2, R.color.colorAccent)
                .setImageResource(R.id.iv3, R.color.colorPrimary);
    }
}
