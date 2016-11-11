package com.lexing.lexingframe.gallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lexing.common.assist.L;
import com.lexing.lexingframe.R;
import com.lexing.lrecyclerview.mutlitype.Item;
import com.lexing.lrecyclerview.mutlitype.MultiTypeAdapter;
import com.lexing.lrecyclerview.recyclerview.ProgressStyle;
import com.lexing.lrecyclerview.recyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    private XRecyclerView mRecyclerView;
    private MultiTypeAdapter mMultiTypeAdapter;
    private int index = 1;

    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        L.init(true, "ganhuo");
        View _view = inflater.inflate(R.layout.fragment_ganhuo, container, false);
        initViews(_view);
        return _view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void initViews(View rootView) {
        mRecyclerView = (XRecyclerView) rootView.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mMultiTypeAdapter=new MultiTypeAdapter(getInitDatas());
        mMultiTypeAdapter.applyGlobalMultiTypePool();
        mMultiTypeAdapter.register(ImageDatas.class,new ImageItemProvider());
        mMultiTypeAdapter.register(TitleData.class,new TitleItemProvider());
        mRecyclerView.setAdapter(mMultiTypeAdapter);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);
    }

    List<Item> getInitDatas(){
        Calendar _calendar = Calendar.getInstance();
        _calendar.set(2016,11,14);
        Date _date1 = _calendar.getTime();
        _calendar.set(2016,11,15);
        Date _date2 = _calendar.getTime();
        _calendar.set(2016,11,16);
        Date _date3 = _calendar.getTime();
        ImageData _imageData = new ImageData("aa",_date1);
        List<ImageData> datas = new ArrayList<>();
        datas.add(_imageData);
        _imageData = new ImageData("bb",_date1);
        datas.add(_imageData);
        _imageData = new ImageData("cc",_date1);
        datas.add(_imageData);
        _imageData = new ImageData("dd",_date1);
        datas.add(_imageData);
        _imageData = new ImageData("ee",_date1);
        datas.add(_imageData);
        _imageData = new ImageData("fff",_date1);
        datas.add(_imageData);
        _imageData = new ImageData("zzz",_date1);
        datas.add(_imageData);
        _imageData = new ImageData("ggg",_date2);
        datas.add(_imageData);
        _imageData = new ImageData("hhh",_date2);
        datas.add(_imageData);
        _imageData = new ImageData("iii",_date2);
        datas.add(_imageData);
        _imageData = new ImageData("jjj",_date2);
        datas.add(_imageData);
        _imageData = new ImageData("ee",_date3);
        datas.add(_imageData);
        _imageData = new ImageData("fff",_date3);
        datas.add(_imageData);
        _imageData = new ImageData("zzz",_date3);
        datas.add(_imageData);
        _imageData = new ImageData("ggg",_date3);
        datas.add(_imageData);
        _imageData = new ImageData("hhh",_date3);
        datas.add(_imageData);
        _imageData = new ImageData("iii",_date3);
        datas.add(_imageData);
        _imageData = new ImageData("jjj",_date3);
        datas.add(_imageData);
       return ImageHelper.spiltImageDataByDate(datas);
    }

}
