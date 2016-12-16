package com.lexing.lexingframe.ganhuo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lexing.common.assist.L;
import com.lexing.lexingframe.R;
import com.lexing.lexingframe.core.ServiceGenerator;
import com.lexing.lrecyclerview.recyclerview.ProgressStyle;
import com.lexing.lrecyclerview.recyclerview.XRecyclerView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GanhuoFragment extends Fragment implements GanhuoView{

    private XRecyclerView mRecyclerView;
    private AndroidAdapter mAdapter;
    private IGanhuoPresenter mIGanhuoPresenter;


    public GanhuoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        L.init(true, "ganhuo");
        View _view = inflater.inflate(R.layout.fragment_ganhuo, container, false);
        mIGanhuoPresenter = new GanhuoPresenter(ServiceGenerator.ganhuoService(),this);
        initViews(_view);
        return _view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initViews(View rootView) {
        mRecyclerView = (XRecyclerView) rootView.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.recyclerview_header, (ViewGroup) getActivity().findViewById(R.id.container), false);
        mRecyclerView.addHeaderView(header);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mIGanhuoPresenter.refreshGanhuoList();
            }

            @Override
            public void onLoadMore(){
                mIGanhuoPresenter.loadMoreGanhuoList();
            }
        });
        mAdapter = new AndroidAdapter(mRecyclerView, null, R.layout.item_ganhuo_android);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setRefreshing(true);
    }


    @Override
    public void onGanhuoListLoadMore(List<GanhuoAndroid> ganhuoAndroids) {
        mAdapter.addAll(ganhuoAndroids);
        mRecyclerView.loadMoreComplete();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGanhuoListRefresh(List<GanhuoAndroid> ganhuoAndroids) {
        mAdapter.refresh(ganhuoAndroids);
        mRecyclerView.refreshComplete();
        mAdapter.notifyDataSetChanged();
    }
}
