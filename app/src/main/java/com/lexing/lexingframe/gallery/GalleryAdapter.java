package com.lexing.lexingframe.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lexing.lexingframe.R;
import com.lexing.lrecyclerview.adapter.BaseRecyclerAdapter;
import com.lexing.lrecyclerview.adapter.RecyclerHolder;

import java.util.Collection;
import java.util.List;

/**
 * Author: mopel
 * Date : 2016/10/29
 */
public class GalleryAdapter extends BaseRecyclerAdapter<ImageData[]> {
    private List<Integer> mTimeList;
    @Override
    public int getItemViewType(int position) {
        for (Integer _integer:mTimeList){
            if (position== _integer){
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return datas.size()+mTimeList.size();
    }

    public GalleryAdapter(RecyclerView v, Collection<ImageData[]> datas, List<Integer> timeDivider) {
        super(v, datas, R.layout.item_galley);
        this.mTimeList = timeDivider;
    }

    @Override
    protected void convert(RecyclerHolder holder, ImageData[] item, int position, boolean isScrolling) {
        if (getItemViewType(position) == 0){
            holder.setImageResource(R.id.iv1,R.mipmap.ic_launcher)
                    .setImageResource(R.id.iv2,R.mipmap.ic_launcher)
                    .setImageResource(R.id.iv3,R.mipmap.ic_launcher);
        }else {
            holder.setText(R.id.head,"head");
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new RecyclerHolder(LayoutInflater.from(context).inflate(R.layout.item_galley,parent,false));
        }else {
            return  new RecyclerHolder(LayoutInflater.from(context).inflate(R.layout.item_time,parent,false));
        }
    }
}
