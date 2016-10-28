package com.lexing.lexingframe.ganhuo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import com.lexing.lexingframe.R;
import com.lexing.lrecyclerview.adapter.BaseRecyclerAdapter;
import com.lexing.lrecyclerview.adapter.RecyclerHolder;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Author: mopel
 * Date : 2016/10/27
 */
public class AndroidAdapter extends BaseRecyclerAdapter<GanhuoAndroid> {

    public AndroidAdapter(RecyclerView v, Collection<GanhuoAndroid> datas, int itemLayoutId) {
        super(v, datas, itemLayoutId);
    }

    @Override
    protected void convert(RecyclerHolder holder, GanhuoAndroid item, int position, boolean isScrolling, List<Object> payloads) {
        super.convert(holder, item, position, isScrolling, payloads);
    }

    @Override
    protected void convert(RecyclerHolder holder, GanhuoAndroid item, int position, boolean isScrolling) {
        holder.setText(R.id.tvTitle,item.desc)
                .setText(R.id.tvAuthor,item.author)
                .setText(R.id.tvUrl,item.url)
                .itemView.setBackgroundColor(getRandomColor(position));
    }

    int getRandomColor(int i){
        Random _random=new Random();
        return Color.argb(60,_random.nextInt(255),_random.nextInt(255),_random.nextInt(255));
    }
}
